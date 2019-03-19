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
import java.util.Optional;

/**
 * An object that contains all the subsystems and various other components (i.e. Compressor and PDP) of the robot to be
 * able to control them all from one place.
 *
 * TODO: MAYBE BE ABLE TO CONTROL BOTH COMPRESSORS
 * TODO: Add ability to start and end loops for each subsystem
 *
 * @author Arvind
 */
public final class RobotManager {

    private final List<AldrinSubsystem> mSubsystems;
    private final Compressor mCompressor;
    private final PowerDistributionPanel mPDP;
    /*private final Logger mLogger;

    private CSVWriter mWriter;*/

    private static Optional<RobotProfile> sProfile = Optional.empty();
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
        mSubsystems = List.of(Drive.getInstance(), Elevator.getInstance(), LateralDrive.getInstance(), Lift.getInstance(),
                Manipulator.getInstance());
        // mSubsystems = List.of(Drive.getInstance(), Elevator.getInstance(), Lift.getInstance(), Manipulator.getInstance());
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
    }

    public void outputToDashboard() {
        // Subsystems
        mSubsystems.forEach(AldrinSubsystem::outputToDashboard);

        // PDP
        SmartDashboard.putNumber("PDPTemp", mPDP.getTemperature());
        SmartDashboard.putNumber("PDPCurrent", mPDP.getTotalCurrent());
        SmartDashboard.putNumber("PDPEnergy", mPDP.getTotalEnergy());
        SmartDashboard.putNumber("PDPPower", mPDP.getTotalPower());

        // Compressor
        SmartDashboard.putNumber("CompressorCurrent", mCompressor.getCompressorCurrent());
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
        boolean isGood = true;
        for (AldrinSubsystem subsystem : mSubsystems) {
            isGood &= subsystem.checkSubsystem();
        }
        return isGood;
    }

    public void startTuning() {
        /*
        startNotifier()
        In Notifier:
            updateCSVWriter()
            updateFromDashboard()
            flushCSVWriter()
         */
    }

    public void stopTuning() {
        /*
        flushCSVWriter()
        stopNotifier()
         */
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

    public void stop() {
        mSubsystems.forEach(AldrinSubsystem::stop);
        stopCompressor();
    }

    public static void setProfile(boolean isCompBot) {
        sProfile = isCompBot ? Optional.of(new CompetitionRobot()) : Optional.of(new PracticeRobot());
    }

    public static RobotProfile getProfile() {
        return sProfile.orElseThrow(IllegalStateException::new);
    }

    public static boolean isCompetitionRobot() {
        return getProfile() instanceof CompetitionRobot;
    }

}
