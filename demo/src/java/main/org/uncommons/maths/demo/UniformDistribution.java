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
package org.uncommons.maths.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.random.DiscreteUniformGenerator;

/**
 * @author Daniel Dyer
 */
class UniformDistribution extends ProbabilityDistribution
{
    public static final double SQRT_12 = 3.4641016151377546;

    private final int min;
    private final int max;

    UniformDistribution(int min, int max)
    {
        this.min = min;
        this.max = max;
    }


    public Map<Double, Double> getExpectedValues()
    {
        Map<Double, Double> values = new HashMap<Double, Double>();
        for (int i = min; i <= max; i++)
        {
            values.put((double) i, 1.0 / ((max - min) + 1));
        }
        return values;
    }


    protected DiscreteUniformGenerator createValueGenerator(Random rng)
    {
        return new DiscreteUniformGenerator(min, max, rng);
    }


    public double getExpectedMean()
    {
        return (max - min) / 2.0 + min;
    }


    public double getExpectedStandardDeviation()
    {
        return (max - min) / SQRT_12;
    }


    public String getDescription()
    {
        return "Uniform Distribution (Range = " + min + "..." + max + ')';
    }


    public boolean isDiscrete()
    {
        return true;
    }
}
