package com.team175.robot.commands.drive;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;

public class ResetGyro extends AldrinCommand {

    public ResetGyro() {
        requires(Drive.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().resetGyro();
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
