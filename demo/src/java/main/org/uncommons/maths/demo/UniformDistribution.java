// ============================================================================
//   Copyright 2006, 2007 Daniel W. Dyer
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.random.DiscreteUniformGenerator;

/**
 * @author Daniel Dyer
 */
public class UniformDistribution extends AbstractProbabilityDistribution
{
    private final int min;
    private final int max;

    public UniformDistribution(int min, int max)
    {
        this.min = min;
        this.max = max;
    }


    public Map<Integer, Double> getExpectedValues()
    {
        Map<Integer, Double> values = new LinkedHashMap<Integer, Double>();
        for (int i = min; i <= max; i++)
        {
            values.put(i, 1d / ((max - min) + 1));
        }
        return values;
    }


    protected DiscreteUniformGenerator createValueGenerator(Random rng)
    {
        return new DiscreteUniformGenerator(min, max, rng);
    }
}
