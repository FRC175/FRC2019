package com.team175.robot.commands.vision;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Vision;

/**
 * @author Arvind
 */
public class RotateCamera extends AldrinCommand {

    public RotateCamera() {
        requires(Vision.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        // boolean isCameraDown = Vision.getInstance().isCameraDown();
        boolean isCameraDown = false;
        mLogger.info("Rotating camera to {} position", isCameraDown ? "up" : "down");
        // Vision.getInstance().rotateCameraDown(!isCameraDown);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
