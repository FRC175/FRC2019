package com.team175.robot.commands;

import com.team175.robot.subsystems.Manipulator;

public class TogglePunchHatch extends AldrinToggleCommand {

    private boolean mPush;

    public TogglePunchHatch() {
        requires(Manipulator.getInstance());

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Manipulator.getInstance().punchHatch(mPush);
        mPush = !mPush;

        super.logInit();
    }

}