package com.team175.robot.profiles;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.Constants;
import com.team175.robot.util.CTREConfiguration;
import com.team175.robot.util.ClosedLoopGains;

public class PracticeRobot extends RobotProfile {

    @Override
    public CTREConfiguration getLeftMasterConfig() {
        return new CTREConfiguration.Builder(false)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(
                        Constants.PRACTICE_LEFT_DRIVE_KP,
                        0,
                        Constants.PRACTICE_LEFT_DRIVE_KD,
                        super.getDriveTransmission().getKf(),
                        Constants.PRACTICE_LEFT_DRIVE_ACCELERATION,
                        Constants.PRACTICE_LEFT_DRIVE_CRUISE_VELOCITY
                ))
                .build();
    }

    @Override
    public CTREConfiguration getLeftSlaveConfig() {
        return new CTREConfiguration.Builder(false).build();
    }

    @Override
    public CTREConfiguration getRightMasterConfig() {
        return new CTREConfiguration.Builder(true)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(
                        Constants.PRACTICE_RIGHT_DRIVE_KP,
                        0,
                        Constants.PRACTICE_RIGHT_DRIVE_KD,
                        super.getDriveTransmission().getKf(),
                        Constants.PRACTICE_RIGHT_DRIVE_ACCELERATION,
                        Constants.PRACTICE_RIGHT_DRIVE_CRUISE_VELOCITY
                ))
                .build();
    }

    @Override
    public CTREConfiguration getRightSlaveConfig() {
        return new CTREConfiguration.Builder(true).build();
    }

    @Override
    public CTREConfiguration getElevatorConfig() {
        return new CTREConfiguration.Builder(true)
                .setPrimarySensor(FeedbackDevice.CTRE_MagEncoder_Relative)
                .setSensorPhase(true)
                // Forward Gains
                .setPrimaryGains(new ClosedLoopGains(
                        8,
                        0,
                        0,
                        super.getElevatorTransmission().getKf(),
                        1000,
                        1000
                ))
                // Reverse Gains
                .setAuxGains(new ClosedLoopGains(
                        8,
                        0,
                        0,
                        super.getElevatorTransmission().getKf(),
                        1000,
                        1000
                ))
                .build();
    }

    @Override
    public CTREConfiguration getLateralDriveConfig() {
        return new CTREConfiguration.Builder(true)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(
                        Constants.PRACTICE_LATERAL_DRIVE_KP,
                        0,
                        Constants.PRACTICE_LATERAL_DRIVE_KD,
                        super.getLateralDriveTransmission().getKf(),
                        Constants.PRACTICE_LATERAL_DRIVE_ACCELERATION,
                        Constants.PRACTICE_LATERAL_DRIVE_CRUISE_VELOCITY
                ))
                .build();
    }

    @Override
    public CTREConfiguration getManipulatorArmMasterConfig() {
        return new CTREConfiguration.Builder(false)
                .setPrimarySensor(FeedbackDevice.Analog)
                .setSensorPhase(true)
                .setPrimaryGains(new ClosedLoopGains(
                        5,
                        0,
                        0,
                        super.getManipulatorArmTransmission().getKf(),
                        600,
                        600
                ))
                .setAuxGains(new ClosedLoopGains(
                        6,
                        0,
                        0,
                        super.getManipulatorArmTransmission().getKf(),
                        600,
                        600
                ))
                .build();
    }

    @Override
    public CTREConfiguration getManipulatorArmSlaveConfig() {
        return new CTREConfiguration.Builder(false).build();
    }

}
