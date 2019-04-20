package com.team175.robot.subsystems;

import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.util.drivers.Limelight;
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
public final class Vision extends AldrinSubsystem {

    // private final Limelight mLimelight;
    private final Servo mRotate;
    private final Manipulator mManipulator; // I don't like one subsystem requiring another but it has to be done

    private VisionState mWantedState;

    private static Vision sInstance;

    private enum VisionState {
        UPDATE,
        MANUAL;
    }

    public static Vision getInstance() {
        if (sInstance == null) {
            sInstance = new Vision();
        }

        return sInstance;
    }

    private Vision() {
        // mLimelight = new Limelight();

        // Run camera on separate thread
        new Thread(() -> CameraServer.getInstance().addAxisCamera("10.1.75.10")).start();

        mManipulator = Manipulator.getInstance();

        // Servo(portNum : int)
        mRotate = new Servo(Constants.CAMERA_ROTATE_PORT);

        mWantedState = VisionState.MANUAL;

        // Start with camera in up position
        rotateCameraDown(false);

        super.logInstantiation();
    }

    public void rotateCameraDown(boolean rotateDown) {
        mRotate.set(rotateDown ? 0.57 : 0.7); // 0.57 for practice bot
    }

    public boolean isCameraDown() {
        return mRotate.get() == 0.57;
    }

    @Override
    public void start() {
        // mLimelight.setLEDState(false);
    }

    @Override
    public void loop() {
        // TODO: Check
        if (mManipulator.getArmPosition() < ManipulatorArmPosition.STOW.getPosition()) {
            rotateCameraDown(false);
        }
        outputToDashboard();
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
}