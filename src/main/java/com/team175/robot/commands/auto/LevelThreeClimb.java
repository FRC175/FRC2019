package com.team175.robot.commands.auto;

/*import com.team175.robot.commands.old.DriveLiftToFront;
import com.team175.robot.commands.old.DriveLiftToRear;
import com.team175.robot.commands.old.PositionLift;
import com.team175.robot.positions.LiftPosition;*/
import com.team175.robot.commands.lift.DriveLiftToFront;
import com.team175.robot.commands.lift.DriveLiftToRear;
import com.team175.robot.commands.lift.FrontLiftToPosition;
import com.team175.robot.commands.lift.RearLiftToPosition;
import com.team175.robot.positions.LiftPosition;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author Arvind
 */
public class LevelThreeClimb extends CommandGroup {

    public LevelThreeClimb() {
        /*addParallel(new PositionLift(LiftPosition.EXTEND, LiftPosition.EXTEND));
        // addSequential(new PositionRearLift(LiftPosition.EXTEND)); // Could be addParallel()
        addSequential(new DriveLiftToFront());
        addSequential(new PositionLift(LiftPosition.RETRACT, LiftPosition.IDLE));
        addSequential(new DriveLiftToRear());
        addSequential(new PositionLift(LiftPosition.IDLE, LiftPosition.RETRACT));*/

        /*addParallel(new FrontLiftToPosition(LiftPosition.EXTEND));
        addSequential(new RearLiftToPosition(LiftPosition.EXTEND));
        addSequential(new DriveLiftToFront());
        addSequential(new FrontLiftToPosition(LiftPosition.RETRACT));
        addSequential(new DriveLiftToRear());
        addSequential(new RearLiftToPosition(LiftPosition.RETRACT));*/
    }

}
