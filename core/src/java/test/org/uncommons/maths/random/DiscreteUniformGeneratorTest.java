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

import org.testng.annotations.Test;
import org.uncommons.maths.Maths;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.statistics.DataSet;

/**
 * Unit test for the uniform integer number generator.
 * @author Daniel Dyer
 */
public class DiscreteUniformGeneratorTest
{
    /**
     * Check that the observed mean and standard deviation are consistent
     * with the specified distribution parameters.
     */
    @Test(groups = "non-deterministic")
    public void testDistribution()
    {
        final int min = 150;
        final int max = 500;
        final int range = max - min;
        // Expected standard deviation for a uniformly distributed population of values in the range 0..n
        // approaches n/sqrt(12).
        NumberGenerator<Integer> generator = new DiscreteUniformGenerator(min,
                                                                          max,
                                                                          new MersenneTwisterRNG());
        final int iterations = 10000;
        DataSet data = new DataSet(iterations);
        for (int i = 0; i < iterations; i++)
        {
            int value = generator.nextValue();
            assert value >= min && value <= max : "Value out-of-range: " + value;
            data.addValue(value);
        }
        final double expectedMean = (range / 2) + min;
        assert Maths.approxEquals(data.getArithmeticMean(), expectedMean, 0.02)
            : "Observed mean outside acceptable range: " + data.getArithmeticMean();
        final double standardDeviation = range / Math.sqrt(12);
        assert Maths.approxEquals(data.getSampleStandardDeviation(), standardDeviation, 0.02)
            : "Observed standard deviation outside acceptable range: " + data.getSampleStandardDeviation();
        // Expected median is the same as expected mean.
        assert Maths.approxEquals(data.getMedian(), expectedMean, 0.02)
            : "Observed mean outside acceptable range: " + data.getMedian();
    }

}
