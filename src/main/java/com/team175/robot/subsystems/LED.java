package com.team175.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.LEDChannel;
import com.team175.robot.Constants;
import com.team175.robot.positions.LEDColor;
import com.team175.robot.util.CTREDiagnostics;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public class LED extends AldrinSubsystem {

    private final CANifier mController;

    private Color mWantedColor;
    private LEDState mWantedState;

    private static final int BLINK_TIME = 0;

    private static LED sInstance;

    private enum LEDState {
        SET_COLOR,
        BLINK_COLOR,
        MOOD_LAMP,
        BREATH_COLOR,
        OFF;
    }

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

        mWantedColor = new Color(0, 0, 0);
        mWantedState = LEDState.OFF;
        setColor(mWantedColor);
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
        blinkColor(color.getColor(), BLINK_TIME);
    }

    public void moodLampCycle() {
        // TODO: Perhaps use notifier and loop for just mood cycle or whole subsystem since you don't want LED routine to interfere with robot code
        // TODO: Perhaps use sine wave to make light smooth
    }

    public void breathColor(Color color) {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        switch (mWantedState) {
            case BLINK_COLOR:
                break;
            case MOOD_LAMP:
                break;
            case OFF:
                setColor(LEDColor.OFF);
                break;
            default:
                break;
        }
        outputToDashboard();
    }

    @Override
    public void stop() {
        // Turns off LEDs
        setColor(LEDColor.OFF);
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        Map<String, Supplier> m = new LinkedHashMap<>();
        m.put("LEDRed", mWantedColor::getRed);
        m.put("LEDGreen", mWantedColor::getGreen);
        m.put("LEDBlue", mWantedColor::getBlue);
        return m;
    }

    @Override
    public void updateFromDashboard() {
        setColor(new Color(
                (int) SmartDashboard.getNumber("LEDRed", 0),
                (int) SmartDashboard.getNumber("LEDGreen", 0),
                (int) SmartDashboard.getNumber("LEDBlue", 0)
        ));
    }

}
