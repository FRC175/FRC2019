package com.team175.robot.commands.tuning;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.util.CSVWriter;
import edu.wpi.first.wpilibj.Timer;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CollectMotionData extends AldrinCommand {

    private Map<String, Supplier> mData;
    private CSVWriter mWriter;
    private double mStartTime, mPrevTime, mCurrentTime;
    private int mMaxVel, mMaxAccel, mMaxJerk;
    private double[][] mMotionData;

    private static final String FILE_PATH = "/home/lvuser/csvlog/motion-data.csv";
    private static final String DELIMITER = ",";

    public CollectMotionData() {
        requires(Drive.getInstance());

        /*mData = new LinkedHashMap<>();
        mData.put("left_velocity", Drive.getInstance()::getLeftVelocity);
        mData.put("right_velocity", Drive.getInstance()::getRightVelocity);
        try {
            mWriter = new CSVWriter(mData, FILE_PATH, DELIMITER);
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate CSVWriter!", e);
        }*/

        mMaxVel = mMaxAccel = mMaxJerk = Integer.MIN_VALUE;
        mStartTime = mPrevTime = mCurrentTime = 0;
    }

    @Override
    protected void initialize() {
        mPrevTime = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        mCurrentTime = Timer.getFPGATimestamp();

        if (mPrevTime == mCurrentTime) {
            mPrevTime = Timer.getFPGATimestamp();
        } else {

        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        // Determine max jerk

        mLogger.info("Collection of motion data complete!");
        // mLogger.info("Retrieve csv log from RoboRIO and use kinematics script to analyze motion.");
        mLogger.info("Max Velocity: {}", mMaxVel);
        mLogger.info("Max Acceleration: {}", mMaxAccel);
        mLogger.info("Max Jerk: {}", mMaxJerk);
    }

}
