package com.team175.robot.util;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.NotifierJNI;
import edu.wpi.first.wpilibj.IterativeRobotBase;
import edu.wpi.first.wpilibj.RobotController;

/**
 * FastTimedRobot is the same as TimedRobot except the default refresh rate
 * of 20 ms is changed to 10 ms.
 *
 * @author Arvind
 */
public class FastTimedRobot extends IterativeRobotBase {
    // The refresh rate
    public static final double kDefaultPeriod = 0.01;

    // The C pointer to the notifier object. We don't use it directly, it is
    // just passed to the JNI bindings.
    private final int m_notifier = NotifierJNI.initializeNotifier();

    // The absolute expiration time
    private double m_expirationTime;

    /**
     * Constructor for FastTimedRobot.
     */
    protected FastTimedRobot() {
        this(kDefaultPeriod);
    }

    /**
     * Constructor for FastTimedRobot.
     *
     * @param period
     *         Period in seconds.
     */
    protected FastTimedRobot(double period) {
        super(period);

        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_Timed);
    }

    @Override
    @SuppressWarnings("NoFinalizer")
    protected void finalize() {
        NotifierJNI.stopNotifier(m_notifier);
        NotifierJNI.cleanNotifier(m_notifier);
    }

    /**
     * Provide an alternate "main loop" via startCompetition().
     */
    @Override
    @SuppressWarnings("UnsafeFinalization")
    public void startCompetition() {
        robotInit();

        // Tell the DS that the robot is ready to be enabled
        HAL.observeUserProgramStarting();

        m_expirationTime = RobotController.getFPGATime() * 1e-6 + m_period;
        updateAlarm();

        // Loop forever, calling the appropriate mode-dependent function
        while (true) {
            long curTime = NotifierJNI.waitForNotifierAlarm(m_notifier);
            if (curTime == 0) {
                break;
            }

            m_expirationTime += m_period;
            updateAlarm();

            loopFunc();
        }
    }

    /**
     * Get time period between calls to Periodic() functions.
     */
    public double getPeriod() {
        return m_period;
    }

    /**
     * Update the alarm hardware to reflect the next alarm.
     */
    @SuppressWarnings("UnsafeFinalization")
    private void updateAlarm() {
        NotifierJNI.updateNotifierAlarm(m_notifier, (long) (m_expirationTime * 1e6));
    }
}

