package com.team175.robot.util.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team175.robot.Constants;

/**
 * @author Arvind
 */
public class AldrinTalonSRX extends TalonSRX {

    public AldrinTalonSRX(int portNum) {
        super(portNum);
    }

	public ErrorCode config_kP(double value) {
		return super.config_kP(Constants.SLOT_INDEX, value, Constants.TIMEOUT_MS);
	}

	public ErrorCode config_kI(double value) {
		return super.config_kI(Constants.SLOT_INDEX, value, Constants.TIMEOUT_MS);
	}

	public ErrorCode config_kD(double value) {
		return super.config_kD(Constants.SLOT_INDEX, value, Constants.TIMEOUT_MS);
	}

	public ErrorCode config_kF(double value) {
		return super.config_kF(Constants.SLOT_INDEX, value, Constants.TIMEOUT_MS);
	}

	public ErrorCode config_aux_kP(double value) {
		return super.config_kP(Constants.AUX_SLOT_INDEX, value, Constants.TIMEOUT_MS);
	}

	public ErrorCode config_aux_kI(double value) {
		return super.config_kI(Constants.AUX_SLOT_INDEX, value, Constants.TIMEOUT_MS);
	}

	public ErrorCode config_aux_kD(double value) {
		return super.config_kD(Constants.AUX_SLOT_INDEX, value, Constants.TIMEOUT_MS);
	}

	public ErrorCode config_aux_kF(double value) {
		return super.config_kF(Constants.AUX_SLOT_INDEX, value, Constants.TIMEOUT_MS);
	}

	public void setBrakeMode(boolean on) {
		super.setNeutralMode(on ? NeutralMode.Brake : NeutralMode.Coast);
    }

    /*public void setPosition(int position) {
        super.set(ControlMode.Position, position);
    }
    
    public void setPower(double power) {
        super.set(ControlMode.PercentOutput, power);
    }

    public double getPower() {
        return super.getMotorOutputPercent();
    }

    public int positionToMove() {
        return getSelectedSensorPosition();
    }

    public void setPIDF(double kF, double kP, double kI, double kD) {
        config_kF(kF);
        config_kP(kP);
        config_kI(kI);
        config_kD(kD);
    }*/

}