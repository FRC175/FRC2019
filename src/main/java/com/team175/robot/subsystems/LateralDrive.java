package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.team175.robot.Constants;
import com.team175.robot.positions.LineSensorPosition;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.*;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import com.team175.robot.util.tuning.ClosedLoopGains;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public final class LateralDrive extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mMaster;
    private final SimpleDoubleSolenoid mDeploy;
    // private final Map<String, DigitalInput> mLineSensors;
    // private final Pixy mPixy;
    // private final DigitalInput mPixy;

    private int mWantedPosition;
    private ClosedLoopGains mGains;

    private static final int ALLOWED_POSITION_DEVIATION = 10;

    private static LateralDrive sInstance;

    public static LateralDrive getInstance() {
        if (sInstance == null) {
            sInstance = new LateralDrive();
        }

        return sInstance;
    }

    private LateralDrive() {
        // CTREFactory.getMasterTalon(portNum : int)
        mMaster = CTREFactory.getMasterTalon(Constants.LATERAL_DRIVE_PORT);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int, isOnPCMTwo : boolean)
        mDeploy = new SimpleDoubleSolenoid(Constants.LATERAL_DRIVE_DEPLOY_FORWARD_CHANNEL, Constants.LATERAL_DRIVE_DEPLOY_REVERSE_CHANNEL,
                true);

        // DigitalInput(portNum : int)
        // mPixy = new DigitalInput();
        /*mLineSensors = Map.of(
                "LeftTwo", new DigitalInput(Constants.LEFT_TWO_SENSOR_PORT),
                "LeftOne", new DigitalInput(Constants.LEFT_ONE_SENSOR_PORT),
                "Center", new DigitalInput(Constants.CENTER_SENSOR_PORT),
                "RightOne", new DigitalInput(Constants.RIGHT_ONE_SENSOR_PORT),
                "RightTwo", new DigitalInput(Constants.RIGHT_TWO_SENSOR_PORT)
        );*/

        mWantedPosition = 0;

        RobotProfile profile = RobotManager.getProfile();
        CTREConfiguration.config(mMaster, profile.getLateralDriveConfig(), "LateralDrive");
        mGains = CTREConfiguration.getGains(profile.getLateralDriveConfig(), true);

        resetSensors();
        stop();

        super.logInstantiation();
    }

    public void deploy(boolean enable) {
        mDeploy.set(enable);
    }

    public boolean isDeployed() {
        return mDeploy.get();
    }

    public void setPower(double power) {
        mMaster.set(ControlMode.PercentOutput, power);
    }

    public double getPower() {
        return mMaster.getMotorOutputPercent();
    }

    public void setPosition(int position) {
        mWantedPosition = position;
        mLogger.debug("Setting position to {}.", mWantedPosition);
        mLogger.debug("Current position: {}", getPosition());
        mMaster.set(ControlMode.MotionMagic, mWantedPosition);
    }

    public void setPosition(LineSensorPosition position) {
        setPosition(position.getPosition());
    }

    public int getPosition() {
        return mMaster.getSelectedSensorPosition();
    }

    public int getVelocity() {
        return mMaster.getSelectedSensorVelocity();
    }

    public boolean isAtWantedPosition() {
        return Math.abs(getPosition() - mWantedPosition) <= ALLOWED_POSITION_DEVIATION;
    }

    public void setGains(ClosedLoopGains gains) {
        mGains = gains;
        CTREConfiguration.setGains(mMaster, mGains, true,"LateralDrive");
    }

    /*private String getLineSensorArray() {
        String binarySensorArray = "";
        binarySensorArray += mLineSensors.get("LeftTwo").get() ? "1" : "0";
        binarySensorArray += mLineSensors.get("LeftOne").get() ? "1" : "0";
        binarySensorArray += mLineSensors.get("Center").get() ? "1" : "0";
        binarySensorArray += mLineSensors.get("RightOne").get() ? "1" : "0";
        binarySensorArray += mLineSensors.get("RightTwo").get() ? "1" : "0";

        return binarySensorArray;
    }

    public LineSensorPosition getLineSensorPosition() {
        switch (getLineSensorArray()) {
            case "10000":
                return LineSensorPosition.EXTREME_LEFT;
            case "11000":
                return LineSensorPosition.LEFTIST;
            case "01000":
                return LineSensorPosition.LEFT;
            case "01100":
                return LineSensorPosition.CENTER_LEFT;
            case "00100":
                return LineSensorPosition.CENTER;
            case "00110":
                return LineSensorPosition.CENTER_RIGHT;
            case "00010":
                return LineSensorPosition.RIGHT;
            case "00011":
                return LineSensorPosition.RIGHTIST;
            case "00001":
                return LineSensorPosition.EXTREME_RIGHT;
            default:
                // Robot orientation wrong
                return LineSensorPosition.ERROR;
        }
    }*/

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        setPower(0);
        deploy(false);
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("LateralKp", () -> mGains.getKp());
        m.put("LateralKd", () -> mGains.getKd());
        m.put("LateralKf", () -> mGains.getKf());
        m.put("LateralAccel", () -> mGains.getAcceleration());
        m.put("LateralCruiseVel", () -> mGains.getCruiseVelocity());
        m.put("LateralWantedPos", () -> mWantedPosition);
        m.put("LateralPos", this::getPosition);
        m.put("LateralVel", this::getVelocity);
        m.put("LateralPower", this::getPower);
        m.put("LateralIsDeployed", this::isDeployed);
        return m;
    }

    @Override
    public boolean checkSubsystem() {
        CTREDiagnostics diag = new CTREDiagnostics(mMaster, "LateralDrive");

        deploy(true);
        mLogger.info("Beginning diagnostics test for LateralDrive subsystem.");
        boolean isGood = diag.checkMotorController();
        mLogger.info(diag.toString());

        if (!isGood) {
            mLogger.error("LateralDrive subsystem failed diagnostics test!");
        } else {
            mLogger.info("LateralDrive subsystem passed diagnostics test!");
        }

        return isGood;
    }

    @Override
    public void updateFromDashboard() {
        setGains(new ClosedLoopGains(
                SmartDashboard.getNumber("LateralKp", 0),
                0,
                SmartDashboard.getNumber("LateralKd", 0),
                SmartDashboard.getNumber("LateralKf", 0),
                (int) SmartDashboard.getNumber("LateralAccel", 0),
                (int) SmartDashboard.getNumber("LateralCruiseVel", 0)
        ));
        setPosition((int) SmartDashboard.getNumber("LateralWantedPos", 0));
    }

    @Override
    public void updateGains() {
        updateFromDashboard();
    }

    @Override
    public void resetSensors() {
        CTREDiagnostics.checkCommand(mMaster.setPrimarySensorPosition(0), "Failed to zero LateralDrive encoder!");
    }

    @Override
    public Map<String, Supplier> getCSVTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("lateral_position", this::getPosition);
        m.put("lateral_wanted_position", () -> mWantedPosition);
        m.put("lateral_velocity", this::getVelocity);
        return m;
    }

}
