package com.team175.robot.commands.drive;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;

/**
 * Shifts drive to high gear.
 *
 * @author Arvind
 */
public class ShiftDriveGear extends AldrinCommand {

    private boolean mIsHighGear;

    public ShiftDriveGear(boolean isHighGear) {
        requires(Drive.getInstance());
        mIsHighGear = isHighGear;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setHighGear(mIsHighGear);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
        super.end();
    }

}
