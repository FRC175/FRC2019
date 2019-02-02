package com.team175.robot.commands;

import com.team175.robot.subsystems.Manipulator;

public class TogglePushHatch extends AldrinInstantCommand {

    private boolean mPush;

    public TogglePushHatch() {
        requires(Manipulator.getInstance());

        super.instantiationLog();
    }

    @Override
    protected void initialize() {
        Manipulator.getInstance().pushHatch(mPush);
        mPush = !mPush;

        super.initLog();
    }

}