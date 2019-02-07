package com.team175.robot.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * @author Arvind
 */
public class Vision extends AldrinSubsystem implements Runnable {

    /* Declarations */
    private CameraServer mCamera;

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
        mCamera = CameraServer.getInstance();
    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public void run() {
        mCamera.startAutomaticCapture();
    }

    @Override
    public void stop() {
        mCamera = null;
    }

}