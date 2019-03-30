package com.team175.robot.commands.manipulator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.IntakeMode;

public class SwitchIntakeMode extends AldrinCommand {

    private IntakeMode mMode;

    public SwitchIntakeMode(IntakeMode mode) {
        mMode = mode;
    }

    @Override
    protected void initialize() {
        IntakeMode.setMode(mMode);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
