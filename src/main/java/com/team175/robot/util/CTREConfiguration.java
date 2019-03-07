package com.team175.robot.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.team175.robot.Constants;
import com.team175.robot.util.tuning.ClosedLoopGains;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A object used to hold a CTRE Motor Controller's configuration settings in order to adapt to the different hardware
 * changes across the competition and practice robots. A builder is used in order to make this object immutable.
 *
 * TODO: Remove setPrimaryGains() and boolean isPrimary to setGains()
 *
 * @author Arvind
 */
public class CTREConfiguration {

    private boolean mIsInverted, mSensorPhase;
    private int mForwardSoftLimit, mReverseSoftLimit;
    private FeedbackDevice mPrimarySensor;
    private ClosedLoopGains mPrimaryGains, mAuxGains;

    private static final Logger sLogger = LoggerFactory.getLogger(CTREConfiguration.class);

    public static class Builder {

        private boolean isInverted, sensorPhase;
        private int forwardSoftLimit, reverseSoftLimit;
        private FeedbackDevice primarySensor;
        private ClosedLoopGains primaryGains, auxGains;

        public Builder() {
            this.isInverted = false;
            this.sensorPhase = false;
            // TODO: Check soft limit initial values
            this.forwardSoftLimit = 0;
            this.reverseSoftLimit = 0;
            this.primarySensor = null;
            this.primaryGains = null;
            this.auxGains = null;
        }

        public Builder setInverted(boolean isInverted) {
            this.isInverted = isInverted;
            return this;
        }

        public Builder setSensorPhase(boolean sensorPhase) {
            this.sensorPhase = sensorPhase;
            return this;
        }

        public Builder setForwardSoftLimit(int forwardSoftLimit) {
            this.forwardSoftLimit = forwardSoftLimit;
            return this;
        }

        public Builder setReverseSoftLimit(int reverseSoftLimit) {
            this.reverseSoftLimit = reverseSoftLimit;
            return this;
        }

        public Builder setPrimarySensor(FeedbackDevice primarySensor) {
            this.primarySensor = primarySensor;
            return this;
        }

        public Builder setPrimaryGains(ClosedLoopGains primaryGains) {
            this.primaryGains = primaryGains;
            return this;
        }

        public Builder setAuxGains(ClosedLoopGains auxGains) {
            this.auxGains = auxGains;
            return this;
        }

        public CTREConfiguration build() {
            return new CTREConfiguration(this);
        }

    }

    private CTREConfiguration(Builder b) {
        mIsInverted = b.isInverted;
        mSensorPhase = b.sensorPhase;
        mForwardSoftLimit = b.forwardSoftLimit;
        mReverseSoftLimit = b.reverseSoftLimit;
        mPrimarySensor = b.primarySensor;
        mPrimaryGains = b.primaryGains;
        mAuxGains = b.auxGains;
    }

    public static void config(BaseMotorController bmc, CTREConfiguration config, String name) {
        bmc.setInverted(config.mIsInverted);
        bmc.setSensorPhase(config.mSensorPhase);

        CTREDiagnostics.checkCommand(bmc.configForwardSoftLimitThreshold(config.mForwardSoftLimit, Constants.TIMEOUT_MS),
                "Failed to config " + name + " forward soft limit!");
        CTREDiagnostics.checkCommand(bmc.configReverseSoftLimitThreshold(config.mReverseSoftLimit, Constants.TIMEOUT_MS),
                "Failed to config " + name + " reverse soft limit!");

        if (config.mPrimarySensor != null) {
            CTREDiagnostics.checkCommand(bmc.configSelectedFeedbackSensor(config.mPrimarySensor, Constants.PRIMARY_GAINS_SLOT, Constants.TIMEOUT_MS),
                    "Failed to config " + name + " encoder!");
        }

        if (config.mPrimaryGains != null) {
            setGains(bmc, config.mPrimaryGains, true, name);
        }
        if (config.mAuxGains != null) {
            setGains(bmc, config.mAuxGains, false, name);
        }
    }

    public static void setGains(BaseMotorController bmc, ClosedLoopGains gains, int gainsSlot, String name) {
        CTREDiagnostics.checkCommand(bmc.config_kP(gainsSlot, gains.getKp(), Constants.TIMEOUT_MS),
                "Failed to config " + name + " kP!");
        CTREDiagnostics.checkCommand(bmc.config_kI(gainsSlot, gains.getKi(), Constants.TIMEOUT_MS),
                "Failed to config " + name + " kI!");
        CTREDiagnostics.checkCommand(bmc.config_kD(gainsSlot, gains.getKd(), Constants.TIMEOUT_MS),
                "Failed to config " + name + " kD!");
        CTREDiagnostics.checkCommand(bmc.config_kF(gainsSlot, gains.getKf(), Constants.TIMEOUT_MS),
                "Failed to config " + name + " kF!");
        CTREDiagnostics.checkCommand(bmc.configMotionAcceleration(gains.getAcceleration(), Constants.TIMEOUT_MS),
                "Failed to config " + name + " acceleration!");
        CTREDiagnostics.checkCommand(bmc.configMotionCruiseVelocity(gains.getCruiseVelocity(), Constants.TIMEOUT_MS),
                "Failed to config " + name + " cruise velocity!");
    }

    public static void setGains(BaseMotorController bmc, ClosedLoopGains gains, boolean isPrimaryGains, String name) {
        setGains(bmc, gains, isPrimaryGains ? Constants.PRIMARY_GAINS_SLOT : Constants.AUX_GAINS_SLOT, name);
    }

    public static ClosedLoopGains getGains(CTREConfiguration config, boolean isPrimaryGains) {
        return isPrimaryGains ? config.mPrimaryGains : config.mAuxGains;
    }

}
