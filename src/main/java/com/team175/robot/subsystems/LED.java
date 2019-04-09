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
public final class LED extends AldrinSubsystem {

    private final CANifier mController;

    private Color mWantedColor;
    private LEDState mWantedState;
    private double mStartTime, mBlinkDur, mCounter;

    private static final int BLINK_TIME = 2; // s

    private static LED sInstance;

    private enum LEDState {
        BLINK_COLOR,
        COLOR_CYCLE,
        MANUAL;
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

        mWantedColor = LEDColor.OFF.getColor();
        mWantedState = LEDState.MANUAL;
        mStartTime = Timer.getFPGATimestamp();
        mBlinkDur = BLINK_TIME;
        mCounter = 0;

        super.logInstantiation();
    }

    public synchronized void setStaticColor(Color color) {
        mWantedColor = color;
        mWantedState = LEDState.MANUAL;
    }

    public synchronized void setStaticColor(LEDColor color) {
        setStaticColor(color.getColor());
    }

    private void setColor(Color color) {
        mController.setLEDOutput((double) color.getRed() / 255, LEDChannel.LEDChannelA);
        mController.setLEDOutput((double) color.getGreen() / 255, LEDChannel.LEDChannelB);
        mController.setLEDOutput((double) color.getBlue() / 255, LEDChannel.LEDChannelC);
    }

    private void setColor(LEDColor color) {
        setColor(color.getColor());
    }

    /*private void setColor() {
        mController.setLEDOutput((double) mWantedColor.getRed() / 255, LEDChannel.LEDChannelA);
        mController.setLEDOutput((double) mWantedColor.getGreen() / 255, LEDChannel.LEDChannelB);
        mController.setLEDOutput((double) mWantedColor.getBlue() / 255, LEDChannel.LEDChannelC);
    }*/

    public synchronized void blinkColor(Color color, int timeDur) {
        mWantedColor = color;
        mBlinkDur = timeDur;
        mStartTime = Timer.getFPGATimestamp();
        mWantedState = LEDState.BLINK_COLOR;
    }

    public synchronized void blinkColor(LEDColor color, int timeDur) {
        blinkColor(color.getColor(), timeDur);
    }

    public synchronized void blinkColor(LEDColor color) {
        blinkColor(color, BLINK_TIME);
    }

    private void blinkColor() {
        if ((Timer.getFPGATimestamp() - mStartTime) <= mBlinkDur) {
            if (((Timer.getFPGATimestamp() - mStartTime) / mBlinkDur) % 2 == 0) {
                setColor(mWantedColor);
            } else {
                setColor(LEDColor.OFF);
            }
        } else {
            setStaticColor(LEDColor.DEFAULT);
        }
    }

    public synchronized void colorCycle(boolean enable) {
        mWantedState = enable ? LEDState.COLOR_CYCLE : LEDState.MANUAL;
        mCounter = 0;
    }

    private void colorCycle() {
        setColor(new Color(
                // 127.5 * sin(x) + 127.5
                (int) (127.5 * Math.sin(mCounter) + 127.5),
                // 127.5 * sin(x + (PI/2)) + 127.5
                (int) (127.5 * Math.sin(mCounter + (Math.PI / 2)) + 127.5),
                // 127.5 * sin(x + (3PI/2)) + 127.5
                (int) (127.5 * Math.sin(mCounter + ((3 * Math.PI) / 2)) + 127.5)
        ));
        mCounter = mCounter + (Math.PI / 120);

        // Cycle through rainbow
        // Credit to some tutorial for the Arduino

        // Start at red
        /*int[] colorElements = { 255, 0, 0 };

        for (int decColor = 0; decColor < 3; decColor++) {
            int incColor = decColor == 2 ? 0 : decColor + 1;

            for (int i = 0; i < 255; i++) {
                colorElements[decColor]--;
                colorElements[incColor]++;
                setColor(new Color(colorElements[0], colorElements[1], colorElements[2]));

                Timer.delay(0.01);
            }
        }*/

        /*incColor = decColor == 2 ? 0 : decColor + 1;
        if (decColor < 3) {
            if (i < 255) {
                i++;
                colorElements[decColor]--;
                colorElements[incColor]++;
                setColor(new Color(colorElements[0], colorElements[1], colorElements[2]));
            } else if (i == 255) {
                i = 0;
                decColor++;
            }
            incColor = decColor == 2 ? 0 : decColor + 1;
        } else if (decColor == 3) {
            decColor = 0;
        }*/
    }

    @Override
    public void start() {
        mWantedColor = LEDColor.DEFAULT.getColor();
        mWantedState = LEDState.MANUAL;
        setColor(mWantedColor);
    }

    @Override
    public void loop() {
        synchronized (this) {
            switch (mWantedState) {
                case BLINK_COLOR:
                    blinkColor();
                    break;
                case COLOR_CYCLE:
                    colorCycle();
                    break;
                case MANUAL:
                default:
                    setColor(mWantedColor);
                    break;
            }

            outputToDashboard();
        }
    }

    @Override
    public void stop() {
        mWantedColor = LEDColor.OFF.getColor();
        mWantedState = LEDState.MANUAL;
        setColor(mWantedColor);
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        Map<String, Supplier> m = new LinkedHashMap<>();
        m.put("LEDRed", mWantedColor::getRed);
        m.put("LEDGreen", mWantedColor::getGreen);
        m.put("LEDBlue", mWantedColor::getBlue);
        m.put("LEDState", () -> mWantedState);
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
