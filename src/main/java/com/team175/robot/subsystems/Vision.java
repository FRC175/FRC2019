package com.team175.robot.subsystems;

import com.team175.robot.Constants;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
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
        mRotate.set(rotateDown ? 0.4 : 0.7);
    }

    public boolean isCameraDown() {
        return mRotate.get() == 0.4;
    }

    @Override
    public void start() {
        // new Thread(() -> mCamera.addAxisCamera("10.1.75.10")).start();
    }

    @Override
    public void stop() {
        rotateCameraDown(false);
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("CameraRotateAngle", mRotate::get);
        m.put("IsCameraDown", this::isCameraDown);
        return m;
    }

    @Override
    public void updateFromDashboard() {
        mRotate.set(SmartDashboard.getNumber("CameraRotateAngle", 0));
    }

    @Override
    public void run() {
        mCamera.addAxisCamera("10.1.75.10");
    }

}