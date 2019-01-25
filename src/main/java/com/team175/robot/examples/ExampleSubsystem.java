/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot.examples;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The following java class contains example code that can help you understand
 * how to create a subsystem.
 * 
 * ROBOT CODE CONVENTIONS:
 * - When dealing with private instance variables, there must be a prefix of m.
 *      EX: private int mInteger;
 * - When dealing with private static variables, there must be a prefix of s.
 *      EX: private static int sInteger;
 * - When dealing with all static final variables, there must be a prefix of k.
 *      EX: private final int kInteger;
 * - When dealing with all other variables, there is nothing special.
 *      EX: public int integer;
 * 
 * STEPS TO MAKE A SUBSYSTEM:
 * 1.) Create a constructor to configure all variables.
 * 2.) Override initDefaultCommand() method.
 * 3.) Create custom methods.
 *
 * @author Arvind
 * @see Subsystem
 */
public class ExampleSubsystem extends Subsystem {

    // Subsystem Instance (Used in Singleton Design Pattern; Optional)
    private static ExampleSubsystem sInstance;

    /**
     * OPTIONAL STEP
     * 
     * DESCRIPTION:
     * A method used to return the one and only instance of the Example Subsystem. This is used in to comply with the
     * Singleton design pattern.
     */
    public static ExampleSubsystem getInstance() {
        if (sInstance == null) {
            try {
                sInstance = new ExampleSubsystem();
            } catch (Exception e) {
                // DriverStation.reportError("Example Subsystem failed to instantiate.\n" + e, true);
            }
        }

        return sInstance;
    }

    /**
     * STEP 1:
     * 
     * DESCRIPTION:
     * A method used to instantiate data fields and objects in the subsystem when created in the Robot.java file.
     * 
     * NOTE:
     * Constructor can be set to private if Singleton design pattern is used.
     */
    private ExampleSubsystem() {
        // Since the constructor in Subsystem.java has a constructor with no parameters, the super command is not
        // required to be called here.

        /* Instantiations */
        // Below, the electronic and pneumatic objects in this subsystem are instantiated just like how this subsystem
        // will be instantiated in Robot.java.

        // You can further configure the hardware here.
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    /**
     * STEP 2:
     * 
     * DESCRIPTION:
     * A method that sets the default command for the SRX subsystem.
     */
    @Override
    protected void initDefaultCommand() {
        // Usually left blank.
    }

    // STEP 3:
    // You can create custom methods that use the electronics and pneumatics in the subsystem that can be used by
    // different commands.
    // EX: public void setPower() {}
    
}
