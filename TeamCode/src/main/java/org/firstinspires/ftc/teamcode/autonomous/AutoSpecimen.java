package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.MecanumDrive.Params.*;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "AutoSpecimen", group = "Autonomous")
public class AutoSpecimen extends LinearOpMode {
    public CRServo pull1, pull2;
    private Servo lift1, lift2, claw, ext1, ext2, outputArm1, outputArm2;

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
                .lineToY(-40.5);

        TrajectoryActionBuilder tr3 = tr2.endTrajectory().fresh()
                .splineTo(new Vector2d(42,-13), Math.PI/2)
                .lineToY(-12)
                .setTangent(3*Math.PI/2)
                .strafeTo(new Vector2d(48.5, -12))
                .setTangent(Math.PI/2)
                .lineToYConstantHeading(-55)
                .lineToYConstantHeading(-42)
                .turn(Math.PI);

        TrajectoryActionBuilder tr4 = tr3.endTrajectory().fresh()
                .lineToYConstantHeading(-58);

        TrajectoryActionBuilder tr5 = tr4.endTrajectory().fresh()
                .strafeTo(new Vector2d(-5,-40))
                .setTangent(Math.PI/2)
                .lineToYConstantHeading(-32);

        TrajectoryActionBuilder tr6 = tr5.endTrajectory().fresh()
                .lineToYConstantHeading(-47);

        TrajectoryActionBuilder tr7 = tr6.endTrajectory().fresh()
                .strafeTo(new Vector2d(48,-40));

        TrajectoryActionBuilder tr8 = tr7.endTrajectory().fresh()
                .setTangent(Math.PI/2)
                .lineToYConstantHeading(-58);

        TrajectoryActionBuilder tr9 = tr8.endTrajectory().fresh()
                .strafeTo(new Vector2d(-4,-40))
                .setTangent(Math.PI/2)
                .lineToYConstantHeading(-32);

        TrajectoryActionBuilder tr10 = tr9.endTrajectory().fresh()
                .lineToYConstantHeading(-49);

        TrajectoryActionBuilder tr11 = tr10.endTrajectory().fresh()
                .strafeTo(new Vector2d(48,-60));

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        tr1.build(), // Mișcarea spre bară
                        new InstantAction(() ->{
                            claw.setPosition(0.6); // Închidere de clește
                        }),
                        new SleepAction(0.5), // Pauză înaintea ridicării brațului
                        new InstantAction(() ->{ // Rudicare brațului și atașarea specimenului
                            outputArm1.setPosition(0.7);
                            outputArm2.setPosition(0.3);
                        }),
                        new SleepAction(0.5), // Pauză pentru a oferi timp pentru mișcare
                        tr2.build(),      // Mișcarea în față pentru a asigura atașarea specimenului
                        new InstantAction(() ->{   // Deschiderea cleștelui și revenirea brațului
                            claw.setPosition(0.4); // Revenirea brațului în poziția de jos
                            outputArm1.setPosition(0.358);
                            outputArm2.setPosition(0.637);
                        }),
                        tr3.build(), // Mișcarea spre următorul sample
                        new InstantAction(() ->{
                            lift1.setPosition(1);
                            lift2.setPosition(0);
//                            maxWheelVel = 20;
                            pull1.setPower(1);
                            pull2.setPower(1);
                        }),
                        tr4.build(),
                        new InstantAction(() -> {
                            maxWheelVel = 200;
                            lift1.setPosition(0.8);
                            lift2.setPosition(0.2);
                            pull1.setPower(1); // q
                            pull2.setPower(1); // q
                        }),
                        tr5.build(),
                        new InstantAction(() -> {
                            claw.setPosition(0.6);
                            pull1.setPower(1); // q
                            pull2.setPower(1); // q
                        }),
                        new SleepAction(0.4),
                        new InstantAction(() ->{
                            lift1.setPosition(1);
                            lift2.setPosition(0);
                            outputArm1.setPosition(0.7); // 0.85
                            outputArm2.setPosition(0.3);
                        }),
                        new SleepAction(0.5),
                        tr6.build(),
                        new InstantAction(() ->{
                            claw.setPosition(0.4);
                            outputArm1.setPosition(0.358); // 0.15
                            outputArm2.setPosition(0.637); // 0.85
                        }),
                        tr7.build(),
                        new InstantAction(() ->{
                            lift1.setPosition(1);
                            lift2.setPosition(0);
                            pull1.setPower(1);
                            pull2.setPower(1);
                        }),
                        tr8.build(),
                        new InstantAction(() -> {
                            maxWheelVel = 200;
                            lift1.setPosition(0.8);
                            lift2.setPosition(0.2);
                        }),
                        tr9.build(),
                        new InstantAction(() -> {
                            claw.setPosition(0.6);
                            pull1.setPower(0);
                            pull2.setPower(0);
                        }),
                        new SleepAction(0.4),
                        new InstantAction(() ->{
                            lift1.setPosition(1);
                            lift2.setPosition(0);
                            outputArm1.setPosition(0.7); // 0.85
                            outputArm2.setPosition(0.3);
                        }),
                        new SleepAction(0.6),
                        tr10.build(),
                        new InstantAction(() ->{
                            claw.setPosition(0.4);
//                            outputArm1.setPosition(0.358); // 0.15
//                            outputArm2.setPosition(0.637); // 0.85
                            maxWheelVel = 200;
                        }),
                        tr11.build(),
                        new SleepAction(2)
                )
        );
    }

}
