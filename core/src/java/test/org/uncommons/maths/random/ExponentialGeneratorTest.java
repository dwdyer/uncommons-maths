// ============================================================================
//   Copyright 2006-2010 Daniel W. Dyer
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
 * Unit test for the exponential random generator.
 * @author Daniel Dyer
 */
public class ExponentialGeneratorTest
{
    private final Random rng = new MersenneTwisterRNG();

    @Test(groups = "non-deterministic")
    public void testDistribution()
    {
        final double rate = 3.2d;
        NumberGenerator<Double> generator = new ExponentialGenerator(rate, rng);
        checkDistribution(generator, rate);
    }


    @Test(groups = "non-deterministic")
    public void testDynamicParameters()
    {
        final double initialRate = 0.75d;
        AdjustableNumberGenerator<Double> rateGenerator = new AdjustableNumberGenerator<Double>(initialRate);
        NumberGenerator<Double> generator = new ExponentialGenerator(rateGenerator, rng);
        checkDistribution(generator, initialRate);

        // Adjust parameters and ensure that the generator output conforms to this
        // new distribution.
        final double adjustedRate = 1.05d;
        rateGenerator.setValue(adjustedRate);

        checkDistribution(generator, adjustedRate);
    }


    
    private void checkDistribution(NumberGenerator<Double> generator,
                                   double rate)
    {
        final double expectedMean = 1 / rate;
        final double expectedStandardDeviation = Math.sqrt(1 / (rate * rate));
        final double expectedMedian = Math.log(2) / rate;

        final int iterations = 10000;
        DataSet data = new DataSet(iterations);
        for (int i = 0; i < iterations; i++)
        {
            data.addValue(generator.nextValue());
        }
        // Exponential distribution appears to be a bit more volatile than the others in
        // terms of conforming to expectations, so use a 4% tolerance here, instead of the 2%
        // used for other distributions, to avoid too many false positives.
        assert Maths.approxEquals(data.getArithmeticMean(), expectedMean, 0.04d)
            : "Observed mean outside acceptable range: " + data.getArithmeticMean();
        assert Maths.approxEquals(data.getSampleStandardDeviation(), expectedStandardDeviation, 0.04d)
            : "Observed standard deviation outside acceptable range: " + data.getSampleStandardDeviation();
        assert Maths.approxEquals(data.getMedian(), expectedMedian, 0.04d)
            : "Observed median outside acceptable range: " + data.getMedian();
    }
}
