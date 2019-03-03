package com.team175.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.LEDChannel;
import com.team175.robot.Constants;
import com.team175.robot.positions.LEDColor;

import java.awt.Color;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public class LED extends AldrinSubsystem {

    private CANifier mController;

    private static LED sInstance;

    public static LED getInstance() {
        if (sInstance == null) {
            sInstance = new LED();
        }

        return sInstance;
    }

    private LED() {
        mController = new CANifier(Constants.CANIFIER_PORT);
        mController.configFactoryDefault(Constants.TIMEOUT_MS);
    }

    public void blinkColor(Color color, int timeDur) {

    }

    public void setColor(Color color) {
        mController.setLEDOutput((double) color.getRed() / 255, LEDChannel.LEDChannelA);
        mController.setLEDOutput((double) color.getGreen() / 255, LEDChannel.LEDChannelB);
        mController.setLEDOutput((double) color.getBlue() / 255, LEDChannel.LEDChannelC);
    }

    public void setColor(LEDColor color) {
        setColor(color.getColor());
    }

    public void breathColor(Color color) {

    }

    public void moodLampCycle() {

    }

    @Override
    public void stop() {
        // Turns off LEDs
        setColor(LEDColor.OFF);
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        return null;
    }

    @Override
    public void updateFromDashboard() {

    }

}
