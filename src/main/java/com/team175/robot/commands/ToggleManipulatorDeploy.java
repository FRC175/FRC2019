package com.team175.robot.commands;

import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ToggleManipulatorDeploy extends AldrinToggleCommand {

    private boolean mDeploy;

    public ToggleManipulatorDeploy() {
        requires(Manipulator.getInstance());

        mDeploy = false;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        // Manipulator.getInstance().setArmPosition(ManipulatorArmPosition.HOME);
        Manipulator.getInstance().deploy(mDeploy);
        Manipulator.getInstance().stopArm();
        mDeploy = !mDeploy;

        super.logInit();
    }

}
