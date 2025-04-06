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

@TeleOp(name = "TestClaw")
public class TestClaw extends OpMode {
    private GamepadEx gamepadDriver, gamepadMechanism; // Definirea gammepad-urilor

    private Servo claw;

    @Override
    public void init() {
        gamepadDriver = new GamepadEx(gamepad1);
        gamepadMechanism = new GamepadEx(gamepad2);

        claw = hardwareMap.get(Servo.class, "claw");

        claw.setPosition(0.5); // Deschiderea cleștelui
    }

    @Override
    public void loop() {
        claw.setPosition(claw.getPosition() + gamepadMechanism.getRightY()*0.0001);
        telemetry.addData("Claw", claw.getPosition());
        telemetry.update();
    }

}