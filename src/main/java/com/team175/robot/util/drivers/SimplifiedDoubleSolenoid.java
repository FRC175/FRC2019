package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Treats a double-acting solenoid as a single-acting one.
 *
 * @author Arvind
 */
public class SimplifiedDoubleSolenoid extends DoubleSolenoid {

    public SimplifiedDoubleSolenoid(int forwardChannel, int reverseChannel) {
        super(forwardChannel, reverseChannel);
    }

}
