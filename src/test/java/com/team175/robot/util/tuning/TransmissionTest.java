package com.team175.robot.util.tuning;

import com.team175.robot.util.tuning.Transmission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arvind
 */
public class TransmissionTest {

    @Test
    public void testKfCalculation() {
        int maxRPM = 60000;
        int countsPerRev = 4096;
        double gearRatio = 16 / 1;

        Transmission t = new Transmission(maxRPM, countsPerRev, gearRatio);
        assertEquals(t.getKf(), getKf195Method(maxRPM, countsPerRev, gearRatio));
    }

    public double getKf195Method(int maxRPM, int countsPerRev, double gearRatio) {
        // Video is wrong: Correct formula is the returned one, NOT the commented out one
        // return 1023.0 / (maxRPM / gearRatio / (600.0 * countsPerRev));
        return 1023.0 / ((double) (maxRPM * countsPerRev) / gearRatio / 600.0);
    }

}