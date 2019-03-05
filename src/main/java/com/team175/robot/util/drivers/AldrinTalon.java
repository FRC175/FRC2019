package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.Talon;

public class AldrinTalon extends Talon {

    private int mPDPChannel;

    public AldrinTalon(int portNum) {
        this(portNum, -1);
    }

    public AldrinTalon(int portNum, int pdpChannel) {
        super(portNum);
        mPDPChannel = pdpChannel;
    }

    public double getPDPCurrent() {
        if (mPDPChannel == -1) {
            throw new UnsupportedOperationException("Talon SR " + super.getChannel() +
                    " is not configured to read PDP current!");
        } else {
            // return RegulatoryHardware.getInstance().getPDP().getCurrent(mPDPChannel);
            return RegulatoryHardware.getInstance().getCurrentForPDPChannel(mPDPChannel);
        }
    }

}
