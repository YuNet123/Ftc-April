package org.firstinspires.ftc.teamcode.manual;
import android.graphics.Color;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;

//@TeleOp(name="MainTeleop")
public class MainTeleOp extends OpMode {
    // Hardware declarations
    private GamepadEx gamepadDriver, gamepadMechanism;
    private Motor lift1, lift2;
    private ServoEx vacuumArm1, vacuumArm2;
    private ServoEx vacuumHold1, vacuumHold2;
    private ServoEx catchSample;
    private ServoEx claw;
    private CRServo pull1, pull2;
    private ColorSensor colorSensor;
    private MecanumDrive drive;

    // State management
    private boolean vacuumRunning = false;
    private boolean capturedSpecimen = false;
    private boolean isClawClosed = false;
    private long colorDetectedTime = -1;
    private final float[] hsvValues = new float[3];

    // Constants
    private static final int LIFT_HEIGHT = 60; // !!!!!!!!!!!!!!!!!!!!!
    private static final double CLAW_OPEN = 0.1;
    private static final double CLAW_CLOSE = 0.8;
    private static final double ARM_EXTEND = 0.6;
    private static final double ARM_RETRACT = 0.1;
    private static final double HOLD_ENGAGE = 0.8;
    private static final double HOLD_RELEASE = 0.2;
    private static final double CATCH_SECURE = 0.5;
    private static final double CATCH_RELEASE = 0.3;

    @Override
    public void init() {
        // Initialize game pads
        gamepadDriver = new GamepadEx(gamepad1);
        gamepadMechanism = new GamepadEx(gamepad2);

        // Drivetrain initialization
        Motor rightFront = new Motor(hardwareMap, "rightFront");
        Motor leftBack = new Motor(hardwareMap, "leftBack");
        rightFront.setInverted(true);
        leftBack.setInverted(true);

        drive = new MecanumDrive(
                new Motor(hardwareMap, "leftFront"),
                rightFront,
                leftBack,
                new Motor(hardwareMap, "rightBack")
        );

        // Lift initialization
        lift1 = new Motor(hardwareMap, "lift1");
        lift2 = new Motor(hardwareMap, "lift2");
        lift1.setRunMode(Motor.RunMode.PositionControl);
        lift2.setRunMode(Motor.RunMode.PositionControl);
        lift1.setPositionTolerance(50);
        lift2.setPositionTolerance(50);

        // Vacuum system initialization
        vacuumArm1 = new SimpleServo(hardwareMap, "vacuumArm1", 0, 180);
        vacuumArm2 = new SimpleServo(hardwareMap, "vacuumArm2", 0, 180);
        vacuumHold1 = new SimpleServo(hardwareMap, "vacuumHold1", 0, 180);
        vacuumHold2 = new SimpleServo(hardwareMap, "vacuumHold2", 0, 180);
        pull1 = hardwareMap.get(CRServo.class, "pull1");
        pull2 = hardwareMap.get(CRServo.class, "pull2");

        // Capture mechanism
        catchSample = new SimpleServo(hardwareMap, "catchSample", 0, 180);
        claw = new SimpleServo(hardwareMap, "claw", 0, 180);
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        // Initial positions
        claw.setPosition(CLAW_OPEN);
        catchSample.setPosition(CATCH_RELEASE);
    }

    @Override
    public void loop() {
        // Drive control
        drive.driveRobotCentric(
                gamepadDriver.getLeftX(),
                gamepadDriver.getLeftY(),
                gamepadDriver.getRightX()
        );

        // Control systems
        handleVacuumSystem();
        handleLiftControls();
        updateTelemetry();
    }

    private void handleVacuumSystem() {
        if (gamepadMechanism.wasJustPressed(GamepadKeys.Button.X)) {
            if (!vacuumRunning && !capturedSpecimen) {
                startVacuumSequence();
            }
        }

        if (vacuumRunning) {
            checkColorDetection();
        }
    }

    private void startVacuumSequence() {
        vacuumRunning = true;
        // Extend arms and engage holders
        vacuumArm1.setPosition(ARM_EXTEND);
        vacuumArm2.setPosition(ARM_EXTEND);
        vacuumHold1.setPosition(HOLD_ENGAGE);
        vacuumHold2.setPosition(HOLD_ENGAGE);
        // Start suction
        pull1.setPower(1);
        pull2.setPower(1);
        // Prepare catch mechanism
        catchSample.setPosition(CATCH_RELEASE);
    }

    private void stopVacuumSequence() {
        // Retract arms and release holders
        vacuumArm1.setPosition(ARM_RETRACT);
        vacuumArm2.setPosition(ARM_RETRACT);
        vacuumHold1.setPosition(HOLD_RELEASE);
        vacuumHold2.setPosition(HOLD_RELEASE);
        // Stop suction
        pull1.setPower(0);
        pull2.setPower(0);
        // Secure specimen
        catchSample.setPosition(CATCH_SECURE);
        claw.setPosition(CLAW_CLOSE);
        vacuumRunning = false;
        capturedSpecimen = true;
        isClawClosed = true;
    }

    private void checkColorDetection() {
        Color.RGBToHSV(
                colorSensor.red() * 8,
                colorSensor.green() * 8,
                colorSensor.blue() * 8,
                hsvValues
        );

        boolean isTargetColor = (hsvValues[0] >= 20 && hsvValues[0] <= 60) ||   // Yellow
                (hsvValues[0] >= 180 && hsvValues[0] <= 240) ||  // Blue
                (hsvValues[0] <= 10 || hsvValues[0] >= 350);     // Red

        if (isTargetColor && hsvValues[1] > 0.7 && hsvValues[2] > 0.7) {
            if (colorDetectedTime == -1) {
                colorDetectedTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - colorDetectedTime >= 1000) {
                stopVacuumSequence();
                colorDetectedTime = -1;
            }
        } else {
            colorDetectedTime = -1;
        }
    }

    private void handleLiftControls() {
        // Button Y: Raise lift with specimen
        if (gamepadMechanism.wasJustPressed(GamepadKeys.Button.Y)) {
            if (capturedSpecimen) {
                lift1.setTargetPosition(LIFT_HEIGHT);
                lift2.setTargetPosition(LIFT_HEIGHT);
                lift1.set(1);
                lift2.set(1);
                // Ensure claw remains closed
                claw.setPosition(CLAW_CLOSE);
            }
        }

        // Button B: Lower lift and release
        if (gamepadMechanism.wasJustPressed(GamepadKeys.Button.B)) {
            lift1.setTargetPosition(0);
            lift2.setTargetPosition(0);
            lift1.set(1);
            lift2.set(1);
            // Release specimen
            claw.setPosition(CLAW_OPEN);
            catchSample.setPosition(CATCH_RELEASE);
            capturedSpecimen = false;
            isClawClosed = false;
        }
    }

    private void updateTelemetry() {
        telemetry.addData("Vacuum State", vacuumRunning ? "ACTIVE" : "INACTIVE");
        telemetry.addData("Claw State", isClawClosed ? "CLOSED" : "OPEN");
        telemetry.addData("Specimen Captured", capturedSpecimen);
        telemetry.addData("Lift Position", "L1: %d | L2: %d",
                lift1.getCurrentPosition(), lift2.getCurrentPosition());
        telemetry.addData("Color Sensor", "H: %.1f S: %.1f V: %.1f",
                hsvValues[0], hsvValues[1], hsvValues[2]);
        telemetry.update();
    }
}