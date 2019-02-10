package com.team175.robot.auto;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;

public class TrajectoryFollower {

    private TalonSRX mMaster;
    private TalonSRX mFollower;
    private PigeonIMU mPigeon;

    private BufferedTrajectoryPointStream mBuffer;

    public TrajectoryFollower(TalonSRX master, TalonSRX follower, PigeonIMU pigeon) {
        mMaster = master;
        mFollower = follower;
        mPigeon = pigeon;

        // Configure follower polling rate at 5 ms
        mFollower.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.TIMEOUT_MS);
        mFollower.setSensorPhase(true);

        // Config master primary sensor to be average of master and follower
        mMaster.configRemoteFeedbackFilter(mMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor,
                Constants.SLOT_INDEX, Constants.TIMEOUT_MS);
        mMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.TIMEOUT_MS);
        mMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, Constants.TIMEOUT_MS);
        mMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, Constants.SLOT_INDEX, Constants.TIMEOUT_MS);
        mMaster.configSelectedFeedbackCoefficient(0.5, Constants.SLOT_INDEX, Constants.TIMEOUT_MS);

        // Config master secondary sensor to be pigeon
        mMaster.configRemoteFeedbackFilter(mPigeon.getDeviceID(), RemoteSensorSource.Pigeon_Yaw,
                Constants.AUX_SLOT_INDEX, Constants.TIMEOUT_MS);
        mMaster.configSelectedFeedbackCoefficient((3600.0 / 8192.0), Constants.AUX_SLOT_INDEX, Constants.TIMEOUT_MS);
    }

    public void init() {
        mMaster.clearMotionProfileTrajectories();
        mMaster.changeMotionControlFramePeriod(5);
        mMaster.clearMotionProfileHasUnderrun(Constants.TIMEOUT_MS);

        mFollower.clearMotionProfileTrajectories();
        mFollower.changeMotionControlFramePeriod(5);
        mFollower.clearMotionProfileHasUnderrun(Constants.TIMEOUT_MS);
    }

    public void follow(Trajectory trajectory) {
        loadBuffer(trajectory);

        mFollower.follow(mMaster, FollowerType.AuxOutput1);
        mFollower.startMotionProfile(mBuffer, 10, ControlMode.MotionProfileArc);
    }

    private void loadBuffer(Trajectory trajectory) {
        TrajectoryPoint point = new TrajectoryPoint();

        double[][] path = trajectory.getPath();
        int direction = trajectory.isReversed() ? -1 : 1;

        for (int i = 0; i < path.length; i++) {
            point.position = direction * path[i][0];
            point.velocity = direction * path[i][1];

            point.timeDur = (int) path[i][2];

            // Angle
            // Pigeon's max angle is 3600
            point.auxiliaryPos = 10.0 * path[i][3];
            point.auxiliaryVel = 0.0;
            point.auxiliaryArbFeedFwd = 0.0;

            point.profileSlotSelect0 = Constants.SLOT_INDEX;
            point.profileSlotSelect1 = Constants.AUX_SLOT_INDEX;
            point.zeroPos = false;
            point.isLastPoint = (i + 1) == path.length;
            point.useAuxPID = true;

            mBuffer.Write(point);
        }
    }

    public boolean isFinished() {
        return mMaster.isMotionProfileFinished();
    }

    public void reset() {
        mMaster.clearMotionProfileTrajectories();
        mMaster.clearMotionProfileHasUnderrun(Constants.TIMEOUT_MS);
        mMaster.changeMotionControlFramePeriod(10);
        mMaster.set(ControlMode.PercentOutput, 0);

        mFollower.clearMotionProfileTrajectories();
        mFollower.clearMotionProfileHasUnderrun(Constants.TIMEOUT_MS);
        mFollower.changeMotionControlFramePeriod(Constants.TIMEOUT_MS);
        mFollower.set(ControlMode.PercentOutput, 0);
    }

}
