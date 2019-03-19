package com.team175.robot.loops;

/**
 * An ever-so-slightly modified version of Team254's Loop object.
 *
 * Interface for loops, which are a set of routines that run periodically in the robot code (e.g., periodic gyroscope
 * calibration, etc.)
 *
 * @author Team254
 */
public interface Loop {

    void start();

    void loop();

    void stop();

}
