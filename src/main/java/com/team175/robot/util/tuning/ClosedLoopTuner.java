package com.team175.robot.util.tuning;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team175.robot.loops.Loop;
import com.team175.robot.loops.Looper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

/**
 * @author Arvind
 */
public final class ClosedLoopTuner {

    // TODO: Make a wrapper around ZMQ messages that can easily send data back and forth between a network
    // TODO: Use the wrapper to send position and time over a network
    // TODO: Use the wrapper to send PID values
    // TODO: Make a python web app that reads position and time values and graphs it
    // TODO: Make python web app update PID values real time
    ;

    private final TalonSRX mTalon;
    private final String mTalonName;
    private final Logger mLogger;
    // private final Looper mLooper;

    private static final double PERIOD = 0.01;

    public ClosedLoopTuner(TalonSRX talon, String talonName) {
        mTalon = talon;
        mTalonName = talonName;
        mLogger = LoggerFactory.getLogger(getClass().getSimpleName());
        // mLooper = new Looper(PERIOD);
    }

    /*private class Server implements Loop {

        @Override
        public void start() {
            mLogger.info("Starting ClosedLoopTuner for {}!", mTalonName);
        }

        @Override
        public void loop() {

        }

        @Override
        public void stop() {

        }

    }*/

}
