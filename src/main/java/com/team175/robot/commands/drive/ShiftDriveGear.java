package com.team175.robot.commands.drive;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;

/**
 * @author Arvind
 */
public class ShiftDriveGear extends AldrinCommand {

    public ShiftDriveGear() {
        requires(Drive.getInstance());
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setHighGear(!Drive.getInstance().isHighGear());
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
