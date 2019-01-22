package com.team175.robot.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class AldrinVictorSPX extends VictorSPX {

    public AldrinVictorSPX(int portNum) {
        super(portNum);
    }

    public ErrorCode config_kF(double value) {
        return super.config_kF(Constants.kSlotIdx, value, Constants.kTimeoutMs);
    }

    public ErrorCode config_kP(double value) {
        return super.config_kP(Constants.kSlotIdx, value, Constants.kTimeoutMs);
    }

    public ErrorCode config_kI(double value) {
        return super.config_kI(Constants.kSlotIdx, value, Constants.kTimeoutMs);
    }

    public ErrorCode config_kD(double value) {
        return super.config_kD(Constants.kSlotIdx, value, Constants.kTimeoutMs);
    }

    public ErrorCode setSelectedSensorPosition(int position) {
        return super.setSelectedSensorPosition(position, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    }

    public int getSelectedSensorPosition() {
        return super.getSelectedSensorPosition(Constants.kPIDLoopIdx);
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

    public int getPosition() {
        return getSelectedSensorPosition();
    }

    public void setPIDF(double kF, double kP, double kI, double kD) {
        config_kF(kF);
        config_kP(kP);
        config_kI(kI);
        config_kD(kD);
    }*/

}