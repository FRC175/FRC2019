package com.team175.robot.util.actions;

public class ActionRunner {

    private final Runner mRunner;

    public ActionRunner(Action a) {
        mRunner = new Runner(a::loop, a::isFinished);
    }

    public static void runAction(Action a) {

    }

}
