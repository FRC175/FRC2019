package com.team175.robot.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.team175.robot.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A object used to hold a CTRE Motor Controller's configuration settings in order to adapt to the different hardware
 * changes across the competition and practice robots. A builder is used in order to make this object immutable.
 *
 * @author Arvind
 */
public class CTREConfiguration {

    private boolean mIsInverted;
    private boolean mSensorPhase;
    private int mForwardSoftLimit;
    private int mReverseSoftLimit;
    private FeedbackDevice mPrimarySensor;
    /*private FeedbackDevice mAuxSensor;*/
    private ClosedLoopGains mPrimaryGains;
    private ClosedLoopGains mAuxGains;

    private static final Logger sLogger = LoggerFactory.getLogger(CTREConfiguration.class);

    public static class Builder {

        // Required
        private boolean isInverted;

        // Optional
        private boolean sensorPhase;
        private int forwardSoftLimit;
        private int reverseSoftLimit;
        private FeedbackDevice primarySensor;
        /*private FeedbackDevice auxSensor;*/
        private ClosedLoopGains primaryGains;
        private ClosedLoopGains auxGains;

        public Builder(boolean isInverted) {
            this.isInverted = isInverted;
            this.sensorPhase = false;
            // TODO: Check soft limit initial values
            this.forwardSoftLimit = 0;
            this.reverseSoftLimit = 0;
            this.primarySensor = null;
            /*this.auxSensor = null;*/
            this.primaryGains = null;
            this.auxGains = null;
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

        /*public Builder setAuxSensor(FeedbackDevice auxSensor) {
            this.auxSensor = auxSensor;
            return this;
        }*/

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
        /*mAuxSensor = b.auxSensor;*/
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
            CTREDiagnostics.checkCommand(bmc.configSelectedFeedbackSensor(config.mPrimarySensor, Constants.SLOT_INDEX, Constants.TIMEOUT_MS),
                    "Failed to config " + name + " encoder!");
        }
        /*if (config.mAuxSensor != null) {
            CTREDiagnostics.checkCommand(bmc.configSelectedFeedbackSensor(config.mAuxSensor, Constants.AUX_SLOT_INDEX, Constants.TIMEOUT_MS),
                    "Failed to config " + name + " aux sensor!");
        }*/

        if (config.mPrimaryGains != null) {
            setPrimaryGains(bmc, config.mPrimaryGains, name);
        }
        if (config.mAuxGains != null) {
            setAuxGains(bmc, config.mAuxGains, name);
        }
    }

    public static ClosedLoopGains getGainsFromConfig(CTREConfiguration config, boolean isPrimaryGains) {
        return isPrimaryGains ? config.mPrimaryGains : config.mAuxGains;
    }

    public static void setGains(BaseMotorController bmc, ClosedLoopGains gains, int slotIdx, String name) {
        CTREDiagnostics.checkCommand(bmc.config_kP(slotIdx, gains.getKp()),
                "Failed to config " + name + " kP!");
        CTREDiagnostics.checkCommand(bmc.config_kI(slotIdx, gains.getKi()),
                "Failed to config " + name + " kI!");
        CTREDiagnostics.checkCommand(bmc.config_kD(slotIdx, gains.getKd()),
                "Failed to config " + name + " kD!");
        CTREDiagnostics.checkCommand(bmc.config_kF(slotIdx, gains.getKf()),
                "Failed to config " + name + " kF!");
        CTREDiagnostics.checkCommand(bmc.configMotionAcceleration(slotIdx, gains.getAcceleration()),
                "Failed to config " + name + " acceleration!");
        CTREDiagnostics.checkCommand(bmc.configMotionCruiseVelocity(slotIdx, gains.getCruiseVelocity()),
                "Failed to config " + name + " cruise velocity!");
    }

    public static void setPrimaryGains(BaseMotorController bmc, ClosedLoopGains gains, String name) {
        setGains(bmc, gains, Constants.SLOT_INDEX, name);
    }

    public static void setAuxGains(BaseMotorController bmc, ClosedLoopGains gains, String name) {
        setGains(bmc, gains, Constants.AUX_SLOT_INDEX, name);
    }

}
