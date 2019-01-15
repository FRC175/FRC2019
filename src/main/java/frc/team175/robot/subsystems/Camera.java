package frc.team175.robot.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class Camera extends AldrinSubsystem implements Runnable {
    
    private UsbCamera camera;

    private static Camera sInstance;

    public static Camera getInstance() {
        if (sInstance == null) {
            try {
                sInstance = new Camera();
            } catch(Exception e) {
                // Insert log here
            }
        }

        return sInstance;
    }

    private Camera() {
        // camera = CameraServer.getInstance().startAutomaticCapture();
        // camera.setResolution(640, 480);
        // camera.setFPS(60);
    }

    public void setExposure(int value) {
        camera.setExposureManual(value);
    }

    @Override
    public void onTeleop() {
    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public void run() {
    }

}