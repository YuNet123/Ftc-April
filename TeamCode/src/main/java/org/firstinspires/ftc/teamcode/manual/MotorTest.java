package org.firstinspires.ftc.teamcode.manual;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

//@TeleOp(name = "MotorTest")
public class MotorTest extends OpMode {
    private GamepadEx gamepadDriver;
    public DcMotor motor;
    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "Mbrat");
    }

    @Override
    public void loop() {
        if(gamepadDriver.gamepad.left_stick_y > 0){
            motor.setPower(gamepadDriver.gamepad.left_stick_y);
        }
        motor.setPower(0);
    }
}
