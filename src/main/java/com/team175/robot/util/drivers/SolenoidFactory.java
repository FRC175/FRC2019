package com.team175.robot.util.drivers;

import com.team175.robot.Constants;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * A static factory class that allows for the construction of a Solenoid or DoubleSolenoid that detects whether the
 * channel is located on the first or second PCM.
 *
 * @author Arvind
 */
public class SolenoidFactory {

    // Prevent SolenoidFactory from being instantiated
    private SolenoidFactory() {
    }

    public static Solenoid getSolenoid(int channel) {
        return new Solenoid(channel > 7 ? channel - 10 : channel, getPCMID(channel));
    }

    public static SimpleDoubleSolenoid getDoubleSolenoid(int forwardChannel, int reverseChannel) {
        return null;
    }

    private static int getRealChannel(int channel) {
        return -1;
    }

    private static int getPCMID(int channel) {
        return channel > 7 ? Constants.PCM_NUMBER_TWO_ID : Constants.PCM_NUMBER_ONE_ID;
    }

}
