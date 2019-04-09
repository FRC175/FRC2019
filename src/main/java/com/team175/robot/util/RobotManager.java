package com.team175.robot.util;

import com.team175.robot.Constants;
import com.team175.robot.loops.CSVWriterLoop;
import com.team175.robot.loops.Looper;
import com.team175.robot.loops.MessageLoop;
import com.team175.robot.profiles.CompetitionRobot;
import com.team175.robot.profiles.PracticeRobot;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.subsystems.*;
import com.team175.robot.util.tuning.CSVWritable;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Supplier;

/**
 * Contains all the subsystems and various other components (i.e. Compressor and PDP) of the robot to be able to control
 * them all from one place.
 *
 * TODO: Maybe be able to control both compressors.
 *
 * @author Arvind
 */
public final class RobotManager {

    private final List<AldrinSubsystem> mSubsystems;
    private final Compressor mCompressor;
    private final PowerDistributionPanel mPDP;
    private final Logger mLogger;
    private final String[] mNaan = {
            "Abdullah is watching you.",
            "Jamie is the all supreme lord.",
            "Bret is MVP.",
            "Andrew Jones was here.",
            "Dimepiece is Gulag MemeLord!",
            "Aaron likes his waifu!",
            "Aaron's girlfriend is fake!",
            "Join the Abdullah, cult or else...",
            "Join the Arvind, fan club or else...",
            "Big Jim Morin fan here!",
            "Without Boyer, our robot would not work.",
            "Jamie likes lolis.",
            "What are you doing?!",
            "Ben is the absolute unit.",
            "Don't miss!",
            "Roland is turning in his grave.",
            "Tyrique misses you.",
            "Everyone disliked that.",
            "This messaging system was brought to you by team 254."
    };

    private Looper mSubsystemLooper, mCSVLooper, mMessageLooper, mLEDLooper;

    private static Optional<RobotProfile> sProfile = Optional.empty();
    private static RobotManager sInstance;

    private static final String CSV_LOG_FILE_PATH = "/home/lvuser/csvlog/subsystem-telemetry.csv";
    private static final double LOOPER_PERIOD = 0.01;
    private static final double MESSAGE_PERIOD = 1;

    public static RobotManager getInstance() {
        if (sInstance == null) {
            sInstance = new RobotManager();
        }

        return sInstance;
    }

    private RobotManager() {
        mSubsystems = List.of(Drive.getInstance(), Elevator.getInstance(), LateralDrive.getInstance(), Lift.getInstance(),
                Manipulator.getInstance(), Vision.getInstance());
        mCompressor = new Compressor(Constants.COMPRESSOR_PORT);
        mPDP = new PowerDistributionPanel(Constants.PDP_PORT);
        mLogger = LoggerFactory.getLogger(getClass().getSimpleName());
        mSubsystemLooper = new Looper(LOOPER_PERIOD, mSubsystems);
        mMessageLooper = new Looper(MESSAGE_PERIOD, new MessageLoop(mNaan, "Survey Says"));
        // mLEDLooper = new Looper(LOOPER_PERIOD, LED.getInstance());

        LinkedHashMap<String, Supplier> data = new LinkedHashMap<>();
        // Add data from each subsystem's getCSVTelemetry()
        for (AldrinSubsystem s : mSubsystems) {
            if (s instanceof CSVWritable) {
                ((CSVWritable) s).getCSVTelemetry().forEach(data::put);
            }
        }
        data.put("time", Timer::getFPGATimestamp);
        try {
            mCSVLooper = new Looper(LOOPER_PERIOD, new CSVWriterLoop(data, CSV_LOG_FILE_PATH));
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate CSVLooper!", e);
        }
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

    public void startCSVLogging() {
        mCSVLooper.start();
    }

    public void stopCSVLogging() {
        mCSVLooper.stop();
    }

    public boolean checkSubsystems() {
        boolean isGood = true;
        for (AldrinSubsystem subsystem : mSubsystems) {
            isGood &= subsystem.checkSubsystem();
        }
        return isGood;
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

    public void startSubsystems() {
        mSubsystemLooper.start();
    }

    public void stopSubsystems() {
        mSubsystemLooper.stop();
        stopCompressor();
    }

    public void startMessaging() {
        mMessageLooper.start();
    }

    public void stopMessaging() {
        mMessageLooper.stop();
    }

    public void startLED() {
        // mLEDLooper.start();
    }

    public void stopLED() {
        // mLEDLooper.stop();
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
