package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Elevator;

/**
 * @author Arvind
 */
public class ManualElevator extends AldrinCommand {

    public ManualElevator() {
        requires(Elevator.getInstance());

        super.instantiationLog();
    }

    @Override
    protected void initialize() {
        super.initLog();
    }

    @Override
    protected void execute() {
        Elevator.getInstance().setPower(OI.getInstance().getOperatorStickY());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Elevator.getInstance().setPower(0);

        super.endLog();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
