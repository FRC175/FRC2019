package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * A wrapper that treats a double-acting solenoid as a single-acting one.
 *
 * @author Arvind
 */
public class SimpleDoubleSolenoid {

    private DoubleSolenoid mSolenoid;

    public SimpleDoubleSolenoid(int forwardChannel, int reverseChannel) {
        mSolenoid = new DoubleSolenoid(forwardChannel, reverseChannel);
    }

    public SimpleDoubleSolenoid(int forwardChannel, int reverseChannel, int pcmID) {
        mSolenoid = new DoubleSolenoid(pcmID, forwardChannel, reverseChannel);
    }

    public void set(boolean enable) {
        mSolenoid.set(enable ? Value.kForward : Value.kReverse);
    }

    public boolean get() {
        return mSolenoid.get() == Value.kForward;
    }

}
