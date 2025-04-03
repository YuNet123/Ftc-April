package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);



        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();


//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d((t*-0.5-2.75), (t*2.5 + 2.75)  , Math.toRadians(270)))
//                .waitSeconds(1)
//                .splineToLinearHeading(new Pose2d(1,rungY-4,Math.toRadians(180),Math.toRadians(0)))
//                        .build());


                myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-39, -63, Math.PI/2))
                        .strafeToLinearHeading(new Vector2d(-55,-55), Math.PI/2) // Mișcarea spre coș
                        .turn(-Math.PI/4) // Întoarcerea cu spatele spre coș
                        .strafeToLinearHeading(new Vector2d(-46,-35), Math.PI/2) // Mișcarea la primul sample
                        .setTangent(Math.PI/2)  // Setarea direcției de mers
                        .lineToY(-33.5)   // Mișcarea înainte pentru a lua sample-ul
                        .strafeToLinearHeading(new Vector2d(-55,-55), Math.PI/4) // Mișcarea spre coș
                        .strafeToLinearHeading(new Vector2d(-56,-35), Math.PI/2) // Mișcarea la al doilea sample
                        .setTangent(Math.PI/2) // Setarea direcției de mers
                        .lineToY(-32.5)  // Mișcarea înainte pentru a lua sample-ul
                        .strafeToLinearHeading(new Vector2d(-55,-55), Math.PI/4) // Mișcarea spre coș
                        .strafeToLinearHeading(new Vector2d(-34,-11), Math.PI) // Începutul mișcării spre locul de parcare
                        .setTangent(Math.PI) // Setarea direcției de mers
                        .lineToX(-20)  // Mișcarea spre locul de parcare
                .build());

//                        .lineToY(-30)
//                        .lineToY(-40)
//                        .splineTo(new Vector2d(44,-13), Math.PI/2)
//                        .lineToY(-12)
//                        .setTangent(3*Math.PI/2)
//                        .strafeTo(new Vector2d(58, -12))
//                        .setTangent(Math.PI/2)
//                        .lineToYConstantHeading(-55)
//                        .lineToYConstantHeading(-42)
//                        .turn(Math.PI)
//                        .lineToYConstantHeading(-58)
//
//                        .strafeTo(new Vector2d(-5,-40))
//                        .setTangent(Math.PI/2)
//                        .lineToYConstantHeading(-32)
//                        .lineToYConstantHeading(-45)
//                        .build());
//                        .strafeTo(new Vector2d(48,-40))
//                        .setTangent(Math.PI/2)
//                        .lineToYConstantHeading(-58)
//                        .strafeTo(new Vector2d(0,-40))
//                        .setTangent(Math.PI/2)
//                        .lineToYConstantHeading(-32)
//                        .lineToYConstantHeading(-44)
//                        .strafeTo(new Vector2d(48,-60))
//                                .build());
//
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, -63, 3*Math.PI/2))
//                        .lineToY(-30)
//                .strafeTo(new Vector2d(0,-30))
//                 put sample
//                                .strafeTo(new Vector2d(0,-35))
//                                .turn(Math.toRadians(60))
//                                .strafeTo(new Vector2d(47, -60))
//                                .turn(Math.toRadians(-60))
//                                .strafeTo(new Vector2d(-5,-30))
//                                .strafeTo(new Vector2d(-5,-35))
//                 turn
//                .build());
//
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, -63, 3*Math.PI/2))
//                  .lineToY(-30)
//                  .lineToY(-40)
//                  .splineTo(new Vector2d(35,-30), Math.PI/2)
//                  .lineToY(-12)
//                  .setTangent(0)
//                  .splineToConstantHeading(new Vector2d(48, -12), Math.PI / 2)
//                  .lineToYConstantHeading(-50)
//                  .lineToYConstantHeading(-13)
//                  .setTangent(0)
//                  .lineToX(57)
//                  .turn(Math.toRadians(0.01))
//                  .lineToY(-50)
//                .lineToYConstantHeading(-13)
//                .strafeTo(new Vector2d(62, -13))
//                .setTangent(3*Math.PI/2)
//                .lineToY(-50)
//
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(4, -63, 3*Math.PI/2))
//                .lineToY(-30)
//                .lineToY(-40)
//                .splineTo(new Vector2d(39,-13), Math.PI/2)
//                .lineToY(-12)
//                .setTangent(3*Math.PI/2)
//                .strafeTo(new Vector2d(46, -12))
//                .setTangent(Math.PI/2)
//                .lineToYConstantHeading(-55)
//                 here
//                .lineToYConstantHeading(-42)
//                .turn(Math.PI)
//
//
//
//                .lineToYConstantHeading(-58)
//                .strafeTo(new Vector2d(-5,-40))
//                .setTangent(Math.PI/2)
//                .lineToYConstantHeading(-32)
//                .lineToYConstantHeading(-44)
//                .strafeTo(new Vector2d(48,-40))
//                .setTangent(Math.PI/2)
//                .lineToYConstantHeading(-58)
//                .strafeTo(new Vector2d(0,-40))
//                .setTangent(Math.PI/2)
//                .lineToYConstantHeading(-32)
//                .lineToYConstantHeading(-44)
//                .strafeTo(new Vector2d(48,-60))
//                .build());


        myBot.setDimensions(16,16);

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}