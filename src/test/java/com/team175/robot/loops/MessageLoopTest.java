package com.team175.robot.loops;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageLoopTest {

    private final String[] mNaan = {
            "Abdullah is watching you.",
            "Jamie is the all supreme lord.",
            "Bret is MVP",
            "Adrian is sicko mode!",
            "Andrew Jones was here.",
            "Aaron likes his waifu!",
            "Aaron's girlfriend is fake!"
    };

    @Test
    public void testTrolling() {
        Looper foo = new Looper(1, new MessageLoop(mNaan, "Yo!"));
        try {
            foo.start();
        } catch (Exception e) {
            foo.stop();
        }
    }

}