package com.team175.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;

import java.util.Map;

/**
 * @author Arvind
 */
public final class Vision extends AldrinSubsystem implements Runnable {

    /* Declarations */
    private final CameraServer mCamera;

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
    public void run() {
        mCamera.startAutomaticCapture();
    }

    @Override
    public void stop() {
    }

    @Override
    public Map<String, Object> getTelemetry() {
        return null;
    }

    @Override
    public void updateFromDashboard() {
    }

}