package com.team175.robot.util.drivers;

import com.team175.robot.Constants;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * A wrapper that treats a double-acting solenoid as a single-acting one.
 *
 * @author Arvind
 */
public class SimpleDoubleSolenoid {

    private DoubleSolenoid mSolenoid;

    public SimpleDoubleSolenoid(int forwardChannel, int reverseChannel, boolean isOnPCMTwo) {
        mSolenoid = new DoubleSolenoid(isOnPCMTwo ? Constants.PCM_NUMBER_TWO_ID : Constants.PCM_NUMBER_ONE_ID,
                forwardChannel, reverseChannel);
    }

    public SimpleDoubleSolenoid(int forwardChannel, int reverseChannel) {
        this(forwardChannel, reverseChannel, false);
    }

    public void set(boolean enable) {
        mSolenoid.set(enable ? Value.kForward : Value.kReverse);
    }

    public boolean get() {
        return mSolenoid.get() == Value.kForward;
    }

}
