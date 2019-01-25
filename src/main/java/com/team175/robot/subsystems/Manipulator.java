package com.team175.robot.subsystems;

/**
 * @author Arvind
 */
public class Manipulator extends AldrinSubsystem {

    // Singleton Instance
    private static Manipulator sInstance;

    public static Manipulator getInstance() {
        if (sInstance == null) {
            sInstance = new Manipulator();
        }

        return sInstance;
    }

    @Override
    protected void initDefaultCommand() {

    }

}
