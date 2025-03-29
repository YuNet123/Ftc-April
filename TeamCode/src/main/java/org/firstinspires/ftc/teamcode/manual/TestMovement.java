package org.firstinspires.ftc.teamcode.manual;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TestMovement extends OpMode {
    private Motor fl,fr,bl,br;
    @Override
    public void init() {
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


    }

    @Override
    public void loop() {
        if(gamepad1.a){
            fl.set(1);
        }
        if(gamepad1.x){
            fr.set(1);
        }
        if(gamepad1.b){
            bl.set(1);
        }
        if(gamepad1.y){
            br.set(1);
        }
        if(gamepad1.right_bumper){
            fl.set(0);
        }
    }
}
