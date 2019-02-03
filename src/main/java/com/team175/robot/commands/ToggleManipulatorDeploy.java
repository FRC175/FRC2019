package com.team175.robot.commands;

import com.team175.robot.subsystems.Manipulator;

public class ToggleManipulatorDeploy extends AldrinInstantCommand {

    private boolean mDeploy;

    public ToggleManipulatorDeploy() {
        requires(Manipulator.getInstance());

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Manipulator.getInstance().deploy(mDeploy);
        mDeploy = !mDeploy;

        super.logInit();
    }

}
