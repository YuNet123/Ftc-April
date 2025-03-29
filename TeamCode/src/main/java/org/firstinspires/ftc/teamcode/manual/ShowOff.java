package org.firstinspires.ftc.teamcode.manual;

import static android.os.SystemClock.sleep;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//@TeleOp(name = "ShowOff")
public class ShowOff extends OpMode {
    private ServoEx claw;
    private static final double TURN_SCALE = 0.45; // Adjust this value (0.1 to 0.5)
    private static final double INPUT_SMOOTHING = 0.2; // Adjust smoothing (0.1 to 0.5)
    private double previousTurn = 0;
    private GamepadEx gamepadDriver, gamepadMechanism;
    private MecanumDrive drive;
    private CRServo pull1, pull2;
    private ServoEx ext2, lift1, lift2, liftArm;
    private DcMotor arm1,arm2;
//    private final int start1, start2;

    @Override
    public void init() {
        gamepadDriver = new GamepadEx(gamepad1);
        gamepadMechanism = new GamepadEx(gamepad2);
        Motor fl, fr, bl, br;
        // 0 backLeft 1 // 1 frontLeft 2 rightBack // 3 frontRight

        fr = new Motor(hardwareMap, "fr");
        fr.setInverted(true);
        fl = new Motor(hardwareMap,"fl");
//        fl.setInverted(true);
        bl = new Motor(hardwareMap, "bl");
        bl.setInverted(true);
        br = new Motor(hardwareMap,"br");
//        br.setInverted(true);
        drive = new MecanumDrive(
                fl,
                fr,
                bl,
                br
        );
        drive.setMaxSpeed(0.45);


        // actual back left
//        Motor frontLeft = new Motor(hardwareMap, "frontLeftMotor");
//        frontLeft.setInverted(true);

        // actual back right
//        Motor backLeft = new Motor(hardwareMap, "backLeftMotor");
//        backLeft.setInverted(true);

            // Actual front left
//        Motor frontRight = new Motor(hardwareMap, "frontRightMotor");
//        frontRight.setInverted(true);

        // actual front right
//        Motor backRight = new Motor(hardwareMap, "backRightMotor");
//        backRight.setInverted(true);

//        drive = new MecanumDrive(
//                frontRight,
//                new Motor(hardwareMap, "backRightMotor"),
//                frontLeft,
//                new Motor(hardwareMap, "backLeftMotor")
//        );
        // jos inside sus outside
        claw = new SimpleServo(hardwareMap, "claw", 0, 180);
        liftArm = new SimpleServo(hardwareMap, "liftArm", 0, 180); // port 4
        pull1 = hardwareMap.get(CRServo.class, "pull1");
        pull2 = hardwareMap.get(CRServo.class, "pull2");
        lift1 = new SimpleServo(hardwareMap, "lift1", 0, 180);
        lift2 = new SimpleServo(hardwareMap, "lift2", 0, 180);
//        ext1 = new SimpleServo(hardwareMap, "ext1", 0, 180);
        ext2 = new SimpleServo(hardwareMap, "ext2 ", 0, 180);
        pull1.setDirection(CRServo.Direction.REVERSE);

        arm1 = hardwareMap.get(DcMotor.class, "Motor1");
        arm2 = hardwareMap.get(DcMotor.class, "Motor2");

        ext2.setPosition(0);
        pull1.setPower(0);
        pull2.setPower(0);
        liftArm.setPosition(0.28);
        claw.setPosition(0.7);
        // TODO:
        drive.setMaxSpeed(0.6);
    }

