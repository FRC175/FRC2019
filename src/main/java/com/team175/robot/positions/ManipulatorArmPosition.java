package com.team175.robot.positions;

import com.team175.robot.util.RobotManager;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition {

    HATCH_PICKUP(386, 178),
    BALL_PICKUP(338, 186),
    SCORE(254, 75),
    STOW(187, -190);

    private final int mCompetitionPosition;
    private final int mPracticePosition;

    ManipulatorArmPosition(int competitionPosition, int practicePosition) {
        mCompetitionPosition = competitionPosition;
        mPracticePosition = practicePosition;
    }

    public int getPosition() {
        return RobotManager.isCompetitionRobot() ? mCompetitionPosition : mPracticePosition;
    }

}
