package com.team175.robot.util.tuning;

/**
 * Represents a message to be sent over a network. Can be sent to NetworkTables, SmartDashboard, or over ZMQ Socket.
 *
 * TODO: Think more about this.
 *
 * @author Arvind
 * @param <T>
 */
public class Message<T> {

    private T mData;

    public Message(T data) {
        mData = data;
    }

    public T getMessage() {
        return mData;
    }

}
