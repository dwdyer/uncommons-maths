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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.util.Random;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.uncommons.maths.Maths;

/**
 * Unit test for the AES RNG.
 * @author Daniel Dyer
 */
public class AESCounterRNGTest
{
    @Test
    public void testMaxSeedLengthOk()
    {
        assert AESCounterRNG.MAX_KEY_LENGTH_BYTES >= 16 :
                "Should allow a 16-byte key";
        assert AESCounterRNG.MAX_KEY_LENGTH_BYTES <= 32 :
                "Shouldn't allow a key longer than 32 bytes";
    }

    @Test
    public void testSerializableWithoutSeedInCounter()
            throws GeneralSecurityException, IOException, ClassNotFoundException
    {
        // Serialise an RNG.
        AESCounterRNG rng = new AESCounterRNG(16);
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
        objectOutStream.writeObject(rng);

        // Read the RNG back-in.
        ObjectInputStream objectInStream = new ObjectInputStream(new ByteArrayInputStream(byteOutStream.toByteArray()));
        AESCounterRNG rng2 = (AESCounterRNG) objectInStream.readObject();
        assert rng != rng2 : "Deserialised RNG should be distinct object.";

        // Both RNGs should generate the same sequence.
        assert RNGTestUtils.testEquivalence(rng, rng2, 20) : "Output mismatch after serialisation.";
    }

    @Test
    public void testSerializableWithSeedInCounter()
            throws GeneralSecurityException, IOException, ClassNotFoundException
    {
        // Serialise an RNG.
        AESCounterRNG rng = new AESCounterRNG(48);
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
        objectOutStream.writeObject(rng);

        // Read the RNG back-in.
        ObjectInputStream objectInStream = new ObjectInputStream(new ByteArrayInputStream(byteOutStream.toByteArray()));
        AESCounterRNG rng2 = (AESCounterRNG) objectInStream.readObject();
        assert rng != rng2 : "Deserialised RNG should be distinct object.";

        // Both RNGs should generate the same sequence.
        assert RNGTestUtils.testEquivalence(rng, rng2, 20) : "Output mismatch after serialisation.";
    }

    /**
     * Test to ensure that two distinct RNGs with the same seed return the
     * same sequence of numbers.
     */
    @Test
    public void testRepeatability() throws GeneralSecurityException
    {
        AESCounterRNG rng = new AESCounterRNG(48);
        // Create second RNG using same seed.
        AESCounterRNG duplicateRNG = new AESCounterRNG(rng.getSeed());
        assert rng.equals(duplicateRNG);
        assert RNGTestUtils.testEquivalence(rng, duplicateRNG, 1000) : "Generated sequences do not match.";
    }

    /**
     * Test to ensure that the output from the RNG is broadly as expected.  This will not
     * detect the subtle statistical anomalies that would be picked up by Diehard, but it
     * provides a simple check for major problems with the output.
     */
    @Test(groups = "non-deterministic",
          dependsOnMethods = "testRepeatability")
    public void testDistribution() throws GeneralSecurityException, SeedException
    {
        AESCounterRNG rng = new AESCounterRNG(DefaultSeedGenerator.getInstance());
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
    public void testStandardDeviation() throws GeneralSecurityException
    {
        AESCounterRNG rng = new AESCounterRNG();
        // Expected standard deviation for a uniformly distributed population of values in the range 0..n
        // approaches n/sqrt(12).
        int n = 100;
        double observedSD = RNGTestUtils.calculateSampleStandardDeviation(rng, n, 10000);
        double expectedSD = 100 / Math.sqrt(12);
        Reporter.log("Expected SD: " + expectedSD + ", observed SD: " + observedSD);
        assert Maths.approxEquals(observedSD, expectedSD, 0.02) : "Standard deviation is outside acceptable range: " + observedSD;
    }

    @Test(expectedExceptions = GeneralSecurityException.class)
    public void testSeedTooShort() throws GeneralSecurityException
    {
        new AESCounterRNG(new byte[]{1, 2, 3}); // Should throw an exception.
    }

    @Test(expectedExceptions = GeneralSecurityException.class)
    public void testSeedTooLong() throws GeneralSecurityException
    {
        new AESCounterRNG(49); // Should throw an exception.
    }

    /**
     * RNG must not accept a null seed otherwise it will not be properly initialised.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullSeed() throws GeneralSecurityException
    {
        new AESCounterRNG((byte[]) null); // Should throw an exception.
    }

    @Test
    public void testSetSeed() throws GeneralSecurityException {
        // can't use a real SeedGenerator since we need longs, so use a Random
        Random masterRNG = new Random();
        long[] seeds = {masterRNG.nextLong(), masterRNG.nextLong(),
        masterRNG.nextLong(), masterRNG.nextLong()};
        long otherSeed = masterRNG.nextLong();
        AESCounterRNG[] rngs = {new AESCounterRNG(16), new AESCounterRNG(16)};
        for (int i=0; i<2; i++) {
            for (long seed : seeds) {
                AESCounterRNG rngReseeded = new AESCounterRNG(rngs[i].getSeed());
                AESCounterRNG rngReseededOther = new AESCounterRNG(rngs[i].getSeed());
                rngReseeded.setSeed(seed);
                rngReseededOther.setSeed(otherSeed);
                assert !(rngs[i].equals(rngReseeded));
                assert !(rngReseededOther.equals(rngReseeded));
                assert rngs[i].nextLong() != rngReseeded.nextLong()
                        : "setSeed had no effect";
                rngs[i] = rngReseeded;
            }
        }
        assert rngs[0].nextLong() != rngs[1].nextLong()
                : "RNGs converged after 4 setSeed calls";
    }

    @Test
    public void testEquals() throws GeneralSecurityException {
        RNGTestUtils.doEqualsSanityChecks(new AESCounterRNG());
    }

    @Test
    public void testHashCode() throws Exception {
        assert RNGTestUtils.testHashCodeDistribution(AESCounterRNG.class.getConstructor())
                : "Too many hashCode collisions";
    }
}
