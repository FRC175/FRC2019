package com.team175.robot.commands.auto;

import com.team175.robot.commands.AldrinCommand;

public class LogCommand extends AldrinCommand {

    private String mMsg;

    public LogCommand(String msg) {
        mMsg = msg;
    }

    @Override
    protected void initialize() {
        mLogger.debug(mMsg);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


}
