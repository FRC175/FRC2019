package com.team175.robot.commands;

import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class TogglePunchHatch extends AldrinToggleCommand {

    private boolean mPush;

    public TogglePunchHatch() {
        requires(Manipulator.getInstance());

        mPush = true;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Manipulator.getInstance().punchHatch(mPush);
        mPush = !mPush;

        super.logInit();
    }

}