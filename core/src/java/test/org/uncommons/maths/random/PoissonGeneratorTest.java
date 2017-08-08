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

import java.util.Random;
import org.testng.annotations.Test;
import org.uncommons.maths.Maths;
import org.uncommons.maths.number.AdjustableNumberGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.statistics.DataSet;

/**
 * Unit test for the Poisson number generator.
 * @author Daniel Dyer
 */
public class PoissonGeneratorTest
{
    private final Random rng = new MersenneTwisterRNG();


    /**
     * Check that the observed mean and standard deviation are consistent
     * with the specified distribution parameters.
     */
    @Test(groups = "non-deterministic")
    public void testDistribution()
    {
        final double mean = 19;
        NumberGenerator<Integer> generator = new PoissonGenerator(mean, rng);
        checkDistribution(generator, mean);
    }


    @Test(groups = "non-deterministic")
    public void testDynamicParameters()
    {
        final double initialMean = 19;
        AdjustableNumberGenerator<Double> meanGenerator = new AdjustableNumberGenerator<Double>(initialMean);
        NumberGenerator<Integer> generator = new PoissonGenerator(meanGenerator,
                                                                  rng);
        checkDistribution(generator, initialMean);

        // Adjust parameters and ensure that the generator output conforms to this new
        // distribution.
        final double adjustedMean = 13;
        meanGenerator.setValue(adjustedMean);

        checkDistribution(generator, adjustedMean);
    }



    /**
     * The mean must be greater than zero to be useful.  This test ensures
     * that an appropriate exception is thrown if the mean is not positive.  Not
     * throwing an exception is an error because it permits undetected bugs in
     * programs that use {@link PoissonGenerator}.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMeanTooLow()
    {
        new PoissonGenerator(0d, rng);
    }


    private void checkDistribution(NumberGenerator<Integer> generator,
                                   double expectedMean)
    {
        final int iterations = 10000;
        DataSet data = new DataSet(iterations);
        for (int i = 0; i < iterations; i++)
        {
            int value = generator.nextValue();
            assert value >= 0 : "Value must be non-negative: " + value;
            data.addValue(value);
        }
        assert Maths.approxEquals(data.getArithmeticMean(), expectedMean, 0.02)
                : "Observed mean outside acceptable range: " + data.getArithmeticMean();

        // Variance of a Possion distribution equals its mean.
        final double expectedStandardDeviation = Math.sqrt(expectedMean);
        assert Maths.approxEquals(data.getSampleStandardDeviation(), expectedStandardDeviation, 0.02)
                : "Observed standard deviation outside acceptable range: " + data.getSampleStandardDeviation();
    }
}
