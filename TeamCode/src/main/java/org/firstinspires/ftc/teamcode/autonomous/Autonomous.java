    package org.firstinspires.ftc.teamcode.autonomous;

    import static com.arcrobotics.ftclib.kotlin.extensions.drivebase.RobotDriveExtKt.setMaxSpeed;

    import com.acmerobotics.roadrunner.Action;
    import com.acmerobotics.roadrunner.InstantAction;
    import com.acmerobotics.roadrunner.Pose2d;
    import com.acmerobotics.roadrunner.PoseVelocity2d;
    import com.acmerobotics.roadrunner.SequentialAction;
    import com.acmerobotics.roadrunner.SleepAction;
    import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
    import com.acmerobotics.roadrunner.Vector2d;
    import com.acmerobotics.roadrunner.ftc.Actions;
    import com.arcrobotics.ftclib.gamepad.GamepadEx;
    import com.arcrobotics.ftclib.gamepad.GamepadKeys;
    import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
    import com.arcrobotics.ftclib.hardware.motors.Motor;
    import com.qualcomm.robotcore.eventloop.opmode.OpMode;
    import com.qualcomm.robotcore.hardware.CRServo;
    import com.qualcomm.robotcore.hardware.Servo;

    import org.firstinspires.ftc.teamcode.MecanumDrive;

    @com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous")
    public class Autonomous extends OpMode {
        MecanumDrive drive;

        TrajectoryActionBuilder trajectory1, trajectory2;

        public CRServo pull1, pull2;
        private Servo lift1, lift2, arm1, arm2, ext1,claw;
        private Servo ext2;
        Motor fl, fr, bl, br, sl, sr;
        private Servo outputArm1;
        private Servo outputArm2;

        @Override
        public void init() {

            // Initialize motors
            // Expansion hub
            // port 2, 0, 1, 3
            // fr = frontRight, fl = frontLeft, bl = backLeft, br = backRight
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

            // Control hub
            // port 2,3
            sl = new Motor(hardwareMap, "motor1");
            sl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

            sr = new Motor(hardwareMap, "motor2");
            sr.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
            sl.setInverted(true);


            // Initialize servos and CR servos
            // Expansion hub
            // port 1,4
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

//            pullToggled = false;

            ext1.setPosition(0.35); // 0.47
            ext2.setPosition(0.35); // 0.53

            pull1.setPower(0);
            pull2.setPower(0);
            arm1.setPosition(0.2);
            arm2.setPosition(0.2);
//            claw.setPosition(0.4);

//            outputArm1.setPosition(0.5); // 0.15
//            outputArm2.setPosition(0.5); // 0.85

//            val adjustedVelocity = setMaxSpeed(poseVelocity, maxSpeed);

            drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, Math.PI));
//                    .setDrivePowers(poseVelocity);
            trajectory1 = drive.actionBuilder(new Pose2d(0, 0, 0))
                    .splineTo(new Vector2d(5, 5), 0);
//                    .splineTo(new Vector2d(33, 20), 0);

            trajectory2 = trajectory1.endTrajectory().fresh()
                    .splineTo(new Vector2d(10, -35), 0);
        }
    
        @Override
        public void start() {
            Actions.runBlocking(
                    new SequentialAction(
                            trajectory1.build(),
//                            new InstantAction(() -> {
//                                outputArm1.setPosition(0.85);
//                                outputArm2.setPosition(0.15);
//                            }),
                            new SleepAction(1)
//                            trajectory2.build()
                    )
            );
        }

        @Override
        public void loop() {

        }
    }
