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
import org.uncommons.maths.random.ExponentialGenerator;

/**
 * @author Daniel Dyer
 */
class ExponentialDistribution extends ProbabilityDistribution
{
    private final double rate;


    public ExponentialDistribution(double rate)
    {
        this.rate = rate;
    }


    protected ExponentialGenerator createValueGenerator(Random rng)
    {
        return new ExponentialGenerator(rate, rng);
    }


    public Map<Double, Double> getExpectedValues()
    {
        Map<Double, Double> values = new HashMap<Double, Double>();
        double p;
        double x = 0;
        do
        {
            p = getExpectedProbability(x);
            values.put(x, p);
            x += (1 / (2 * rate));
        } while (p > 0.001);
        return values;
    }


    /**
     * This is the probability density function for the Exponential
     * distribution.
     */
    private double getExpectedProbability(double x)
    {
        if (x < 0)
        {
            return 0;
        }
        else
        {
            return rate * Math.exp(-rate * x);
        }
    }


    public double getExpectedMean()
    {
        return Math.pow(rate, -1);
    }


    public double getExpectedStandardDeviation()
    {
        return Math.sqrt(Math.pow(rate, -2));
    }


    public String getDescription()
    {
        return "Exponential Distribution (\u03bb = " + rate + ")";
    }


    public boolean isDiscrete()
    {
        return false;
    }
}
