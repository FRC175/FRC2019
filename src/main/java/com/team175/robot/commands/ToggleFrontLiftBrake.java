package com.team175.robot.commands;

import com.team175.robot.subsystems.Lift;

public class ToggleFrontLiftBrake extends AldrinToggleCommand {

    private boolean mBrake;

    public ToggleFrontLiftBrake() {
        requires(Lift.getInstance());
        mBrake = true;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Lift.getInstance().setFrontBrake(mBrake);
        mBrake = !mBrake;

        super.logInit();
    }

}
