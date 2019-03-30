package com.team175.robot.util.drivers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Holds Limelight NetworkTable data in a convenient wrapper.
 *
 * @author Arvind
 */
public class Limelight {

    private final NetworkTable mTable;

    /**
     * Constructs a default limelight whose network configuration is unaltered.
     */
    public Limelight() {
        this("limelight");
    }

    /**
     * Constructs a limelight for a specific table name if more than one limelight is attached or if NetworkTables name
     * is altered in configuration.
     *
     * @param tableName
     *         Name of limelight NetworkTable
     */
    public Limelight(String tableName) {
        mTable = NetworkTableInstance.getDefault().getTable(tableName);
    }

    public void setLEDState(boolean enable) {
        mTable.getEntry("ledMode").setNumber(enable ? 3 : 1);
    }

    public void blinkLED() {
        mTable.getEntry("ledMode").setNumber(2);
    }

    public void setCameraMode(boolean isDriverMode) {
        mTable.getEntry("camMode").setNumber(isDriverMode ? 1 : 0);
    }

    public void setPipeline(int pipelineNum) {
        mTable.getEntry("pipeline").setNumber(pipelineNum);
    }

    public boolean isTargetDetected() {
        return mTable.getEntry("tv").getDouble(0) == 1;
    }

    public double getHorizontalOffset() {
        return mTable.getEntry("tx").getDouble(0);
    }

    public double getVerticalOffset() {
        return mTable.getEntry("ty").getDouble(0);
    }

    public double getTargetArea() {
        return mTable.getEntry("ta").getDouble(0);
    }

    public double getRotation() {
        return mTable.getEntry("ts").getDouble(0);
    }

    public int getPipelineNumber() {
        return (int) mTable.getEntry("getpipe").getDouble(0);
    }

    // Replace with object to hold variables
    public double[] get3DVariables() {
        return mTable.getEntry("camtran").getDoubleArray(new double[] {});
    }

}
