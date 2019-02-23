package com.team175.robot.util;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;
import com.team175.robot.paths.Path;

/**
 * A helper class to easily configure and follow paths using the Talon SRX MotionProfileArc mode. Code is heavily based
 * off of Team319's BobTrajectory library.
 *
 * @author Arvind
 */
public class PathHelper {

    /**
     * The Talon SRXs
     */
    private TalonSRX mMaster, mFollower;
    /**
     * The Pigeon IMU
     */
    private PigeonIMU mPigeon;
    /**
     * The buffered writer to write path points into the Talon SRXs
     */
    private BufferedTrajectoryPointStream mBuffer;

    /**
     * Constructs a new PathHelper.
     *
     * @param master
     *         The master Talon SRX to follow the path
     * @param follower
     *         The follower Talon SRX to mirror the master
     * @param pigeon
     *         The Pigeon IMU for heading correction
     */
    public PathHelper(TalonSRX master, TalonSRX follower, PigeonIMU pigeon) {
        mMaster = master;
        mFollower = follower;
        mPigeon = pigeon;
        mBuffer = new BufferedTrajectoryPointStream();
    }

    /**
     * Configures Talon SRXs for Motion Profiling.
     */
    public void configTalons() {
        // Configure follower polling rate at 5 ms
        CTREDiagnostics.checkCommand(mFollower.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.TIMEOUT_MS),
                "Failed to set LeftMaster polling rate at 5 ms!");

        // Config master primary sensor to be average of master and follower
        CTREDiagnostics.checkCommand(mMaster.configRemoteFeedbackFilter(mFollower.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor,
                Constants.SLOT_INDEX, Constants.TIMEOUT_MS), "Failed to add LeftMaster encoder as a remote sensor for RightMaster!");
        CTREDiagnostics.checkCommand(mMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.TIMEOUT_MS),
                "Failed to config RightMaster sensor term 0!");
        CTREDiagnostics.checkCommand(mMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, Constants.TIMEOUT_MS),
                "Failed to config RightMaster sensor term 1!");
        CTREDiagnostics.checkCommand(mMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, Constants.SLOT_INDEX,
                Constants.TIMEOUT_MS), "Failed to set RightMaster feedback sensor as sum of both left and right!");
        CTREDiagnostics.checkCommand(mMaster.configSelectedFeedbackCoefficient(0.5, Constants.SLOT_INDEX, Constants.TIMEOUT_MS),
                "Failed to set RightMaster encoder sum feedback coefficient at 0.5!");

        // Config master secondary sensor to be pigeon
        CTREDiagnostics.checkCommand(mMaster.configRemoteFeedbackFilter(mPigeon.getDeviceID(), RemoteSensorSource.Pigeon_Yaw,
                Constants.AUX_SLOT_INDEX, Constants.TIMEOUT_MS), "Failed to config Pigeon as remote sensor for RightMaster!");
        CTREDiagnostics.checkCommand(mMaster.configSelectedFeedbackCoefficient((3600.0 / 8192.0), Constants.AUX_SLOT_INDEX,
                Constants.TIMEOUT_MS), "Failed to config RightMaster pigeon feedback coefficient at 3600.0 / 8192.0.");
    }

    /**
     * Prepares Talon SRXs for path following.
     */
    public void reset() {
        CTREDiagnostics.checkCommand(mMaster.clearMotionProfileTrajectories(),
                "Failed to clear RightMaster MP trajectories!");
        CTREDiagnostics.checkCommand(mMaster.changeMotionControlFramePeriod(5),
                "Failed to set RightMaster motion control frame period to 5 ms!");
        CTREDiagnostics.checkCommand(mMaster.clearMotionProfileHasUnderrun(Constants.TIMEOUT_MS),
                "Failed to clear RightMaster motion profile!");

        CTREDiagnostics.checkCommand(mFollower.clearMotionProfileTrajectories(),
                "Failed to clear LeftMaster MP trajectories!");
        CTREDiagnostics.checkCommand(mFollower.changeMotionControlFramePeriod(5),
                "Failed to set LeftMaster motion control frame period to 5 ms!");
        CTREDiagnostics.checkCommand(mFollower.clearMotionProfileHasUnderrun(Constants.TIMEOUT_MS),
                "Failed to clear LeftMaster motion profile!");
    }

    /**
     * Buffers path into the Talon SRX.
     *
     * @param path
     *         The path to buffer into the Talon SRX
     */
    private void bufferPath(Path path) {
        TrajectoryPoint point = new TrajectoryPoint();

        double[][] points = path.getPoints();
        int direction = path.isReversed() ? -1 : 1;
        int startPosition = mMaster.getSelectedSensorPosition();

        // Clear buffer in case it was used elsewhere
        CTREDiagnostics.checkCommand(mBuffer.Clear(), "Failed to clear buffer!");

        for (int i = 0; i < points.length; i++) {
            // Position and velocity
            point.position = (direction * points[i][0]) + startPosition;
            point.velocity = direction * points[i][1];

            // dt
            point.timeDur = (int) points[i][2];

            // Pigeon is the auxiliary sensor
            point.auxiliaryPos = 10.0 * points[i][3];
            point.auxiliaryVel = 0.0;
            point.auxiliaryArbFeedFwd = 0.0;

            // Other configuration
            point.profileSlotSelect0 = Constants.SLOT_INDEX;
            point.profileSlotSelect1 = Constants.AUX_SLOT_INDEX;
            point.zeroPos = false;
            point.isLastPoint = (i + 1) == points.length;
            point.useAuxPID = true;

            CTREDiagnostics.checkCommand(mBuffer.Write(point), "Failed to write path point #" + i + "to the buffer!");
        }
    }

    /**
     * Starts path following on Talon SRXs.
     *
     * @param path
     *         The path to follow
     */
    public void follow(Path path) {
        bufferPath(path);

        mFollower.follow(mMaster, FollowerType.AuxOutput1);
        CTREDiagnostics.checkCommand(mMaster.startMotionProfile(mBuffer, 10, ControlMode.MotionProfileArc),
                "Failed to start motion profile");
    }

    /**
     * Checks if path has been completed.
     *
     * @return If path is complete
     */
    public boolean isFinished() {
        return mMaster.isMotionProfileFinished();
    }

    /**
     * Exits path following mode and resets Talon SRXs to normal.
     */
    public void stop() {
        reset();

        mMaster.set(ControlMode.PercentOutput, 0);
        mFollower.set(ControlMode.PercentOutput, 0);
    }

}
