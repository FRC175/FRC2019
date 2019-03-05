package com.team175.robot.util.drivers;

import com.team175.robot.Constants;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * @author Arvind
 */
public class RegulatoryHardware {

    private Compressor mCompressor;
    private PowerDistributionPanel mPDP;

    private static RegulatoryHardware sInstance;

    public static RegulatoryHardware getInstance() {
        if (sInstance == null) {
            sInstance = new RegulatoryHardware();
        }

        return sInstance;
    }

    private RegulatoryHardware() {
        mCompressor = new Compressor();
        mPDP = new PowerDistributionPanel(Constants.PDP_PORT);
    }

    public void startCompressor() {
        mCompressor.start();
    }

    public void stopCompressor() {
        mCompressor.stop();
    }

    public double getCurrentForPDPChannel(int channel) {
        return mPDP.getCurrent(channel);
    }

}
