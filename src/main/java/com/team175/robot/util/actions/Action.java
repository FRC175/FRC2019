package com.team175.robot.util.actions;

/**
 * Represents that can be performed by the robot (e.g., driving in open loop control, moving a subsystem to a certain
 * position, etc.).
 *
 * @author Arvind
 */
public interface Action {

    void start();

    void loop();

    boolean isFinished();

    void stop();

}
