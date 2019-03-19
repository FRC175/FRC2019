package com.team175.robot.commands.auto;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.util.AutoModeChooser;

/**
 * @author Arvind
 */
public class CancelAuto extends AldrinCommand {

    public CancelAuto() {
        requires(Drive.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        AutoModeChooser.getInstance().stop();
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
        Drive.getInstance().setHighGear(false);
        Drive.getInstance().setBrakeMode(false);
        super.end();
    }

}
