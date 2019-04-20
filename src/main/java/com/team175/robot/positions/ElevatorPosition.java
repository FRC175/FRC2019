package com.team175.robot.positions;

import com.team175.robot.subsystems.Manipulator;
import com.team175.robot.util.RobotManager;

/**
 * @author Arvind
 */
public enum ElevatorPosition {

    CARGO_LEVEL_THREE(23000, 24000),
    FINGER_HATCH_LEVEL_THREE(23000, 24000),
    VELCRO_HATCH_LEVEL_THREE(22859, 24000),
    CARGO_LEVEL_TWO(14520, 14520),
    FINGER_HATCH_LEVEL_TWO(15422, 16344),
    VELCRO_HATCH_LEVEL_TWO(11647, 15000),
    CARGO_LEVEL_ONE(4007, 4007),
    FINGER_HATCH_LEVEL_ONE(4400, 5371),
    VELCRO_HATCH_LEVEL_ONE(281, 4500),
    CARGO_GROUND_PICKUP(666, 680),
    CARGO_LOADING(10000, 10000),
    GROUND_PICKUP_ABOVE(1918, 1211),
    GROUND_PICKUP(415, 1199);

    private final int mCompetitionPosition;
    private final int mPracticePosition;

    ElevatorPosition(int competitionPosition, int practicePosition) {
        mCompetitionPosition = competitionPosition;
        mPracticePosition = practicePosition;
    }

    public int getPosition() {
        return RobotManager.isCompetitionRobot() ? mCompetitionPosition : mPracticePosition;
    }

    public static ElevatorPosition getPositionOne() {
        switch (Manipulator.getInstance().getMode()) {
            case FINGER_HATCH:
                return FINGER_HATCH_LEVEL_ONE;
            case CARGO:
                return CARGO_LEVEL_ONE;
            case VELCRO_HATCH:
            default:
                return VELCRO_HATCH_LEVEL_ONE;
        }
    }

    public static ElevatorPosition getPositionTwo() {
        switch (Manipulator.getInstance().getMode()) {
            case FINGER_HATCH:
                return FINGER_HATCH_LEVEL_TWO;
            case CARGO:
                return CARGO_LEVEL_TWO;
            case VELCRO_HATCH:
            default:
                return VELCRO_HATCH_LEVEL_TWO;
        }
    }

    public static ElevatorPosition getPositionThree() {
        switch (Manipulator.getInstance().getMode()) {
            case FINGER_HATCH:
                return FINGER_HATCH_LEVEL_THREE;
            case CARGO:
                return CARGO_LEVEL_THREE;
            case VELCRO_HATCH:
            default:
                return VELCRO_HATCH_LEVEL_THREE;
        }
    }

}
