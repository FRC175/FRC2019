package com.team175.robot.commands.led;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LEDColor;
import com.team175.robot.subsystems.LED;

public class LEDToColor extends AldrinCommand {

    private LEDColor mColor;

    public LEDToColor(LEDColor color) {
        requires(LED.getInstance());
        mColor = color;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        // LED.getInstance().setColor(mColor);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
