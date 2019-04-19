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
                        super.getDriveTransmission().getVelocity(), super.getDriveTransmission().getVelocity()))
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
                        super.getDriveTransmission().getVelocity(), super.getDriveTransmission().getVelocity()))
                .setAuxGains(new ClosedLoopGains(0.1, 0, 0, 0,
                        super.getDriveTransmission().getVelocity(), super.getDriveTransmission().getVelocity()))
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
                .setPrimaryGains(new ClosedLoopGains(12, 0, 24, super.getElevatorTransmission().getKf(),
                        super.getElevatorTransmission().getVelocity(), super.getElevatorTransmission().getVelocity()
                ))
                // Reverse Gains
                .setAuxGains(new ClosedLoopGains(3, 0, 6, super.getElevatorTransmission().getKf(),
                        super.getElevatorTransmission().getVelocity(), super.getElevatorTransmission().getVelocity()
                ))
                .build();
    }

    @Override
    public CTREConfiguration getLateralDriveConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(0.1, 0, 0, super.getLateralDriveTransmission().getKf(),
                        super.getLateralDriveTransmission().getVelocity(), super.getLateralDriveTransmission().getVelocity()))
                .build();
    }

    @Override
    public CTREConfiguration getFrontLiftConfig() {
        return new CTREConfiguration.Builder()
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setPrimaryGains(new ClosedLoopGains(0.1, 0, 0, super.getLiftTransmission().getKf(),
                        super.getLiftTransmission().getVelocity(), super.getLiftTransmission().getVelocity()))
                .build();
    }

    @Override
    public CTREConfiguration getRearLiftConfig() {
        return new CTREConfiguration.Builder()
                // .setInverted(true)
                .setPrimarySensor(FeedbackDevice.QuadEncoder)
                .setSensorPhase(true)
                .setPrimaryGains(new ClosedLoopGains(0.1, 0, 0, super.getLiftTransmission().getKf(),
                        super.getLiftTransmission().getVelocity(), super.getLiftTransmission().getVelocity()))
                .build();
    }

    @Override
    public CTREConfiguration getManipulatorArmMasterConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .setPrimarySensor(FeedbackDevice.Analog)
                .setSensorPhase(true)
                // Forward Gains
                .setPrimaryGains(new ClosedLoopGains(5, 0, 10, super.getManipulatorArmTransmission().getKf(),
                        super.getManipulatorArmTransmission().getVelocity(), super.getManipulatorArmTransmission().getVelocity()))
                // Reverse Gains
                .setAuxGains(new ClosedLoopGains(125, 0, 250, super.getManipulatorArmTransmission().getKf(),
                        super.getManipulatorArmTransmission().getVelocity(), super.getManipulatorArmTransmission().getVelocity()))
                // .setForwardSoftLimit(100)
                .build();
    }

    @Override
    public CTREConfiguration getManipulatorArmSlaveConfig() {
        return new CTREConfiguration.Builder()
                .setInverted(true)
                .build();
    }

}
