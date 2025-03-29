package org.firstinspires.ftc.teamcode.manual;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@TeleOp(name = "Output Motor Test")
public class OutputMotorTest extends OpMode {
    private MotorGroup motors;
    private GamepadEx gamepad;


    @Override
    public void init() {
        Motor motor1 = new Motor(hardwareMap, "motor1");
        Motor motor2 = new Motor(hardwareMap, "motor2");

        // Daca trebuie de inversat
        motor2.setInverted(true);

        motors = new MotorGroup(motor1, motor2);

        gamepad = new GamepadEx(gamepad1);
    }

    @Override
    public void loop() {
        // Schimbati coeficientul pentru a ajusta viteza
        motors.set(gamepad.getLeftY() * 0.3);
    }
}
