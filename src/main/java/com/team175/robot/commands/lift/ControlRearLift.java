package com.team175.robot.commands.lift;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.Timer;

public class ControlRearLift extends AldrinCommand {

    private LiftPosition mPosition;
    private boolean mIsManual;

    public ControlRearLift(LiftPosition position) {
        requires(Lift.getInstance(), Drive.getInstance());
        mPosition = position;
        mIsManual = false;
        super.logInstantiation();
    }

    public ControlRearLift() {
        requires(Lift.getInstance(), Drive.getInstance());
        mPosition = null;
        mIsManual = true;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().stop();
        /*if (!mIsManual) {
            if (mPosition == LiftPosition.RETRACT) {
                // Bring up and then disengage brake
                Lift.getInstance().setRearPosition(LiftPosition.EXTEND);
                Timer.delay(0.175);
                Lift.getInstance().setRearBrake(false);
                Lift.getInstance().setRearPower(0);
            }
        }*/
        Lift.getInstance().setRearBrake(false);
        /*if (!mIsManual) {
            if (mPosition == LiftPosition.EXTEND) {
                Drive.getInstance().setHighGear(true);
                Drive.getInstance().setPower(1);
            } else {
                Drive.getInstance().stop();
            }
        }*/

        if (!mIsManual) {
            Lift.getInstance().setRearPosition(mPosition);
        }

        super.initialize();
    }

    @Override
    protected void execute() {
        if (mIsManual) {
            Lift.getInstance().setRearPower(OI.getInstance().getDriverStickY());
        }

        if (Lift.getInstance().getRearPosition() < -550) {
            Drive.getInstance().setHighGear(true);
            Drive.getInstance().setPower(0.7);
            Lift.getInstance().setDrivePower(1);
        } else {
            Drive.getInstance().setHighGear(false);
            Drive.getInstance().stop();
            Lift.getInstance().setDrivePower(0);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().setRearPower(0);
        Lift.getInstance().setRearBrake(true);
        super.end();
    }

}
