package com.team175.robot.subsystems;

/**
 * @author Arvind
 */
public class Elevator extends AldrinSubsystem {

    // Singleton Instance
    private static Elevator sInstance;

    public static Elevator getInstance() {
        if (sInstance == null) {
            sInstance = new Elevator();
        }

        return sInstance;
    }

    @Override
    protected void initDefaultCommand() {
    }

}
