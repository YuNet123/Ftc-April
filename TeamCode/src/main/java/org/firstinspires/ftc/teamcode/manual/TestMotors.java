package org.firstinspires.ftc.teamcode.manual;

import static org.firstinspires.ftc.teamcode.autonomous.lib.sleep;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//@TeleOp(name = "TestMotorsServo")
public class TestMotors extends OpMode {
    private Motor motor1, motor2;
    private double ticks = 537.7;
    private Servo ext1, ext2;
    private boolean pullToggled = false, extToggled = false;
    private GamepadEx gamepad;
    private double newTarget;
    public CRServo pull1, pull2;
    private ToggleButtonReader pullInToggle, pullOutToggle, extToggle;
    @Override
    public void init() {
//        motor1 = hardwareMap.get(Motor.class, "motor1");
        motor1 = new Motor(hardwareMap, "motor1");
        motor2 = new Motor(hardwareMap, "motor2");
        ext1 = hardwareMap.get(Servo.class, "ext1"); // 3
        ext2 = hardwareMap.get(Servo.class, "ext2"); // 2
        ext2.setDirection(Servo.Direction.REVERSE);

        motor1.setInverted(true);
        motor1.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        pull1 = hardwareMap.get(CRServo.class, "pull1");
        pull2 = hardwareMap.get(CRServo.class, "pull2");
        pull1.setDirection(CRServo.Direction.REVERSE);


        ext1.setPosition(0.38);
        ext2.setPosition(0.38);
//        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gamepad = new GamepadEx(gamepad2);
        pullInToggle = new ToggleButtonReader(gamepad, GamepadKeys.Button.LEFT_BUMPER);
        pullOutToggle = new ToggleButtonReader(gamepad, GamepadKeys.Button.RIGHT_BUMPER);
        extToggle = new ToggleButtonReader(gamepad, GamepadKeys.Button.RIGHT_BUMPER);
        //motor1.getCurrentPosition();
        //motor2.getCurrentPosition();
    }

    @Override
    public void loop() {
//        motor1.set(gamepad.getRightY() *0.8);
//        motor2.set(gamepad.getRightY() *0.8);

//        ext1.setPosition(ext1.getPosition() + gamepad.getLeftY() *0.007);
//        ext2.setPosition(ext2.getPosition() + gamepad.getLeftY() *0.007);

        if (gamepad.getLeftY() != 0){
                if ((ext1.getPosition() + gamepad.getLeftY()*0.001 >= 0.38 &&
                        ext1.getPosition() + gamepad.getLeftY()*0.001 <= 0.68) &&
                        (ext2.getPosition() + gamepad.getLeftY()*0.001 >= 0.38 &&
                                ext2.getPosition() + gamepad.getLeftY()*0.001 <= 0.68)
                )
                {
                    ext1.setPosition(ext1.getPosition() + gamepad.getLeftY()*0.001);
                    ext2.setPosition(ext2.getPosition() + gamepad.getLeftY()*0.001);
                    telemetry.addData("ext1 ", ext1.getPosition());
                    telemetry.addData("ext2 ", ext2.getPosition());
                    telemetry.update();
                }
            }
//
//        if (gamepad.gamepad.a){
//            ext1.setPosition(0.63);
//            ext2.setPosition(0.63);
//        }
//        if (gamepad.gamepad.b){
//            ext1.setPosition(0.38);
//            ext2.setPosition(0.38);
//        }



//        extToggle.readValue();
//        if (extToggle.getState()){
//            ext1.setPosition(0.63);
//            ext2.setPosition(0.63);
//        } else{ // initial value when program start
//            ext1.setPosition(0.38);
//            ext2.setPosition(0.38);
//        }
//        telemetry.addData("Power ", gamepad.getRightY() * 0.8);
//        telemetry.addData("Power2 ", gamepad.getRightY() *0.8 );


//        telemetry.addData("Motor1 ", motor1.get());
//        telemetry.addData("Motor2 ", motor2.get());
//        telemetry.update();
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

        extToggle.readValue(); // update toggle state
        if (extToggle.wasJustPressed()) {
            if (ext1.getPosition() < 0.6) {
                ext1.setPosition(0.63);
                ext2.setPosition(0.63);
            } else {
                ext1.setPosition(0.38);
                ext2.setPosition(0.38);
            }
        }

    }

}
