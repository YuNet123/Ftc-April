package org.firstinspires.ftc.teamcode.manual;

import static android.os.SystemClock.sleep;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

//@TeleOp(name = "qzec")
public class ServoTester extends OpMode {
    private ServoEx claw;
    private GamepadEx gamepadDriver, gamepadMechanism;
    private MecanumDrive drive;
    private CRServo pull1, pull2;
    private ServoEx lift2, liftArm;
//    private final int start1, start2;

    @Override
    public void init() {
        gamepadDriver = new GamepadEx(gamepad1);
        gamepadMechanism = new GamepadEx(gamepad2);
        Motor fl, fr, bl, br;
        // 0 backLeft 1 // 1 frontLeft 2 rightBack // 3 frontRight

        fr = new Motor(hardwareMap, "fr");
        fr.setInverted(true);
        fr.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        fl = new Motor(hardwareMap, "fl");
        fl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
//        fl.setInverted(true);
        bl = new Motor(hardwareMap, "bl");
        bl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        bl.setInverted(true);
        br = new Motor(hardwareMap, "br");
        br.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
//        br.setInverted(true);
        drive = new MecanumDrive(
                fl,
                fr,
                bl,
                br
        );
        //drive.setMaxSpeed(1);

        claw = new SimpleServo(hardwareMap, "claw", 0, 180);
        liftArm = new SimpleServo(hardwareMap, "liftArm", 0, 180); // port 4
        pull1 = hardwareMap.get(CRServo.class, "pull1");
        pull2 = hardwareMap.get(CRServo.class, "pull2");
        lift2 = new SimpleServo(hardwareMap, "lift2", 0, 180);
//        ext1 = new SimpleServo(hardwareMap, "ext1", 0, 180);
        pull1.setDirection(CRServo.Direction.REVERSE);


        pull1.setPower(0);
        pull2.setPower(0);
        liftArm.setPosition(0.28);
        claw.setPosition(0.7);

    }

    @Override
    public void loop() {
        // Get gamepad values
        double leftX = gamepadDriver.getLeftX();
        double leftY = gamepadDriver.getLeftY();
        double rightX = gamepadDriver.getRightX();

        // Apply the threshold filter
        leftX = Math.abs(leftX) > 0.1 ? leftX : 0;
        leftY = Math.abs(leftY) > 0.1 ? leftY : 0;
        rightX = Math.abs(rightX) > 0.1 ? rightX : 0;

        // Call the drive method with the filtered values
        drive.driveRobotCentric(leftX, leftY, rightX);


        if (gamepadMechanism.getButton(GamepadKeys.Button.DPAD_LEFT)) {
            claw.setPosition(0.88);
            //sleep(250);
            liftArm.setPosition(0.83);
        }
        if(gamepadMechanism.getButton(GamepadKeys.Button.DPAD_RIGHT)){
            claw.setPosition(0.7);
            //sleep(250);
            liftArm.setPosition(0.28);
        }
        lift2.setPosition(gamepadMechanism.getLeftY());


        if (gamepadMechanism.getButton(GamepadKeys.Button.LEFT_BUMPER)){
            if (pull1.getDirection() == CRServo.Direction.FORWARD && pull2.getDirection() == CRServo.Direction.REVERSE){
                pull1.setDirection(CRServo.Direction.REVERSE);
                pull2.setDirection(CRServo.Direction.FORWARD);
            }
            pull1.setPower(1);
            pull2.setPower(1);
        }
        if (gamepadMechanism.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
            if (pull1.getDirection() == CRServo.Direction.REVERSE && pull2.getDirection() == CRServo.Direction.FORWARD){
                pull1.setDirection(CRServo.Direction.FORWARD);
                pull2.setDirection(CRServo.Direction.REVERSE);
            }
            pull1.setPower(1);
            pull2.setPower(1);
        }

        if (gamepadMechanism.getButton(GamepadKeys.Button.B)) {
            pull1.setPower(0);
            pull2.setPower(0);// Toggle between 0 and 1
        }
    }
}