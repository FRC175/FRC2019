package com.team175.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.LEDChannel;
import com.team175.robot.Constants;
import com.team175.robot.positions.LEDColor;
import com.team175.robot.util.CTREDiagnostics;
import edu.wpi.first.wpilibj.Timer;
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
    private double mStartTime, mBlinkDur;
    private boolean mIsOff;

    private static final int BLINK_TIME = 2; // s

    private static LED sInstance;

    private enum LEDState {
        BLINK_COLOR,
        MOOD_LAMP,
        NORMAL,
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
        mStartTime = mBlinkDur = 0;
        mIsOff = false;
        setColor(mWantedColor);

        super.logInstantiation();
    }

    public synchronized void setColor(Color color) {
        mWantedColor = color;
        mController.setLEDOutput((double) mWantedColor.getRed() / 255, LEDChannel.LEDChannelA);
        mController.setLEDOutput((double) mWantedColor.getGreen() / 255, LEDChannel.LEDChannelB);
        mController.setLEDOutput((double) mWantedColor.getBlue() / 255, LEDChannel.LEDChannelC);
    }

    public synchronized void setColor(LEDColor color) {
        // mWantedState = LEDState.NORMAL;
        setColor(color.getColor());
    }

    public synchronized void blinkColor(Color color, int timeDur) {
        mWantedColor = color;
        mBlinkDur = timeDur;
        mStartTime = Timer.getFPGATimestamp();
        mWantedState = LEDState.BLINK_COLOR;
    }

    public synchronized void blinkColor(LEDColor color) {
        blinkColor(color.getColor(), BLINK_TIME);
    }

    private void blinkColor() {
        if (Timer.getFPGATimestamp() - mStartTime <= mBlinkDur) {
            if (mIsOff) {
                setColor(LEDColor.OFF);
            } else {
                setColor(mWantedColor);
            }
            mIsOff = !mIsOff;
            Timer.delay(0.2);
        } else {
            mWantedState = LEDState.NORMAL;
            // setColor(LEDColor.DEFAULT);
        }
    }

    public synchronized void moodLampCycle(boolean enable) {
        mWantedState = enable ? LEDState.MOOD_LAMP : LEDState.NORMAL;
        mIsOff = false;
    }

    private void moodLampCycle() {
        // Start at red
        int[] colorElements = { 255, 0, 0 };

        // Cycle through rainbow
        // Credit to some tutorial for the Arduino
        for (int decColor = 0; decColor < 3; decColor++) {
            int incColor = decColor == 2 ? 0 : decColor + 1;

            for (int i = 0; i < 255; i++) {
                colorElements[decColor]--;
                colorElements[incColor]++;
                setColor(new Color(colorElements[0], colorElements[1], colorElements[2]));

                Timer.delay(0.01);
            }
        }
    }

    @Override
    public void start() {
        mWantedState = LEDState.NORMAL;
        setColor(LEDColor.DEFAULT);
    }

    @Override
    public void loop() {
        synchronized (this) {
            switch (mWantedState) {
                case BLINK_COLOR:
                    blinkColor();
                    break;
                case MOOD_LAMP:
                    moodLampCycle();
                    break;
                case OFF:
                    setColor(LEDColor.OFF);
                    break;
                case NORMAL:
                default:
                    setColor(LEDColor.DEFAULT);
                    break;
            }
            outputToDashboard();
        }
    }

    @Override
    public void stop() {
        mWantedState = LEDState.OFF;
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
