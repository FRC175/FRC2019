package com.team175.robot.profiles;

import com.team175.robot.Constants;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.util.CTREConfiguration;
import com.team175.robot.util.Transmission;

/**
 * @author Arvind
 */
public abstract class RobotProfile {

    public abstract CTREConfiguration getLeftMasterConfig();

    public abstract CTREConfiguration getLeftSlaveConfig();

    public abstract CTREConfiguration getRightMasterConfig();

    public abstract CTREConfiguration getRightSlaveConfig();

    public abstract CTREConfiguration getElevatorConfig();

    public abstract CTREConfiguration getLateralDriveConfig();

    public abstract CTREConfiguration getManipulatorArmMasterConfig();

    public abstract CTREConfiguration getManipulatorArmSlaveConfig();

    public Transmission getDriveTransmission() {
        return new Transmission(Constants.DRIVE_MAX_RPM, Constants.DRIVE_COUNTS_PER_REVOLUTION, Constants.DRIVE_GEAR_RATIO);
    }

    public Transmission getElevatorTransmission() {
        return new Transmission(Constants.ELEVATOR_MAX_RPM, Constants.ELEVATOR_COUNTS_PER_REVOLUTION, Constants.ELEVATOR_GEAR_RATIO);
    }

    public Transmission getLateralDriveTransmission() {
        return new Transmission(Constants.LATERAL_DRIVE_MAX_RPM, Constants.LATERAL_DRIVE_COUNTS_PER_REVOLUTION,
                Constants.LATERAL_DRIVE_GEAR_RATIO);
    }

    public Transmission getManipulatorArmTransmission() {
        return new Transmission(Constants.MANIPULATOR_ARM_MAX_RPM, Constants.MANIPULATOR_ARM_COUNTS_PER_REVOLUTION,
                Constants.MANIPULATOR_ARM_GEAR_RATIO);
    }

}
