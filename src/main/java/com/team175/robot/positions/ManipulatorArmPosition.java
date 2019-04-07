package com.team175.robot.positions;

import com.team175.robot.subsystems.Manipulator;
import com.team175.robot.util.RobotManager;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition {

    VELCRO_HATCH_PICKUP(386, 294),
    FINGER_HATCH_PICKUP(317, 180),
    FINGER_HATCH_TILT(295, 295),
    BALL_PICKUP(320, 195),
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

    public static ManipulatorArmPosition getHatchPosition(boolean isScorePosition) {
        switch (Manipulator.getInstance().getMode()) {
            case FINGER_HATCH:
                return ManipulatorArmPosition.FINGER_HATCH_PICKUP;
            case CARGO:
                return isScorePosition ? ManipulatorArmPosition.SCORE : ManipulatorArmPosition.BALL_PICKUP;
            case VELCRO_HATCH:
            default:
                return isScorePosition ? ManipulatorArmPosition.SCORE : ManipulatorArmPosition.VELCRO_HATCH_PICKUP;
        }
    }

}
