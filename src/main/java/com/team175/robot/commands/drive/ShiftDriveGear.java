package com.team175.robot.commands.drive;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;

/**
 * Shifts drive to high gear.
 *
 * @author Arvind
 */
public class ShiftDriveGear extends AldrinCommand {

    public ShiftDriveGear() {
        requires(Drive.getInstance());
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setHighGear(true);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Drive.getInstance().setHighGear(false);
        super.end();
    }

}
