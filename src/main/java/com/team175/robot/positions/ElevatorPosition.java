package com.team175.robot.positions;

import com.team175.robot.util.RobotManager;

/**
 * @author Arvind
 */
public enum ElevatorPosition {
    
    CARGO_LEVEL_THREE(23738, 24937),
    HATCH_LEVEL_THREE(21495, 21698),
    CARGO_LEVEL_TWO(14520, 14520),
    HATCH_LEVEL_TWO(11647, 11180),
    CARGO_LEVEL_ONE(4007, 4007),
    CARGO_LOADING(10000, 10000),
    GROUND_PICKUP_ABOVE(1918, 1211),
    HATCH_LEVEL_ONE(281, 348),
    GROUND_PICKUP(415, 415);

    private final int mCompetitionPosition;
    private final int mPracticePosition;

    ElevatorPosition(int competitionPosition, int practicePosition) {
        mCompetitionPosition = competitionPosition;
        mPracticePosition = practicePosition;
    }

    public int getPosition() {
        return RobotManager.isCompetitionRobot() ? mCompetitionPosition : mPracticePosition;
    }

}
