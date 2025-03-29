package org.firstinspires.ftc.teamcode.manual;

import static org.firstinspires.ftc.teamcode.autonomous.lib.sleep;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//@TeleOp(name = "TestArm")
public class TestArm extends OpMode {
    private GamepadEx gamepadDriver, gamepadMechanism;
    private Servo outputArm1;
    private Servo outputArm2;
    private CRServo pull1, pull2;
    private DcMotor motor1, motor2;

    // Toggle flags
    private boolean outputArmToggled = false;

    @Override
    public void init() {
        gamepadDriver = new GamepadEx(gamepad1);
        gamepadMechanism = new GamepadEx(gamepad2);

        motor1 = hardwareMap.get(DcMotor.class, "motorLift1");
        motor2 = hardwareMap.get(DcMotor.class, "motorLift2");


        // port 1,3
        outputArm1 = hardwareMap.get(Servo.class, "output arm 1");
        outputArm2 = hardwareMap.get(Servo.class, "output arm 2");

//        pull1 = hardwareMap.get(CRServo.class, "pull1");
//        pull1.setDirection(CRServo.Direction.REVERSE);
//        pull2 = hardwareMap.get(CRServo.class, "pull2");


        outputArm1.setPosition(0.5);
        outputArm2.setPosition(0.5);
    }

    @Override
    public void loop() {
//        double rightY = gamepadDriver.getRightY();


        motor1.setPower(gamepadDriver.getRightY());
        motor2.setPower(gamepadDriver.getRightY());


//        if (gamepadDriver.gamepad.x){
//            pull1.setPower(1);
//            pull2.setPower(1);
//        }
//        if (gamepadDriver.gamepad.y){
//            pull1.setPower(0);
//            pull2.setPower(0);
//        }

        // OLD

//        if (gamepadDriver.gamepad.x){
//            outputArm1.setPosition(0.58);
//            outputArm2.setPosition(0.4);
//        }
//        if (gamepadDriver.gamepad.y){
//            outputArm1.setPosition(0.259);
//            outputArm2.setPosition(0.732);
//        }

        if (gamepadDriver.gamepad.dpad_down){
            outputArm1.setPosition(0.325);
            outputArm2.setPosition(0.673);
        }
        if (gamepadDriver.gamepad.dpad_up){
            outputArm1.setPosition(0.68);
            outputArm2.setPosition(0.316);
        }
        // Output arm position toggle (D-Pad Up/Down)
//        if (gamepadDriver.getRightY() != 0){
//            if ((outputArm1.getPosition() - gamepadDriver.getRightY()*0.001 >= 0.47 &&
//                    outputArm1.getPosition() - gamepadDriver.getRightY()*0.001 <= 0.75) &&
//                    (outputArm2.getPosition() + gamepadDriver.getRightY()*0.001 >= 0.25 &&
//                            outputArm2.getPosition() + gamepadDriver.getRightY()*0.001 <= 0.53)
//            )
//            {
//                outputArm1.setPosition(outputArm1.getPosition() - gamepadDriver.getRightY()*0.001);
//                outputArm2.setPosition(outputArm2.getPosition() + gamepadDriver.getRightY()*0.001);
//
//            }
            telemetry.addData("righty ", gamepadDriver.getRightY());
//            telemetry.addData("position ", outputArm1.getPosition());
//            telemetry.addData("position ", outputArm2.getPosition());
            telemetry.update();

        }
    }

