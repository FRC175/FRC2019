package com.team175.robot.commands;

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

        super.logInit();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
