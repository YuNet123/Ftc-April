package org.firstinspires.ftc.teamcode.manual;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//@TeleOp(name = "ServosTest" + "")
public class ServosTest extends OpMode {
    private ServoEx servo1, servo2;
    private GamepadEx gamepadDriver;
    private DcMotor arm1,arm2;
    @Override
    public void init() {
//        servo1 = new SimpleServo(hardwareMap, "servo1", 0, 180);
        servo2 = new SimpleServo(hardwareMap, "servo2", 0, 180); // port 4
        //arm1 = hardwareMap.get(DcMotor.class, "motor1");
        gamepadDriver = new GamepadEx(gamepad1);

//        arm1 = new Motor(hardwareMap, "Motor1");
        arm1 = hardwareMap.get(DcMotor.class, "Motor1");
        arm2 = hardwareMap.get(DcMotor.class, "Motor2");

        //        arm1.resetEncoder();
    }

    @Override
    public void loop() {
//        if(gamepad1.a){
//            servo2.setPosition(0.7);
//        }
        arm1.setPower(gamepadDriver.getLeftY());
        arm2.setPower(-gamepadDriver.getLeftY());

        // arm2 din stanga
        // arm1 din dreapta
    }
}
