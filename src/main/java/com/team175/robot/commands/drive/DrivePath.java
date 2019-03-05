package com.team175.robot.commands.drive;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.paths.Path;
import com.team175.robot.subsystems.Drive;

/**
 * Based off Team319's BobTrajectory.
 *
 * @author Arvind
 */
public class DrivePath extends AldrinCommand {

    private Path mPath;

    public DrivePath(Path path) {
        requires(Drive.getInstance());
        mPath = path;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPath(mPath);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return Drive.getInstance().isPathFinished();
    }

    @Override
    protected void end() {
        Drive.getInstance().stopPathFollowing();
        super.end();
    }

}
