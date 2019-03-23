package com.team175.robot.subsystems;

import com.team175.robot.util.drivers.Limelight;
import edu.wpi.first.cameraserver.CameraServer;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public final class Vision extends AldrinSubsystem implements Runnable {

    private final CameraServer mCamera;
    // private final Limelight mLimelight;

    private static Vision sInstance;

    public static Vision getInstance() {
        if (sInstance == null) {
            sInstance = new Vision();
        }

        return sInstance;
    }

    private Vision() {
        mCamera = CameraServer.getInstance();
        // mLimelight = new Limelight();
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
    }

    @Override
    public void stop() {
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        return null;
    }

    @Override
    public void run() {
        mCamera.addAxisCamera("10.1.12");
    }

}