package com.team175.robot.util.net;

/**
 * Represents a message to be sent over a network. Can be sent to a NetworkTable, the SmartDashboard, or over ZMQ, TCP,
 * or UDP connection.
 *
 * TODO: Think more about this.
 * TODO: Determine if generic should be used.
 *
 * @author Arvind
 * @param <T>
 */
/*public class Message<T> {

    private T mData;

    public Message(T data) {
        mData = data;
    }

    public T getMessage() {
        return mData;
    }

}*/
public interface Message<T> {

    T getMessage();

}
