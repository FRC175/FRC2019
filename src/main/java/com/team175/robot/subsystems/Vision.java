package com.team175.robot.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * @author Arvind
 */
public class Vision extends AldrinSubsystem {

    private UsbCamera mCamera;

    private static Vision sInstance;

    public static Vision getInstance() {
        if (sInstance == null) {
            sInstance = new Vision();
        }

        return sInstance;
    }

    private Vision() {
        try {
            mCamera = CameraServer.getInstance().startAutomaticCapture();
        } catch (Exception e) {
            mLogger.error("Vision failed to instantiate.", e);
        }

        mLogger.info("Vision instantiated successfully!");
    }

    @Override
    protected void initDefaultCommand() {
    }

}