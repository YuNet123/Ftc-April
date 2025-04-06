package org.firstinspires.ftc.teamcode.manual;

import static org.firstinspires.ftc.teamcode.autonomous.lib.sleep;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Main")
public class Main extends OpMode {
    private GamepadEx gamepadDriver, gamepadMechanism; // Definirea gammepad-urilor
    public CRServo pull1, pull2; // Tragerea înăuntru a specimenilor
    private Servo lift1, lift2, claw; // Controlează mișcarea platformei, cleștelui, a extensiilor
    private Servo ext1, ext2, outputArm1, outputArm2; // Controlează mișcarea extensiei și a brațului
    private MecanumDrive drive; //  Controlează mișcarea robotului
    private Motor fl, fr, bl, br, sl, sr; // Motoarele de mișcare a robotului și de ridicare a brațului
    private ToggleButtonReader extToggle, pullInToggle, pullOutToggle, clawToggle, slowModeToggle; // Butoanele ce lucrează ca toggles
    private double driveCoeficent = 1; // Folosit pentru schimbarea de viteză a robotului
    private ElapsedTime actionTimer = new ElapsedTime(); // Definerea timer-ului
    private enum ClawState { // Definirea stărilor cleștelui
        IDLE,
        INIT_LIFT,
        GRAB,
        WAIT_AFTER_GRAB,
        EXTEND_ARM,
        COMPLETE
    }
    private ClawState clawState = ClawState.IDLE;

