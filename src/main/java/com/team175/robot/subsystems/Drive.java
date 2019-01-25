package com.team175.robot.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CSVLogger;
import com.team175.robot.util.Diagnosable;
import com.team175.robot.util.Loggable;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * @author Arvind
 */
public class Drive extends AldrinSubsystem implements Loggable, Diagnosable {

    // Talon SRXs
    private AldrinTalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;

    // Gyro
    private PigeonIMU mPigeon;

    // Solenoid
    private Solenoid mShift;

    // Loggers
    private CSVLogger mCSVLogger;

    // Singleton Instance
    private static Drive sInstance;

    public static Drive getInstance() {
        if (sInstance == null) {
            sInstance = new Drive();
        }

        return sInstance;
    }

    private Drive() {

    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public void outputToDashboard() {
    }

    @Override
    public void outputToLog() {
    }

    @Override
    public boolean checkSubsystem() {
        return false;
    }
}
