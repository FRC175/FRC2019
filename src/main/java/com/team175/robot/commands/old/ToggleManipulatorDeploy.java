package com.team175.robot.commands.old;

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
        // Manipulator.getInstance().setArmPosition(ManipulatorArmPosition.STOW);
        Manipulator.getInstance().deploy(mDeploy);
        Manipulator.getInstance().stopArm();
        mDeploy = !mDeploy;
        super.logInit();
    }

}
