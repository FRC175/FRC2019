package com.team175.robot.commands.manipulator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ManipulatorMode;
import com.team175.robot.subsystems.Manipulator;

public class SwitchManipulatorMode extends AldrinCommand {

    private ManipulatorMode mMode;

    public SwitchManipulatorMode(ManipulatorMode mode) {
        mMode = mode;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        mLogger.info("Setting manipulator to {} mode.", mMode.toString());
        Manipulator.getInstance().setMode(mMode);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
