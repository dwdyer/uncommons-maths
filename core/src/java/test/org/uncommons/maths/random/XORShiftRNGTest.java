// ============================================================================
//   Copyright 2006-2012 Daniel W. Dyer
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// ============================================================================
package org.uncommons.maths.random;

import java.security.GeneralSecurityException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.uncommons.maths.Maths;

/**
 * Unit test for the cellular automaton RNG.
 * @author Daniel Dyer
 */
public class XORShiftRNGTest
{
    /**
     * Test to ensure that two distinct RNGs with the same seed return the
     * same sequence of numbers.  This method must be run before any of the
     * other tests otherwise the state of the RNG will not be the same in the
     * duplicate RNG.
     */
    @Test
    public void testRepeatability()
    {
        XORShiftRNG rng = new XORShiftRNG();
        // Create second RNG using same seed.
        XORShiftRNG duplicateRNG = new XORShiftRNG(rng.getSeed());
        assert RNGTestUtils.testEquivalence(rng, duplicateRNG, 1000) : "Generated sequences do not match.";
    }


    /**
     * Test to ensure that the output from the RNG is broadly as expected.  This will not
     * detect the subtle statistical anomalies that would be picked up by Diehard, but it
     * provides a simple check for major problems with the output.
     */
    @Test(groups = "non-deterministic",
          dependsOnMethods = "testRepeatability")
    public void testDistribution() throws SeedException
    {
        XORShiftRNG rng = new XORShiftRNG(DefaultSeedGenerator.getInstance());
        double pi = RNGTestUtils.calculateMonteCarloValueForPi(rng, 100000);
        Reporter.log("Monte Carlo value for Pi: " + pi);
        assert Maths.approxEquals(pi, Math.PI, 0.01) : "Monte Carlo value for Pi is outside acceptable range:" + pi;
    }


    /**
     * Test to ensure that the output from the RNG is broadly as expected.  This will not
     * detect the subtle statistical anomalies that would be picked up by Diehard, but it
     * provides a simple check for major problems with the output.
     */
    @Test(groups = "non-deterministic",
          dependsOnMethods = "testRepeatability")
    public void testStandardDeviation()
    {
        XORShiftRNG rng = new XORShiftRNG();
        // Expected standard deviation for a uniformly distributed population of values in the range 0..n
        // approaches n/sqrt(12).
        int n = 100;
        double observedSD = RNGTestUtils.calculateSampleStandardDeviation(rng, n, 10000);
        double expectedSD = n / Math.sqrt(12);
        Reporter.log("Expected SD: " + expectedSD + ", observed SD: " + observedSD);
        assert Maths.approxEquals(observedSD, expectedSD, 0.02) : "Standard deviation is outside acceptable range: " + observedSD;
    }


    /**
     * Make sure that the RNG does not accept seeds that are too small since
     * this could affect the distribution of the output.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidSeedSize()
    {
        new XORShiftRNG(new byte[]{1, 2, 3}); // Not enough bytes, should cause an IllegalArgumentException.
    }


    /**
     * RNG must not accept a null seed otherwise it will not be properly initialised.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullSeed()
    {
        new XORShiftRNG((byte[]) null);
    }


    @Test
    public void testSerializable() throws IOException, ClassNotFoundException
    {
        // Serialise an RNG.
        XORShiftRNG rng = new XORShiftRNG();
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
        objectOutStream.writeObject(rng);

        // Read the RNG back-in.
        ObjectInputStream objectInStream = new ObjectInputStream(new ByteArrayInputStream(byteOutStream.toByteArray()));
        XORShiftRNG rng2 = (XORShiftRNG) objectInStream.readObject();
        assert rng != rng2 : "Deserialised RNG should be distinct object.";

        // Both RNGs should generate the same sequence.
        assert RNGTestUtils.testEquivalence(rng, rng2, 20) : "Output mismatch after serialisation.";
    }

    @Test
    public void testEquals() throws ReflectiveOperationException
    {
        RNGTestUtils.doEqualsSanityChecks(XORShiftRNG.class.getConstructor());
    }

    @Test
    public void testHashCode() throws Exception
    {
        assert RNGTestUtils.testHashCodeDistribution(XORShiftRNG.class.getConstructor())
                : "Too many hashCode collisions";
    }
}
