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

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import org.uncommons.maths.NumberGenerator;

/**
 * Encapsulates a probability distribution.  Provides both theoretical
 * values for the distribution as well as a way of randomly generating
 * values that follow this distribution.
 * @author Daniel Dyer
 */
abstract class ProbabilityDistribution
{
    protected abstract NumberGenerator<?> createValueGenerator(Random rng);

    public Map<Double, Double> generateValues(int count,
                                              Random rng)
    {
        Map<Double, Double> values = isDiscrete()
                                     ? generateDiscreteValues(count, rng)
                                     : generateContinuousValues(count, rng);

        for (Double key : values.keySet())
        {
            values.put(key, values.get(key) / count);
        }
        return values;
    }


    private Map<Double, Double> generateDiscreteValues(int count,
                                                       Random rng)
    {
        NumberGenerator<?> generator = createValueGenerator(rng);
        Map<Double, Double> values = new HashMap<Double, Double>();
        for (int i = 0; i < count; i++)
        {
            double value = generator.nextValue().doubleValue();
            Double aggregate = values.get(value);
            aggregate = aggregate == null ? 0 : aggregate;
            values.put(value, ++aggregate);
        }
        return values;
    }


    private Map<Double, Double> generateContinuousValues(int count,
                                                         Random rng)
    {
        NumberGenerator<?> generator = createValueGenerator(rng);
        double[] values = new double[count];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < count; i++)
        {
            double value = generator.nextValue().doubleValue();
            min = Math.min(value, min);
            max = Math.max(value, max);
            values[i] = value;
        }
        double range = max - min;
        double intervalSize = range / 19;
        int[] intervals = new int[20];
        for (double value : values)
        {
            int interval = (int) Math.round((value - min) / intervalSize);
            assert interval >= 0 && interval <= 19 : "Invalid interval: " + interval;
            ++intervals[interval];
        }
        Map<Double, Double> discretisedValues = new HashMap<Double, Double>();
        for (int i = 0; i < intervals.length; i++)
        {
            discretisedValues.put(min + i * intervalSize, (double) intervals[i]);
        }
        return discretisedValues;
    }


    public abstract Map<Double, Double> getExpectedValues();

    public abstract double getExpectedMean();

    public abstract double getExpectedStandardDeviation();

    public abstract String getDescription();

    public abstract boolean isDiscrete();
}