    @Override
    public void loop() {
        // Get raw turn input
        double rawTurn = gamepadDriver.getRightX();

        // Apply cubic scaling for finer control (optional)
        double scaledTurn = Math.copySign(Math.pow(Math.abs(rawTurn), 3), rawTurn);

        // Apply low-pass filter for smoothing
        double smoothTurn = (previousTurn * (1 - INPUT_SMOOTHING)) + (scaledTurn * INPUT_SMOOTHING);
        previousTurn = smoothTurn;

        // Apply final turn scaling
        double finalTurn = smoothTurn * TURN_SCALE;
        drive.driveRobotCentric(gamepadDriver.getLeftX(), gamepadDriver.getLeftY(),
                finalTurn);

        if (gamepad2.right_trigger > 0.1) {
            telemetry.addData(claw.getPosition()+"","");
            if (claw.getPosition() > 0.7 && claw.getPosition() < 0.88){
                telemetry.addData("1","");
                claw.setPosition(0.88);
                sleep(500);
                liftArm.setPosition(0.7);
                sleep(500);
            }
            else if (claw.getPosition() >= 0.8){
                claw.setPosition(0.7);
                sleep(500);
                liftArm.setPosition(0.28);
                sleep(500);
            }
        }

//        if(gamepad2.left_trigger > 0.1){
//            claw.setPosition(0.7);
//            sleep(1000);
//            liftArm.setPosition(0.28);
//        }
        lift2.setPosition(gamepadMechanism.getLeftY());


        if (gamepadMechanism.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
            if (pull1.getDirection() == CRServo.Direction.FORWARD && pull2.getDirection() == CRServo.Direction.REVERSE){
                pull1.setDirection(CRServo.Direction.REVERSE);
                pull2.setDirection(CRServo.Direction.FORWARD);
            }
            pull1.setPower(1);
            pull2.setPower(1);
        }
        if (gamepadMechanism.getButton(GamepadKeys.Button.LEFT_BUMPER)){
            if (pull1.getDirection() == CRServo.Direction.REVERSE && pull2.getDirection() == CRServo.Direction.FORWARD){
                pull1.setDirection(CRServo.Direction.FORWARD);
                pull2.setDirection(CRServo.Direction.REVERSE);
            }
            pull1.setPower(1);
            pull2.setPower(1);
        }
        if (gamepadMechanism.getButton(GamepadKeys.Button.B)){
            pull1.setPower(0);
            pull2.setPower(0);
        }
        if (gamepad2.touchpad){
            if (ext2.getPosition() == 0) {
                ext2.setPosition(1);
                sleep(250);
            }
            else{
                ext2.setPosition(0);
                sleep(250);
            }
            }
    }

//    @Override
//    public void loop() {
//        drive.driveRobotCentric(gamepadDriver.getLeftX(), gamepadDriver.getLeftY(), gamepadDriver.getRightX());
//
//
//        if (gamepadMechanism.getButton(GamepadKeys.Button.A)) {
////            ext2.setPosition(ext2.getPosition() == 0 ? 1 : 0);  // Toggle between 0 and 1
//            if (ext2.getPosition() == 0) ext2.setPosition(1);
//            else ext2.setPosition(0);
//            sleep(250);
//        }
//
//        if (gamepadMechanism.getButton(GamepadKeys.Button.DPAD_LEFT)) {
//            claw.setPosition(0.88);
//        }
//        if(gamepadMechanism.getButton(GamepadKeys.Button.DPAD_RIGHT)){
//            claw.setPosition(0.7);
//        }
//
//        lift1.setPosition(-gamepadMechanism.getLeftY());
//        lift2.setPosition(gamepadMechanism.getLeftY());
//
////        if (gamepadMechanism.getButt
////        on(GamepadKeys.Button.LEFT_BUMPER)) {
////            liftArm.setPosition(liftArm.getPosition() == 0 ? 0.8 : 0);  // Toggle between 0 and 0.85
////        }
//
//        telemetry.addData("q + " +liftArm.getPosition(), "q");
//        telemetry.update();
//        if (gamepadMechanism.getButton(GamepadKeys.Button.LEFT_BUMPER)){
//            if (pull1.getDirection() == CRServo.Direction.FORWARD && pull2.getDirection() == CRServo.Direction.REVERSE){
//                pull1.setDirection(CRServo.Direction.REVERSE);
//                pull2.setDirection(CRServo.Direction.FORWARD);
//            }
//            pull1.setPower(1);
//            pull2.setPower(1);
//        }
//        if (gamepadMechanism.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
//            if (pull1.getDirection() == CRServo.Direction.REVERSE && pull2.getDirection() == CRServo.Direction.FORWARD){
//                pull1.setDirection(CRServo.Direction.FORWARD);
//                pull2.setDirection(CRServo.Direction.REVERSE);
//            }
//            pull1.setPower(1);
//            pull2.setPower(1);
//        }
//
//        if (gamepadMechanism.getButton(GamepadKeys.Button.B)) {
//            telemetry.addData("qq", "qe");
//            pull1.setPower(0);
//            pull2.setPower(0);// Toggle between 0 and 1
//        }
//
//
//        if (gamepadMechanism.getButton(GamepadKeys.Button.DPAD_DOWN)){
//            telemetry.addData("down","");
//            liftArm.setPosition(0.28);
//        }
//        if (gamepad2.left_trigger > 0.1){
//            liftArm.setPosition(0.7);
//        }
//        if (gamepad2.right_trigger > 0.1){
//            telemetry.addData("up","");
//            liftArm.setPosition(0.83);
//        }
//
//
//
////        liftArm.setPosition(gamepadMechanism.getRightY());
//          telemetry.addData(""+ liftArm.getPosition(), "q");
////        arm1.setPower(gamepadMechanism.getLeftY());
////        arm2.setPower(-gamepadMechanism.getLeftY());
//
//    }

}
