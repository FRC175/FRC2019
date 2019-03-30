package com.team175.robot.subsystems;

import com.team175.robot.Constants;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Servo;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public final class Vision extends AldrinSubsystem implements Runnable {

    private final CameraServer mCamera;

    private static Vision sInstance;

    public static Vision getInstance() {
        if (sInstance == null) {
            sInstance = new Vision();
        }

        return sInstance;
    }

    private Vision() {
        mCamera = CameraServer.getInstance();

        // Servo(portNum : int)


        super.logInstantiation();
    }

    @Override
    public void start() {
        new Thread(this).start();
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
        mCamera.addAxisCamera("10.1.75.10");
    }

}