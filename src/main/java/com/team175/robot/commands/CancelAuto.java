package com.team175.robot.commands;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.util.choosers.AutoModeChooser;

/**
 * @author Arvind
 */
public class CancelAuto extends AldrinCommand {

    public CancelAuto() {
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

}
