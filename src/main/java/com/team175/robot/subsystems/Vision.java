package com.team175.robot.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * @author Arvind
 */
public class Vision extends AldrinSubsystem {

    /* Declarations */
    private UsbCamera mCamera;

    // Singleton Instance
    private static Vision sInstance;

    public static Vision getInstance() {
        if (sInstance == null) {
            sInstance = new Vision();
        }

        return sInstance;
    }

    private Vision() {
        /* Instantiation */
        mCamera = CameraServer.getInstance().startAutomaticCapture();
    }

    @Override
    protected void initDefaultCommand() {
    }

}