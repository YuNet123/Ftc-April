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

@TeleOp(name = "Main")
public class Main extends OpMode {
    private GamepadEx gamepadDriver, gamepadMechanism;
    public CRServo pull1, pull2;
    private Servo lift1, lift2, arm1, arm2, claw, ext1, ext2, outputArm1, outputArm2;
    private MecanumDrive drive;
    private Motor fl, fr, bl, br, sl, sr;
    private ToggleButtonReader extToggle, pullInToggle, pullOutToggle, clawToggle;

    // Toggle flags
    private boolean extToggled = false;
    private boolean pullToggled = false;
    private boolean clawToggled = false;
    private boolean outputArmToggled = false;

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

        // port 5,0
        arm1 = hardwareMap.get(Servo.class, "arm1");
        arm2 = hardwareMap.get(Servo.class, "arm2");
        arm1.setDirection(Servo.Direction.REVERSE);

        // port 1,3
        outputArm1 = hardwareMap.get(Servo.class, "output arm 1");
        outputArm2 = hardwareMap.get(Servo.class, "output arm 2");

        pullToggled = false;

        ext1.setPosition(0.35); // 0.47
        ext2.setPosition(0.35); // 0.53

        pull1.setPower(0);
        pull2.setPower(0);
        arm1.setPosition(0.2);
        arm2.setPosition(0.2);
        claw.setPosition(0.4);

        outputArm1.setPosition(0.5); // 0.15
        outputArm2.setPosition(0.5); // 0.85

        pullInToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.B);
        pullOutToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.X);
        clawToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.LEFT_BUMPER);
        extToggle = new ToggleButtonReader(gamepadMechanism, GamepadKeys.Button.A);
    }

    @Override
    public void loop() {
        double leftX = Math.abs(gamepadDriver.getLeftX()) > 0.1 ? gamepadDriver.getLeftX() : 0;
        double leftY = Math.abs(gamepadDriver.getLeftY()) > 0.1 ? gamepadDriver.getLeftY() : 0;
        double rightX = Math.abs(gamepadDriver.getRightX()) > 0.1 ? gamepadDriver.getRightX() : 0;
        // Default drive values from joysticks
        double driveX = leftX;
        double driveY = leftY;
        double driveRotation = rightX;

// Check for digital overrides; if any are pressed, override joystick values
        if (gamepadDriver.gamepad.dpad_down) {
            driveX = 0;
            driveY = 1;
            driveRotation = 0;
        } else if (gamepadDriver.gamepad.dpad_up) {
            driveX = 0;
            driveY = -1;
            driveRotation = 0;
        } else if (gamepadDriver.gamepad.dpad_right) {
            driveX = -1;
            driveY = 0;
            driveRotation = 0;
        } else if (gamepadDriver.gamepad.dpad_left) {
            driveX = 1;
            driveY = 0;
            driveRotation = 0;
        } else if (gamepadDriver.gamepad.a) {
            driveX = 0;
            driveY = 0.6;
            driveRotation = 0;
        } else if (gamepadDriver.gamepad.y) {
            driveX = 0;
            driveY = -0.6;
            driveRotation = 0;
        } else if (gamepadDriver.gamepad.b) {
            driveX = -0.6;
            driveY = 0;
            driveRotation = 0;
        } else if (gamepadDriver.gamepad.x) {
            driveX = 0.6;
            driveY = 0;
            driveRotation = 0;
        } else if (gamepadDriver.gamepad.right_trigger > 0.1 || gamepadDriver.gamepad.left_bumper) {
            driveX = 0.2;
            driveY = 0;
            driveRotation = 1;
        } else if (gamepadDriver.gamepad.left_trigger > 0.1 || gamepadDriver.gamepad.right_bumper) {
            driveX = 0.2;
            driveY = 0;
            driveRotation = -1;
        }

// Call the drive method once per loop iteration
        drive.driveRobotCentric(driveX, driveY, driveRotation);

        // Elevator controls (up/down)
        sl.set(gamepadMechanism.getRightY() * 0.8);
        sr.set(gamepadMechanism.getRightY() * 0.8);



//            outputArm1.setPosition(outputArm1.getPosition() + gamepadMechanism.getLeftY()*0.001);
//            outputArm2.setPosition(outputArm2.getPosition() - gamepadMechanism.getLeftY()*0.001);

//            telemetry.addData("Arm1 ", outputArm1.getPosition());
//            telemetry.addData("Arm2 ", outputArm2.getPosition());

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

        // Automatic claw grab (Y button)
        if (gamepadMechanism.gamepad.y) {
//            ext1.setPosition(0.34);
//            ext2.setPosition(0.34);

            lift1.setPosition(0.8);
            lift2.setPosition(0.2);


            sleep(250);
//
            claw.setPosition(0.6);

            sleep(250);

            outputArm1.setPosition(0.7); // 0.85
            outputArm2.setPosition(0.3); // 0.15
//            sleep(250);
            lift1.setPosition(1);
            lift2.setPosition(0);

//            ext1.setPosition(0.35);
//            ext2.setPosition(0.35);


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
                telemetry.addData("ext1 ", ext1.getPosition());
                telemetry.addData("ext2 ", ext2.getPosition());
                telemetry.update();
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