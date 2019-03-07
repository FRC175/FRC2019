package com.team175.robot.util;

import com.team175.robot.Constants;
import com.team175.robot.profiles.CompetitionRobot;
import com.team175.robot.profiles.PracticeRobot;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.subsystems.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.List;

/**
 * An object that contains all the subsystems and various other components (i.e. Compressor and PDP) of the robot to be
 * able to control them all from one place.
 *
 * TODO: MAYBE BE ABLE TO CONTROL BOTH COMPRESSORS
 *
 * @author Arvind
 */
public class RobotManager {

    private final List<AldrinSubsystem> mSubsystems;
    private final Compressor mCompressor;
    private final PowerDistributionPanel mPDP;
    /*private final Logger mLogger;

    private CSVWriter mWriter;*/
    private RobotProfile mProfile;

    private static RobotManager sInstance;

    private static final String LOG_FILE_PATH = "/home/lvuser/csvlog/all-telemetry.csv";
    private static final String LOG_DELIMITER = ",";

    public static RobotManager getInstance() {
        if (sInstance == null) {
            sInstance = new RobotManager();
        }

        return sInstance;
    }

    private RobotManager() {
        mSubsystems = List.of(Elevator.getInstance(), LateralDrive.getInstance(), Lift.getInstance(),
                Manipulator.getInstance());
        mCompressor = new Compressor();
        mPDP = new PowerDistributionPanel(Constants.PDP_PORT);
        /*mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

        Map<String, Supplier> m = new LinkedHashMap<>();
        // Only add subsystems who are CSVWritable
        mSubsystems.forEach((s) -> s.getTelemetry().forEach((k, v) -> {
                    if (s instanceof CSVWritable) {
                        m.put(k, v);
                    }
                }
        ));
        m.put("time", Timer::getFPGATimestamp);
        try {
            mWriter = new CSVWriter(m, LOG_FILE_PATH, LOG_DELIMITER);
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate CSVWriter!", e);
        }*/

        mProfile = new CompetitionRobot();
    }

    public void outputToDashboard() {
        // Subsystems
        mSubsystems.forEach(AldrinSubsystem::outputToDashboard);

        /*// PDP
        SmartDashboard.putNumber("PDPTemp", mPDP.getTemperature());
        SmartDashboard.putNumber("PDPCurrent", mPDP.getTotalCurrent());
        SmartDashboard.putNumber("PDPEnergy", mPDP.getTotalEnergy());
        SmartDashboard.putNumber("PDPPower", mPDP.getTotalPower());

        // Compressor
        SmartDashboard.putNumber("CompressorCurrent", mCompressor.getCompressorCurrent());*/
    }

    public void updateFromDashboard() {
        mSubsystems.forEach(AldrinSubsystem::updateFromDashboard);
    }

    /*public void startCSVLog() {
        mWriter.write();
    }

    public void stopCSVLog() {
        mWriter.flush();
    }*/

    public boolean checkSubsystems() {
        /*boolean isGood = true;
        for (AldrinSubsystem subsystem : mSubsystems) {
            isGood &= subsystem.checkSubsystem();
        }

        return isGood;*/
        return true;
    }

    public void startCompressor() {
        mCompressor.start();
    }

    public void stopCompressor() {
        mCompressor.stop();
    }

    public double getCurrentForPDPChannel(int channel) {
        return mPDP.getCurrent(channel);
    }

    public void setProfile(boolean isCompBot) {
        mProfile = isCompBot ? new CompetitionRobot() : new PracticeRobot();
    }

    public RobotProfile getProfile() {
        return mProfile;
    }

    public boolean isCompetitionRobot() {
        return mProfile instanceof CompetitionRobot;
    }

    public void stop() {
        // mSubsystems.forEach(AldrinSubsystem::stop);
        stopCompressor();
    }

}
