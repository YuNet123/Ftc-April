package org.firstinspires.ftc.teamcode.manual;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@TeleOp(name = "OldTele")
public class OldTele extends OpMode {
    private GamepadEx gamepadDriver, gamepadMechanism;
    private MecanumDrive drive;
    private Motor arm, ext;
    private PIDController armPID = new PIDController(0.01, 0, 0);
    private ToggleButtonReader extensionToggle, clawToggle, pitchToggle;
    private ServoEx servoPitch, servoClaw;

    @Override
    public void init() {
        gamepadDriver = new GamepadEx(gamepad1);
        gamepadMechanism = new GamepadEx(gamepad2);

        Motor frontLeft = new Motor(hardwareMap, "frontLeftMotor");
        frontLeft.setInverted(true);

        Motor backLeft = new Motor(hardwareMap, "backLeftMotor");
        backLeft.setInverted(true);

        drive = new MecanumDrive(
                frontLeft,
                new Motor(hardwareMap, "frontRightMotor"),
                backLeft,
                new Motor(hardwareMap, "backRightMotor")
        );

        arm = new Motor(hardwareMap, "Mbrat");
        arm.resetEncoder();

        ext = new Motor(hardwareMap, "Mextind");
//        ext.setRunMode(Motor.Run,  Mode.PositionControl);
        ext.resetEncoder();

        extensionToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.A);

        servoPitch = new SimpleServo(hardwareMap, "CH_servo0", 0, 180);
        servoClaw = new SimpleServo(hardwareMap, "CH_servo1", 0, 180);

        servoPitch.setPosition(0.5);
        servoClaw.setPosition(0);

        clawToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.X);
        pitchToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.B);
    }
    @Override
    public void init_loop() {
        arm.set(armPID.calculate(arm.getCurrentPosition(), 1050));
    }

    @Override
    public void loop() {
        drive.driveRobotCentric(gamepadDriver.getLeftX(), gamepadDriver.getLeftY(), gamepadDriver.getRightX());
        arm.set(gamepadMechanism.getLeftY());

        extensionToggle.readValue();
        if (extensionToggle.getState()) {
            ext.setTargetPosition(-1100);
        } else {
            ext.setTargetPosition(0);
        }
        ext.set(0.01);

        clawToggle.readValue();
        if (clawToggle.getState()) {
            servoClaw.setPosition(0);
        } else {
            servoClaw.setPosition(0.7);
        }

        pitchToggle.readValue();
        if (pitchToggle.getState()) {
            servoPitch.setPosition(1);
        } else {
            servoPitch.setPosition(0.57);
        }
    }
}


