package org.firstinspires.ftc.teamcode.manual;
import static android.os.SystemClock.sleep;
import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

//@TeleOp(name = "ColorTest")
public class ColorSensorTest extends OpMode {
    private ColorSensor colorSensor;
    private final float[] hsvValues = new float[3];

    @Override
    public void init() {
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }

    private void checkColorDetection() {
        Color.RGBToHSV(
                colorSensor.red() * 8,  // Scale factor for better detection
                colorSensor.green() * 8,
                colorSensor.blue() * 8,
                hsvValues
        );

        // Hue ranges might need calibration
        boolean isTargetColor = (hsvValues[0] >= 20 && hsvValues[0] <= 60) ||   // Yellow
                (hsvValues[0] >= 180 && hsvValues[0] <= 240) ||  // Blue
                (hsvValues[0] <= 10 || hsvValues[0] >= 350);     // Red

        if (isTargetColor && hsvValues[1] > 0.7 && hsvValues[2] > 0.7) {
            //sleep(1000);
            telemetry.addData("Detected", "color");
            telemetry.update();
        }
    }

    @Override
    public void loop() {
        checkColorDetection();
    }
}

