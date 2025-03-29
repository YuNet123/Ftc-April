package org.firstinspires.ftc.teamcode.autonomous;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Autonomous(name="Auto")
public class Auto2 extends LinearOpMode{
    DcMotor motorLB,motorLF,motorRB,motorRF;
    int limit = 1225;

    @Override
    public void runOpMode() throws InterruptedException{
        motorRB = hardwareMap.get(DcMotor.class, "br");
        motorRF = hardwareMap.get(DcMotor.class, "fr");
        motorLF = hardwareMap.get(DcMotor.class, "fl");
        motorLB = hardwareMap.get(DcMotor.class, "bl");
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        motorLB.setDirection(DcMotorSimple.Direction.REVERSE);
        motorLF.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
//            telemetry.addData("Sr1", sr1.getPosition());
//            telemetry.update();

            DcMotor[] group1 = {motorLF, motorRB};
            DcMotor[] group2 = {motorLB, motorRF};
            DcMotor[] all = {motorRF, motorLB, motorLF, motorRB};

            runMotors(group1, -0.4);
            runMotors(group2, -0.4);
            sleep(1800);
            stopMotors(all);
            requestOpModeStop();

        }
    }
    private void runMotors(@NonNull DcMotor[] motors, double power){
        for(DcMotor motor : motors){
            motor.setPower(power);
        }
    }

    public void stopMotors(@NonNull DcMotor[] motors){
        for(DcMotor motor : motors){
            motor.setPower(0.0);
        }
    }


}



