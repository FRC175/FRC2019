package com.team175.robot.util.tuning;

public class ClosedLoopTuner {

    private final ClosedLoopMotorController mMotorController;
    private final ClosedLoopGains mGains;

    public ClosedLoopTuner(ClosedLoopMotorController motorController, ClosedLoopGains gains) {
        mMotorController = motorController;
        mGains = gains;
    }

    public void setWantedPosition() {

    }

    public void outputToDashboard() {

    }

    public void updateFromDashboard() {

    }

}
