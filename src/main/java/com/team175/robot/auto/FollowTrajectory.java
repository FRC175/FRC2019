package com.team175.robot.auto;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;

/**
 * Based off Team 319's BobTrajectory.
 *
 * @author Arvind
 */
public class FollowTrajectory extends AldrinCommand {

    private Trajectory mTrajectory;

    public FollowTrajectory(Trajectory trajectory) {
        mTrajectory = trajectory;
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setTrajectory(mTrajectory);

        super.logInit();
    }

    @Override
    protected boolean isFinished() {
        return Drive.getInstance().isTrajectoryFinished();
    }

    @Override
    protected void end() {
        Drive.getInstance().stopTrajectory();

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

    private void loadBuffer() {

    }

}
