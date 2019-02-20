package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.Constants;
import com.team175.robot.positions.LineSensorPosition;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.CTREDiagnostics;
import com.team175.robot.util.drivers.CTREFactory;

import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import com.team175.robot.util.tuning.ClosedLoopGains;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public final class LateralDrive extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mMaster;
    private final SimpleDoubleSolenoid mDeploy;
    // private final Map<String, DigitalInput> mLineSensors;
    // private final Pixy mPixy;

    private int mWantedPosition;
    private ClosedLoopGains mGains;

    // Singleton Instance
    private static LateralDrive sInstance;

    public static LateralDrive getInstance() {
        if (sInstance == null) {
            sInstance = new LateralDrive();
        }

        return sInstance;
    }

    private LateralDrive() {
        /* Instantiations */
        // CTREFactory.getMasterTalon(portNum : int)
        mMaster = CTREFactory.getMasterTalon(Constants.LATERAL_DRIVE_PORT);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int, pcmID : int)
        mDeploy = new SimpleDoubleSolenoid(Constants.LATERAL_DRIVE_DEPLOY_FORWARD_CHANNEL, Constants.LATERAL_DRIVE_DEPLOY_REVERSE_CHANNEL,
                Constants.PCM_NUMBER_TWO_ID);

        // DigitalInput(portNum : int)
        /*mLineSensors = Map.of(
                "LeftTwo", new DigitalInput(Constants.LEFT_TWO_SENSOR_PORT),
                "LeftOne", new DigitalInput(Constants.LEFT_ONE_SENSOR_PORT),
                "Center", new DigitalInput(Constants.CENTER_SENSOR_PORT),
                "RightOne", new DigitalInput(Constants.RIGHT_ONE_SENSOR_PORT),
                "RightTwo", new DigitalInput(Constants.RIGHT_TWO_SENSOR_PORT)
        );*/

        mWantedPosition = 0;
        mGains = Constants.LATERAL_DRIVE_GAINS;

        /* Configuration */
        CTREDiagnostics.checkCommand(mMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder),
                "Failed to config LateralDrive encoder!");
        setGains(mGains);
        resetSensors();
        stop();
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

    /*public double getVoltage() {
        return mMaster.getMotorOutputVoltage();
    }*/

    public void setPosition(int position) {
        mWantedPosition = position;
        mMaster.set(ControlMode.MotionMagic, mWantedPosition);
    }

    public void setPosition(LineSensorPosition position) {
        setPosition(position.positionToMove());
    }

    public int getPosition() {
        return mMaster.getSelectedSensorPosition();
    }

    public int getVelocity() {
        return mMaster.getSelectedSensorVelocity();
    }

    public boolean isAtWantedPosition() {
        return Math.abs(getPosition() - mWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
    }

    public void setGains(ClosedLoopGains gains) {
        mGains = gains;
        CTREDiagnostics.checkCommand(mMaster.configPIDF(mGains.getKp(), mGains.getKi(), mGains.getKd(), mGains.getKf()),
                "Failed to config LateralDrive PID gains!");
        CTREDiagnostics.checkCommand(mMaster.configMotionAcceleration(mGains.getAcceleration()),
                "Failed to config LateralDrive acceleration!");
        CTREDiagnostics.checkCommand(mMaster.configMotionCruiseVelocity(mGains.getCruiseVelocity()),
                "Failed to config LateralDrive cruise velocity!");
    }

    @Override
    public void resetSensors() {
        CTREDiagnostics.checkCommand(mMaster.setSelectedSensorPosition(0), "Failed to zero LateralDrive encoder!");
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
    public void stop() {
        setPower(0);
        deploy(false);
    }

    @Override
    public Map<String, Object> getTelemetry() {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("LateralKp", mGains.getKp());
        m.put("LateralKd", mGains.getKd());
        m.put("LateralKf", mGains.getKf());
        m.put("LateralAccel", mGains.getAcceleration());
        m.put("LateralCruiseVel", mGains.getCruiseVelocity());
        m.put("LateralWantedPos", mWantedPosition);
        m.put("LateralPos", getPosition());
        m.put("LateralPower", getPower());
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
        setGains(new ClosedLoopGains(SmartDashboard.getNumber("LateralKp", 0), 0,
                SmartDashboard.getNumber("LateralKd", 0),
                SmartDashboard.getNumber("LateralKf", 0),
                (int) SmartDashboard.getNumber("LateralAccel", 0),
                (int) SmartDashboard.getNumber("LateralCruiseVel", 0)));
        setPosition((int) SmartDashboard.getNumber("LateralWantedPos", 0));
    }

    @Override
    public void updateGains() {
        updateFromDashboard();
        mLogger.debug("Wanted Position: {}", mWantedPosition);
        mLogger.debug("Current Position: {}", getPosition());
    }

    @Override
    public void reset() {
        resetSensors();
    }

    @Override
    public Map<String, DoubleSupplier> getCSVTelemetry() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }

}
