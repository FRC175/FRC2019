package com.team175.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.LEDChannel;
import com.team175.robot.Constants;
import com.team175.robot.loops.Loop;
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
    private int[] mColorArr;
    private boolean mIsOff;

    private static final int BLINK_TIME = 1; // s

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
        mColorArr = new int[3];
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
        }
    }

    public synchronized void moodLampCycle(boolean enable) {
        mWantedState = enable ? LEDState.MOOD_LAMP : LEDState.OFF;
    }

    private void moodLampCycle() {
        // Start at red
        mColorArr[0] = 255;
        mColorArr[1] = 0;
        mColorArr[2] = 0;

        for (int decColor = 0; decColor < 3; decColor++) {
            int incColor = decColor == 2 ? 0 : decColor + 1;

            for (int i = 0; i < 255; i++) {
                mColorArr[decColor]--;
                mColorArr[incColor]++;

                setColor(new Color(mColorArr[0], mColorArr[1], mColorArr[2]));
                Timer.delay(0.01);
            }
        }
    }

    @Override
    public void start() {
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
                    break;
            }
            outputToDashboard();
        }
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
