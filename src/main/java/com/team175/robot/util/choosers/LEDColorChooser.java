package com.team175.robot.util.choosers;

public class LEDColorChooser implements Chooser {

    private static LEDColorChooser sInstance;

    public static LEDColorChooser getInstance() {
        if (sInstance == null) {
            sInstance = new LEDColorChooser();
        }

        return sInstance;
    }

    private LEDColorChooser() {
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void outputToDashboard() {

    }

    @Override
    public void updateFromDashboard() {

    }

}
