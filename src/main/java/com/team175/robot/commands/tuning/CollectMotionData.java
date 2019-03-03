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
    private boolean mVelOrAccel;
    private double mStartTime;
    private double mCurrentTime;
    private int mMax;

    private static final String FILE_PATH = "/home/lvuser/csvlog/motion-data.csv";
    private static final String DELIMITER = ",";

    public CollectMotionData(boolean velOrAccel) {
        requires(Drive.getInstance());

        mData = new LinkedHashMap<>();
        mData.put("left_velocity", Drive.getInstance()::getLeftVelocity);
        mData.put("right_velocity", Drive.getInstance()::getRightVelocity);
        try {
            mWriter = new CSVWriter(mData, FILE_PATH, DELIMITER);
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate CSVWriter!", e);
        }

        mVelOrAccel = velOrAccel;
        mStartTime = 0;
        mCurrentTime = 0;
        mMax = Integer.MIN_VALUE;
    }

    @Override
    protected void initialize() {
        mStartTime = !mVelOrAccel ? Timer.getFPGATimestamp() : mStartTime;
    }

    @Override
    protected void execute() {
        if (mVelOrAccel) { // Collect velocity data

        } else { // Collect acceleration data
            mCurrentTime = Timer.getFPGATimestamp();

            if (mStartTime == mCurrentTime) {
                return;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        mLogger.info("Collection of {} data for Drive complete!", mVelOrAccel ? "velocity" : "acceleration");
        // mLogger.info("Retrieve csv log from RoboRIO and use kinematics script to analyze motion.");
        mLogger.info("Max: {}", mMax);
    }

}
