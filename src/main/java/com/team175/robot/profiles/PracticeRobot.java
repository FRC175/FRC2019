package com.team175.robot.profiles;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.util.CTREConfiguration;
import com.team175.robot.util.tuning.ClosedLoopGains;

/**
 * @author Arvind
 */
public class PracticeRobot extends RobotProfile {

    @Override
    public CTREConfiguration getLeftMasterConfig() {
        return new CTREConfiguration.Builder()
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(0.1, 0, 0, super.getDriveTransmission().getKf(),
                        0, 0))
                .build();
    }

    @Override
    public CTREConfiguration getLeftSlaveConfig() {
        return new CTREConfiguration.Builder().build();
    }

    @Override
    public CTREConfiguration getRightMasterConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(0.1, 0, 0, super.getDriveTransmission().getKf(),
                        0, 0))
                .setAuxGains(new ClosedLoopGains(0.1, 0, 0, 0, 0, 0))
                .build();
    }

    @Override
    public CTREConfiguration getRightSlaveConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .build();
    }

    @Override
    public CTREConfiguration getElevatorConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .setPrimarySensor(FeedbackDevice.CTRE_MagEncoder_Relative)
                .setSensorPhase(true)
                // Forward Gains
                .setPrimaryGains(new ClosedLoopGains(8, 0, 0, super.getElevatorTransmission().getKf(),
                        1000, 1000
                ))
                // Reverse Gains
                .setAuxGains(new ClosedLoopGains(8, 0, 0, super.getElevatorTransmission().getKf(),
                        1000, 1000
                ))
                .build();
    }

    @Override
    public CTREConfiguration getLateralDriveConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(0.1, 0, 0, super.getLateralDriveTransmission().getKf(),
                        0, 0))
                .build();
    }

    @Override
    public CTREConfiguration getFrontLiftConfig() {
        return new CTREConfiguration.Builder()
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(0.1, 0, 0, super.getLiftTransmission().getKf(),
                        0, 0))
                .build();
    }

    @Override
    public CTREConfiguration getRearLiftConfig() {
        return new CTREConfiguration.Builder()
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(0.1, 0, 0, super.getLiftTransmission().getKf(),
                        0, 0))
                .build();
    }

    @Override
    public CTREConfiguration getManipulatorArmMasterConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .setPrimarySensor(FeedbackDevice.Analog)
                .setSensorPhase(true)
                // Forward Gains
                .setPrimaryGains(new ClosedLoopGains(12, 0, 0, super.getManipulatorArmTransmission().getKf(),
                        600, 600))
                // Reverse Gains
                .setAuxGains(new ClosedLoopGains(20, 0, 0, super.getManipulatorArmTransmission().getKf(),
                        600, 600))
                .build();
    }

    @Override
    public CTREConfiguration getManipulatorArmSlaveConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .build();
    }

}
