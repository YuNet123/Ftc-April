package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.MecanumDrive;


//@Autonomous(name = "Autonomy1", group = "Autonomous")
public class Autonomy1 extends LinearOpMode {
    public CRServo pull1, pull2;
    private Servo lift1, lift2, arm1, arm2, claw, ext1, ext2, outputArm1, outputArm2;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, -63, 3*Math.PI/2);
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);
        pull1 = hardwareMap.get(CRServo.class, "pull1");
        pull2 = hardwareMap.get(CRServo.class, "pull2");
        pull1.setDirection(CRServo.Direction.REVERSE);

        // port 5,0
        lift1 = hardwareMap.get(Servo.class, "lift1");
        lift2 = hardwareMap.get(Servo.class, "lift2");

        // port 3,2
        ext1 = hardwareMap.get(Servo.class, "ext1"); // 3
        ext2 = hardwareMap.get(Servo.class, "ext2"); // 2
        ext2.setDirection(Servo.Direction.REVERSE);

        // Control hub
        // port 4
        claw = hardwareMap.get(Servo.class, "claw");

        // port 5,0
        arm1 = hardwareMap.get(Servo.class, "arm1");
        arm2 = hardwareMap.get(Servo.class, "arm2");
        arm1.setDirection(Servo.Direction.REVERSE);

        // port 1,3
        outputArm1 = hardwareMap.get(Servo.class, "output arm 1");
        outputArm2 = hardwareMap.get(Servo.class, "output arm 2");

        ext1.setPosition(0.35); // 0.47
        ext2.setPosition(0.35); // 0.53

        pull1.setPower(0);
        pull2.setPower(0);

        outputArm1.setPosition(0.358); // 0.15
        outputArm2.setPosition(0.637);

        lift1.setPosition(0.8);
        lift2.setPosition(0.2);

        claw.setPosition(0.4);

        outputArm1.setPosition(0.358); // 0.15
        outputArm2.setPosition(0.637); // 0.85


        TrajectoryActionBuilder tr1 = drive.actionBuilder(beginPose)
                        .lineToY(-30);

        TrajectoryActionBuilder tr2 = tr1.endTrajectory().fresh()
                .strafeTo(new Vector2d(0,-40));

        TrajectoryActionBuilder tr3 = tr2.endTrajectory().fresh()
                .turn(Math.toRadians(60))
                .strafeTo(new Vector2d(47, -55))
                .turn(Math.toRadians(-60))
                .strafeTo(new Vector2d(0,-30))
                .strafeTo(new Vector2d(0,-40));

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(

                new SequentialAction(
                        tr1.build(),
                        new InstantAction(() ->{
                            claw.setPosition(0.6);
                        }),
                        new SleepAction(1),
                        new InstantAction(() ->{
                            lift1.setPosition(1);
                            lift2.setPosition(0);
                            outputArm1.setPosition(0.7); // 0.85
                            outputArm2.setPosition(0.3);
                        }),
                        new SleepAction(1),
                        tr2.build(),
                        new SleepAction(1),
                        new InstantAction(() ->{
                            claw.setPosition(0.4);
                            outputArm1.setPosition(0.358); // 0.15
                            outputArm2.setPosition(0.637); // 0.85
                        }),
                        tr3.build()
                )
//                drive.actionBuilder(beginPose)
//                        .strafeTo(new Vector2d(0,-30))
////                        .stopAndAdd()
//                        .strafeTo(new Vector2d(0,-35))
//                        .turn(Math.toRadians(60))
//                        .strafeTo(new Vector2d(47, -55))
//                        .turn(Math.toRadians(-60))
//                        .strafeTo(new Vector2d(0,-30))
//                        .strafeTo(new Vector2d(0,-35))
//                        .build()
        );
    }

}
