package com.team175.robot.util.tuning;

public interface ClosedLoopMotorController {

    void setKp();

    void setKi();

    void setKd();

    void setKf();

    void getKp();

    void getKi();

    void getKd();

    void getKf();

}
