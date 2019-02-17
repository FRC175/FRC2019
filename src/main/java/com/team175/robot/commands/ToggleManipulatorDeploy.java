package com.team175.robot.commands;

import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Manipulator;

public class ToggleManipulatorDeploy extends AldrinToggleCommand {

    private boolean mDeploy;

    public ToggleManipulatorDeploy() {
        requires(Manipulator.getInstance());
        mDeploy = true;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Manipulator.getInstance().setArmPosition(ManipulatorArmPosition.STOW);
        Manipulator.getInstance().deploy(mDeploy);
        mDeploy = !mDeploy;

        super.logInit();
    }

}
