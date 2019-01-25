package com.team175.robot.subsystems;

/**
 * @author Arvind
 */
public class Lift extends AldrinSubsystem {

    // Singleton Instance
    private static Lift sInstance;

    public static Lift getInstance() {
        if (sInstance == null) {
            sInstance = new Lift();
        }

        return sInstance;
    }


    @Override
    protected void initDefaultCommand() {

    }

}
