package com.team175.robot.util.actions;

import edu.wpi.first.hal.NotifierJNI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BooleanSupplier;

/**
 * An improved version of the WPILib Notifier that supports the ability to run periodically until a conditional is true.
 *
 * TODO: Check if BooleanSupplier is necessary
 * TODO: Learn about locks and threads
 *
 * @author Arvind
 * @see Notifier
 */
public class Runner implements AutoCloseable {

    /**
     * The lock for the process information.
     */
    private final ReentrantLock mLock;
    /**
     * The C pointer to the notifier object. We don't use it directly, it is just passed to the JNI bindings.
     */
    private final AtomicInteger mNotifier;
    /**
     * The logger to log different events and errors.
     */
    private final Logger mLogger;
    /**
     * The thread waiting on the HAL alarm.
     */
    private Thread mThread;
    /**
     * The time, in microseconds, at which the corresponding handler should be called. Has the same zero as
     * Utility.getFPGATime().
     */
    private double mExpTime;
    /**
     * The handler passed in by the user which should be called at the appropriate interval.
     */
    private Runnable mHandler;
    /**
     * Whether we are calling the handler just once or periodically.
     */
    private boolean mIsPeriodic;
    /**
     * If periodic, the period of the calling; if just once, stores how long it is until we call the handler.
     */
    private double mPeriod;

    /**
     * Create a Runner for timer event notification.
     *
     * @param run
     *         The handler that is called at the notification time which is set
     *         using StartSingle or StartPeriodic.
     * @param condition
     *         The condition that is checked periodically and runs the thread until it is true.
     */
    public Runner(Runnable run, BooleanSupplier condition) {
        mHandler = run;
        mLock = new ReentrantLock();
        mNotifier = new AtomicInteger();
        mLogger = LoggerFactory.getLogger(getClass().getSimpleName());
        mNotifier.set(NotifierJNI.initializeNotifier());

        mThread = new Thread(() -> {
            while (!condition.getAsBoolean() || !Thread.interrupted()) {
                int notifier = mNotifier.get();
                if (notifier == 0) {
                    break;
                }
                if (NotifierJNI.waitForNotifierAlarm(notifier) == 0) {
                    break;
                }

                Runnable handler;
                mLock.lock();
                try {
                    handler = mHandler;
                    if (mIsPeriodic) {
                        mExpTime += mPeriod;
                        updateAlarm();
                    } else {
                        // need to update the alarm to cause it to wait again
                        updateAlarm((long) -1);
                    }
                } finally {
                    mLock.unlock();
                }

                if (handler != null) {
                    handler.run();
                }
            }
        });
        mThread.setName("Notifier");
        mThread.setDaemon(true);
        mThread.setUncaughtExceptionHandler((thread, error) -> {
            Throwable cause = error.getCause();
            if (cause != null) {
                error = cause;
            }
            mLogger.error("Unhandled exception: {}", error.toString(), error);
            mLogger.error("The loopFunc() method (or methods called by it) should have handled the exception above.");
            /*DriverStation.reportError("Unhandled exception: " + error.toString(), error.getStackTrace());
            DriverStation.reportError(
                    "The loopFunc() method (or methods called by it) should have handled "
                            + "the exception above.", false);*/

        });
        mThread.start();
    }

    public Runner(Runnable run) {
        this(run, () -> false);
    }

    /*@Override
    @SuppressWarnings("NoFinalizer")
    protected void finalize() {
        close();
    }*/

    @Override
    public void close() {
        int handle = mNotifier.getAndSet(0);
        if (handle == 0) {
            return;
        }
        NotifierJNI.stopNotifier(handle);
        // Join the thread to ensure the handler has exited.
        if (mThread.isAlive()) {
            try {
                mThread.interrupt();
                mThread.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        NotifierJNI.cleanNotifier(handle);
        mThread = null;
    }

    /**
     * Update the alarm hardware to reflect the next alarm.
     *
     * @param triggerTime
     *         the time at which the next alarm will be triggered
     */
    private void updateAlarm(long triggerTime) {
        int notifier = mNotifier.get();
        if (notifier == 0) {
            return;
        }
        NotifierJNI.updateNotifierAlarm(notifier, triggerTime);
    }

    /**
     * Update the alarm hardware to reflect the next alarm.
     */
    private void updateAlarm() {
        updateAlarm((long) (mExpTime * 1e6));
    }

    /**
     * Change the handler function.
     *
     * @param handler
     *         Handler
     */
    public void setHandler(Runnable handler) {
        mLock.lock();
        try {
            mHandler = handler;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Register for single event notification. A timer event is queued for a single
     * event after the specified delay.
     *
     * @param delay
     *         Seconds to wait before the handler is called.
     */
    public void startSingle(double delay) {
        mLock.lock();
        try {
            mIsPeriodic = false;
            mPeriod = delay;
            mExpTime = RobotController.getFPGATime() * 1e-6 + delay;
            updateAlarm();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Register for periodic event notification. A timer event is queued for
     * periodic event notification. Each time the interrupt occurs, the event will
     * be immediately requeued for the same time interval.
     *
     * @param period
     *         Period in seconds to call the handler starting one period after
     *         the call to this method.
     */
    public void startPeriodic(double period) {
        mLock.lock();
        try {
            mIsPeriodic = true;
            mPeriod = period;
            mExpTime = RobotController.getFPGATime() * 1e-6 + period;
            updateAlarm();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Stop timer events from occurring. Stop any repeating timer events from
     * occurring. This will also remove any single notification events from the
     * queue. If a timer-based call to the registered handler is in progress, this
     * function will block until the handler call is complete.
     */
    public void stop() {
        NotifierJNI.cancelNotifierAlarm(mNotifier.get());
    }

}
