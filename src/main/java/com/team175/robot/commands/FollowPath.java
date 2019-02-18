package com.team175.robot.commands;

import com.team175.robot.paths.Path;
import com.team175.robot.subsystems.Drive;

/**
 * Based off Team319's BobTrajectory.
 *
 * @author Arvind
 */
public class FollowPath extends AldrinCommand {

    private Path mPath;

    public FollowPath(Path path) {
        requires(Drive.getInstance());

        mPath = path;
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPath(mPath);

        super.logInit();
    }

    @Override
    protected boolean isFinished() {
        return Drive.getInstance().isPathFinished();
    }

    @Override
    protected void end() {
        Drive.getInstance().stopPathFollowing();

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
