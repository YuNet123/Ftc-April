package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.MecanumDrive.Params.maxWheelVel;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "AutoBasket", group = "Autonomous")
public class AutoBasket2 extends LinearOpMode {
    public CRServo pull1, pull2;
    private Servo lift1, lift2, claw, ext1, ext2, outputArm1, outputArm2;
    private Motor sl, sr;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(-39, -63, Math.PI/2);
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

        ext1.setPosition(0.35);
        ext2.setPosition(0.35);

        pull1.setPower(0);
        pull2.setPower(0);

        outputArm1.setPosition(0.358);
        outputArm2.setPosition(0.637);

        lift1.setPosition(0.6);
        lift2.setPosition(0.4);

        claw.setPosition(0.4);

        outputArm1.setPosition(0.358); // 0.15
        outputArm2.setPosition(0.637); // 0.85

        // Control hub
        // port 2,3
        sl = new Motor(hardwareMap, "motor1");
        sl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        sr = new Motor(hardwareMap, "motor2");
        sr.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        sl.setInverted(true);

        TrajectoryActionBuilder tr1 = drive.actionBuilder(beginPose) // auto start
                .strafeToLinearHeading(new Vector2d(-55,-55), Math.PI/2)
                .turn(-Math.PI/4);

        TrajectoryActionBuilder tr2 = tr1.endTrajectory().fresh() // move to next sample
                .strafeToLinearHeading(new Vector2d(-46,-35), Math.PI/2);

        TrajectoryActionBuilder tr3 = tr2.endTrajectory().fresh() // pull in sample
                .setTangent(Math.PI/2)
                .lineToY(-33.5);

        TrajectoryActionBuilder tr4 = tr3.endTrajectory().fresh() // back to base
                .strafeToLinearHeading(new Vector2d(-55,-55), Math.PI/4);

        TrajectoryActionBuilder tr5 = tr4.endTrajectory().fresh() // to the next sample
                .strafeToLinearHeading(new Vector2d(-56,-35), Math.PI/2);

        TrajectoryActionBuilder tr6 = tr5.endTrajectory().fresh() // pull in sample
                .setTangent(Math.PI/2)
                .lineToY(-32.5);

        TrajectoryActionBuilder tr7 = tr6.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-55,-55), Math.PI/4); // back to base

        TrajectoryActionBuilder tr8 = tr7.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-34,-11), Math.PI)
                .setTangent(Math.PI)
                .lineToX(-20);

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        new InstantAction( () -> { // start pull in
                            pull1.setPower(1);     // to push sample
                            pull2.setPower(1);     // into the claw
                        }),
                        new SleepAction(0.5),  // wait for claw to catch the sample
                        new InstantAction( () -> { // grab sample stop pull in
                            claw.setPosition(0.6);
                            pull1.setPower(0);
                            pull2.setPower(0);
                        }),
                        tr1.build(), // move to the basket
                        new InstantAction( () -> { // start the elevator
                            sl.set(0.8);          // lo lift the sample
                            sr.set(0.8);
                        }),
                        new SleepAction(1.2),
                        new InstantAction( () -> {
                            outputArm1.setPosition(0.7);
                            outputArm2.setPosition(0.3);
                        }),
                        new SleepAction(0.6),
                        new InstantAction( () -> {
                            claw.setPosition(0.4);
                        }),
                        new SleepAction(0.5),
                        new InstantAction( () -> {
                            outputArm1.setPosition(0.358); // 0.15
                            outputArm2.setPosition(0.637);
                        }),
                        new SleepAction(0.4),
                        new InstantAction( () -> {
                            sl.set(-.2);
                            sr.set(-.2);
                        }),
                        new SleepAction(0.4),
                        new InstantAction( () -> {
                            sl.set(0);
                            sr.set(0);
                            pull1.setPower(1);
                            pull2.setPower(1);
                            lift1.setPosition(1);
                            lift2.setPosition(0);
                        }),
                        tr2.build(),
                        tr3.build(),
                        new InstantAction( () -> {
                            pull1.setPower(0);
                            pull2.setPower(0);
                            lift1.setPosition(0.6);
                            lift2.setPosition(0.4);
                        }),
                        tr4.build(),
                        new InstantAction( () -> {
                            pull1.setPower(1);
                            pull2.setPower(1);
                        }),
                        new SleepAction(0.5),
                        new InstantAction(() ->{
                            claw.setPosition(0.6);
                            pull1.setPower(0);
                            pull2.setPower(0);
                        }),
                        new SleepAction(0.5),
                        new InstantAction( () -> {
                            sl.set(0.8);
                            sr.set(0.8);
                        }),
                        new SleepAction(1.2),
                        new InstantAction( () -> {
                            outputArm1.setPosition(0.7);
                            outputArm2.setPosition(0.3);
                        }),
                        new SleepAction(0.6),
                        new InstantAction( () -> {
                            claw.setPosition(0.4);
                        }),
                        new SleepAction(0.5),
                        new InstantAction( () -> {
                            outputArm1.setPosition(0.358); // 0.15
                            outputArm2.setPosition(0.637);
                        }),
                        new SleepAction(0.4),
                        new InstantAction( () -> {
                            sl.set(-.2);
                            sr.set(-.2);
                        }),
                        new SleepAction(0.4),
                        new InstantAction( () -> {
                            sl.set(0);
                            sr.set(0);
                            pull1.setPower(1);
                            pull2.setPower(1);
                            lift1.setPosition(1);
                            lift2.setPosition(0);
                        }), // next sample
                        tr5.build(),
                        tr6.build(),
                        new InstantAction( () -> {
                            pull1.setPower(0);
                            pull2.setPower(0);
                            lift1.setPosition(0.6);
                            lift2.setPosition(0.4);
                        }),
                        tr7.build(),
                        new InstantAction( () -> {
                            pull1.setPower(1);
                            pull2.setPower(1);
                        }),
                        new SleepAction(0.5),
                        new InstantAction(() ->{
                            claw.setPosition(0.6);
                            pull1.setPower(0);
                            pull2.setPower(0);
                        }),
                        new SleepAction(0.5),
                        new InstantAction( () -> {
                            sl.set(0.8);
                            sr.set(0.8);
                        }),
                        new SleepAction(1.2),
                        new InstantAction( () -> {
                            outputArm1.setPosition(0.7);
                            outputArm2.setPosition(0.3);
                        }),
                        new SleepAction(0.6),
                        new InstantAction( () -> {
                            claw.setPosition(0.4);
                        }),
                        new SleepAction(0.5),
                        new InstantAction( () -> {
                            outputArm1.setPosition(0.358); // 0.15
                            outputArm2.setPosition(0.637);
                        }),
                        new SleepAction(0.4),
                        new InstantAction( () -> {
                            sl.set(-.2);
                            sr.set(-.2);
                        }),
                        new SleepAction(0.4),
                        new InstantAction( () -> {
                            sl.set(0);
                            sr.set(0);
                            pull1.setPower(1);
                            pull2.setPower(1);
                            lift1.setPosition(1);
                            lift2.setPosition(0);
                        }),
                        new SleepAction(1.5),
                        tr8.build()
                )
        );
    }

}
