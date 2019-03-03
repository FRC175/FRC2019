package com.team175.robot.profiles;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.Constants;
import com.team175.robot.util.CTREConfiguration;
import com.team175.robot.util.ClosedLoopGains;

public class CompetitionRobot extends RobotProfile {

    @Override
    public CTREConfiguration getLeftMasterConfig() {
        return new CTREConfiguration.Builder(false)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(
                        Constants.COMPETITION_LEFT_DRIVE_KP,
                        0,
                        Constants.COMPETITION_LEFT_DRIVE_KD,
                        super.getDriveTransmission().getKf(),
                        Constants.COMPETITION_LEFT_DRIVE_ACCELERATION,
                        Constants.COMPETITION_LEFT_DRIVE_CRUISE_VELOCITY
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
                        Constants.COMPETITION_RIGHT_DRIVE_KP,
                        0,
                        Constants.COMPETITION_RIGHT_DRIVE_KD,
                        super.getDriveTransmission().getKf(),
                        Constants.COMPETITION_RIGHT_DRIVE_ACCELERATION,
                        Constants.COMPETITION_RIGHT_DRIVE_CRUISE_VELOCITY
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
                .setPrimaryGains(new ClosedLoopGains(
                        Constants.COMPETITION_ELEVATOR_KP,
                        0,
                        Constants.COMPETITION_ELEVATOR_KD,
                        super.getElevatorTransmission().getKf(),
                        Constants.COMPETITION_ELEVATOR_ACCELERATION,
                        Constants.COMPETITION_ELEVATOR_CRUISE_VELOCITY
                ))
                .build();
    }

    @Override
    public CTREConfiguration getLateralDriveConfig() {
        return new CTREConfiguration.Builder(false)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(
                        Constants.COMPETITION_LATERAL_DRIVE_KP,
                        0,
                        Constants.COMPETITION_LATERAL_DRIVE_KD,
                        super.getLateralDriveTransmission().getKf(),
                        Constants.COMPETITION_LATERAL_DRIVE_ACCELERATION,
                        Constants.COMPETITION_LATERAL_DRIVE_CRUISE_VELOCITY
                ))
                .build();
    }

    @Override
    public CTREConfiguration getManipulatorArmMasterConfig() {
        return new CTREConfiguration.Builder(false)
                .setPrimarySensor(FeedbackDevice.Analog)
                .setPrimaryGains(new ClosedLoopGains(
                        Constants.COMPETITION_MANIPULATOR_ARM_KP,
                        0,
                        Constants.COMPETITION_MANIPULATOR_ARM_KD,
                        super.getManipulatorArmTransmission().getKf(),
                        Constants.COMPETITION_MANIPULATOR_ARM_ACCELERATION,
                        Constants.COMPETITION_MANIPULATOR_ARM_CRUISE_VELOCITY
                ))
                .build();
    }

    @Override
    public CTREConfiguration getManipulatorArmSlaveConfig() {
        return new CTREConfiguration.Builder(false).build();
    }

}
