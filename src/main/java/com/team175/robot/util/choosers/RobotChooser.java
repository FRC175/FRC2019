package com.team175.robot.util.choosers;

import com.team175.robot.profiles.CompetitionRobot;
import com.team175.robot.profiles.PracticeRobot;
import com.team175.robot.profiles.RobotProfile;

/**
 * @author Arvind
 */
public class RobotChooser {

    private RobotProfile mProfile;

    private static RobotChooser sInstance;

    public static RobotChooser getInstance() {
        if (sInstance == null) {
            sInstance = new RobotChooser();
        }

        return sInstance;
    }

    private RobotChooser() {
        mProfile = new CompetitionRobot();
    }

    public void setProfile(boolean isCompBot) {
        mProfile = isCompBot ? new CompetitionRobot() : new PracticeRobot();
    }

    public RobotProfile getProfile() {
        return mProfile;
    }

    public boolean isCompetitionRobot() {
        return mProfile instanceof CompetitionRobot;
    }

}
