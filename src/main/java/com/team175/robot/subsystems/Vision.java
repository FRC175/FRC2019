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
    private final Servo mRotate;

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
        mRotate = new Servo(Constants.CAMERA_ROTATE_PORT);

        // Start with camera in up position
        rotateCameraDown(false);

        super.logInstantiation();
    }

    public void rotateCameraDown(boolean rotateDown) {
        mRotate.set(rotateDown ? 0.8 : 0.2);
    }

    public boolean isCameraDown() {
        return mRotate.get() == 0.8;
    }

    @Override
    public void start() {
        new Thread(() -> mCamera.addAxisCamera("10.1.75.10")).start();
        rotateCameraDown(false);
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