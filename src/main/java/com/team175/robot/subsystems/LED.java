package com.team175.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.LEDChannel;
import com.team175.robot.Constants;
import com.team175.robot.positions.LEDColor;
import com.team175.robot.util.CTREDiagnostics;

import java.awt.Color;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public class LED extends AldrinSubsystem {

    private final CANifier mController;

    private static LED sInstance;

    /*private enum LEDState {
        SET_COLOR,
        BLINK_COLOR,
        MOOD_LAMP,
        BREATH_COLOR
    }*/

    public static LED getInstance() {
        if (sInstance == null) {
            sInstance = new LED();
        }

        return sInstance;
    }

    private LED() {
        mController = new CANifier(Constants.CANIFIER_PORT);
        CTREDiagnostics.checkCommand(mController.configFactoryDefault(Constants.TIMEOUT_MS),
                "Failed to config CANifier to factory defaults.");
    }

    public void setColor(Color color) {
        mController.setLEDOutput((double) color.getRed() / 255, LEDChannel.LEDChannelA);
        mController.setLEDOutput((double) color.getGreen() / 255, LEDChannel.LEDChannelB);
        mController.setLEDOutput((double) color.getBlue() / 255, LEDChannel.LEDChannelC);
    }

    public void setColor(LEDColor color) {
        setColor(color.getColor());
    }

    public void blinkColor(Color color, int timeDur) {
    }

    public void blinkColor(LEDColor color) {
        blinkColor(color.getColor(), Constants.BLINK_TIME);
    }

    public void moodLampCycle() {
        // TODO: Perhaps use notifier and ILoopable for just mood cycle or whole subsystem since you don't want LED routine to interfere with robot code
        // TODO: Perhaps use sine wave to make light smooth
    }


    public void breathColor(Color color) {
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

    /*@Override
    public void onPeriodic() {
    }*/

}
