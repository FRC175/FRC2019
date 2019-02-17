package com.team175.robot.commands;

import com.team175.robot.subsystems.Lift;

public class ToggleRearLiftBrake extends AldrinToggleCommand {

    private boolean mBrake;

    public ToggleRearLiftBrake() {
        requires(Lift.getInstance());
        mBrake = true;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Lift.getInstance().setRearBrake(mBrake);
        mBrake = !mBrake;

        super.logInit();
    }

}