    @Override
    public void init() {
        gamepadDriver = new GamepadEx(gamepad1);
        gamepadMechanism = new GamepadEx(gamepad2);

        // Initialize motors
        // Expansion hub
        // port 2, 0, 1, 3
        // fr = frontRight, fl = frontLeft, bl = backLeft, br = backRight
        fr = new Motor(hardwareMap, "fr"); // fr = frontRight, port = 2
        fr.setInverted(true);
        fr.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        fl = new Motor(hardwareMap, "fl"); // fl = frontLeft, port = 0
        fl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        bl = new Motor(hardwareMap, "bl"); // bl = backLeft, port = 1
        bl.setInverted(true);
        bl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        br = new Motor(hardwareMap, "br"); // br = backRight, port = 3
        br.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        // Control hub
        // port 2,3
        sl = new Motor(hardwareMap, "motor1");
        sl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        sr = new Motor(hardwareMap, "motor2");
        sr.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        sl.setInverted(true);

        drive = new MecanumDrive(fl, fr, bl, br);
        drive.setMaxSpeed(1);

        // Initialize servos and CR servos
        // Expansion hub
        // port 1,4
        pull1 = hardwareMap.get(CRServo.class, "pull1");
        pull2 = hardwareMap.get(CRServo.class, "pull2");
        pull1.setDirection(CRServo.Direction.REVERSE);

        // port 5,0
        lift1 = hardwareMap.get(Servo.class, "lift1");
        lift2 = hardwareMap.get(Servo.class, "lift2");

        // port 3,2
        ext1 = hardwareMap.get(Servo.class, "ext1"); // 3
        ext2 = hardwareMap.get(Servo.class, "ext2"); // 2
        ext2.setDirection(Servo.Direction.REVERSE);

        // Control hub
        // port 4
        claw = hardwareMap.get(Servo.class, "claw");

        // port 1,3
        outputArm1 = hardwareMap.get(Servo.class, "output arm 1");
        outputArm2 = hardwareMap.get(Servo.class, "output arm 2");

        ext1.setPosition(0.35); // Retragerea extensiei în spatae
        ext2.setPosition(0.35);

        pull1.setPower(0); // Confirmăm că puterea la servo-uri
        pull2.setPower(0); // continue este 0

        claw.setPosition(0.4); // Deschiderea cleștelui

        outputArm1.setPosition(0.358); // down
        outputArm2.setPosition(0.637);

        pullInToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.B);
        pullOutToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.X);
        clawToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.LEFT_BUMPER);
        extToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.A);

        slowModeToggle = new ToggleButtonReader(gamepadDriver, GamepadKeys.Button.A);
    }

    @Override
    public void loop() {
        slowModeToggle.readValue();
        if (slowModeToggle.wasJustPressed()){
            if (driveCoeficent == 1) driveCoeficent = 0.3;
            else driveCoeficent = 1;
        }

        drive.driveRobotCentric(gamepadDriver.getLeftX() * driveCoeficent,
                gamepadDriver.getLeftY()  * driveCoeficent,
                gamepadDriver.getRightX()  * driveCoeficent);

        // Elevator controls (up/down)
        sl.set(gamepadMechanism.getRightY() * 0.8);
        sr.set(gamepadMechanism.getRightY() * 0.8);

        // Intake toggle (B button)
        pullInToggle.readValue(); // update the toggle state
        if (pullInToggle.wasJustPressed()) {
            pull1.setDirection(CRServo.Direction.REVERSE);
            pull2.setDirection(DcMotorSimple.Direction.FORWARD);
            if (pull1.getPower() == 0){
                pull1.setPower(1);
                pull2.setPower(1);
            } else {
                pull1.setPower(0);
                pull2.setPower(0);
            }
        }

        pullOutToggle.readValue(); // update the toggle state
        if (pullOutToggle.wasJustPressed()) {
            pull1.setDirection(CRServo.Direction.FORWARD);
            pull2.setDirection(DcMotorSimple.Direction.REVERSE);
            if (pull1.getPower() == 0){
                pull1.setPower(1);
                pull2.setPower(1);
            } else {
                pull1.setPower(0);
                pull2.setPower(0);
            }
        }

        // Lift control based on triggers
        if (gamepadMechanism.gamepad.left_trigger > 0) { // Intake down
            lift1.setPosition(1);
            lift2.setPosition(0);
        }
        if (gamepadMechanism.gamepad.right_bumper) { // Intake middle
            lift1.setPosition(0.8);
            lift2.setPosition(0.2);
        }
        if (gamepadMechanism.gamepad.right_trigger > 0) { // Intake up
            lift1.setPosition(0.6);
            lift2.setPosition(0.4);
        }

        if (gamepadMechanism.gamepad.y && clawState == ClawState.IDLE) {
            clawState = ClawState.INIT_LIFT;
            actionTimer.reset();
            lift1.setPosition(0.8);
            lift2.setPosition(0.2);
        }

        switch (clawState) { // Timere în funcție de
            case INIT_LIFT:  // poziția la clește
                if (actionTimer.milliseconds() > 350) { // Așteptare de 250ms
                    clawState = ClawState.GRAB; // Setarea noii stări a cleștelui
                    actionTimer.reset(); // Resetarea timerului
                }
                break;

            case GRAB:
                claw.setPosition(0.6);
                clawState = ClawState.WAIT_AFTER_GRAB;
                actionTimer.reset();
                break;

            case WAIT_AFTER_GRAB:
                // Wait another 250ms after the claw grabs
                if (actionTimer.milliseconds() > 350) {
                    clawState = ClawState.EXTEND_ARM;
                    actionTimer.reset();
                }
                break;

            case EXTEND_ARM:
                // Extend the output arm as part of the sequence
                outputArm1.setPosition(0.7); // extended position
                outputArm2.setPosition(0.3);
                // Reset the lift to its final position
                lift1.setPosition(1);
                lift2.setPosition(0);
                clawState = ClawState.COMPLETE;
                break;

            case COMPLETE:
                // Sequence complete; reset state for next use
                clawState = ClawState.IDLE;
                break;

            case IDLE:
            default:
                // Do nothing in the idle state
                break;
        }

        // Claw open/close toggle (Left Bumper)
        clawToggle.readValue();
        if (clawToggle.wasJustPressed()) {
            if (claw.getPosition() < 0.6){
                claw.setPosition(0.6);
            }
            else{
                claw.setPosition(0.4);
            }
        }

        if (gamepadMechanism.gamepad.dpad_left) {
            claw.setPosition(0.4);
            outputArm1.setPosition(0.358); // 0.15
            outputArm2.setPosition(0.637); // 0.85
        }

        if (gamepadMechanism.gamepad.dpad_right) {
            pull1.setPower(0);
            pull2.setPower(0);
            lift1.setPosition(0.6);
            lift2.setPosition(0.4);
            ext1.setPosition(0.35); // Retragerea extensiei în spatae
            ext2.setPosition(0.35);
        }

        // Output arm position toggle (D-Pad Up/Down)
        if (gamepadMechanism.gamepad.dpad_up) {
            outputArm1.setPosition(0.7); // 0.85
            outputArm2.setPosition(0.3); // 0.15
        }
        if (gamepadMechanism.gamepad.dpad_down) {
            outputArm1.setPosition(0.358); // 0.15
            outputArm2.setPosition(0.637); // 0.85
        }

        if (gamepadMechanism.getLeftY() != 0){
            if ((ext1.getPosition() + gamepadMechanism.getLeftY()*0.007 >= 0.35 &&
                    ext1.getPosition() + gamepadMechanism.getLeftY()*0.007 <= 0.68) &&
                    (ext2.getPosition() + gamepadMechanism.getLeftY()*0.007 >= 0.35 &&
                            ext2.getPosition() + gamepadMechanism.getLeftY()*0.007 <= 0.68)
            )
            {
                ext1.setPosition(ext1.getPosition() + gamepadMechanism.getLeftY()*0.007);
                ext2.setPosition(ext2.getPosition() + gamepadMechanism.getLeftY()*0.007);
            }
        }

        extToggle.readValue(); // update toggle state
        if (extToggle.wasJustPressed()) {
            if (ext1.getPosition() < 0.6) {
                ext1.setPosition(0.63);
                ext2.setPosition(0.63);
            } else {
                ext1.setPosition(0.35);
                ext2.setPosition(0.35);
            }
        }

    }

}