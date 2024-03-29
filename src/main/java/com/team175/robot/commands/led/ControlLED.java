package com.team175.robot.commands.led;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.LED;

public class ControlLED extends AldrinCommand {

    public ControlLED() {
        requires(LED.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        LED.getInstance().updateFromDashboard();
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
