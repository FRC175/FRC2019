/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

    /* Declarations */
    private AldrinTalonSRX mTalon;
    private PigeonIMU mPigeon;

    @Override
    public void robotInit() {
        /* Instantiations */
        mTalon = CTREFactory.getTalon(9);
        mPigeon = new PigeonIMU(mTalon);
    }

    @Override
    public void testInit() {
        mPigeon.setYaw(0);
    }

    @Override
    public void testPeriodic() {
        double[] sensors = new double[3];
        mPigeon.getYawPitchRoll(sensors);
        System.out.printf("Pigeon Yaw: %f%n", sensors[0]);
    }

    @Override
    public void robotPeriodic() {

    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {

    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {

    }

}
