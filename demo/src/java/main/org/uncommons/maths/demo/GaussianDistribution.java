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
import org.uncommons.maths.random.GaussianGenerator;

/**
 * @author Daniel Dyer
 */
class GaussianDistribution extends ProbabilityDistribution
{
    private final double mean;
    private final double standardDeviation;


    public GaussianDistribution(double mean, double standardDeviation)
    {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }


    protected GaussianGenerator createValueGenerator(Random rng)
    {
        return new GaussianGenerator(mean, standardDeviation, rng);
    }


    public Map<Double, Double> getExpectedValues()
    {
        Map<Double, Double> values = new HashMap<Double, Double>();
        double p;
        double x = 0;
        do
        {
            p = getExpectedProbability(mean + x);
            values.put(mean + x, p);
            values.put(mean - x, p);
            x += (3 * standardDeviation / 10); // 99.7% of values are within 3 standard deviations of the mean.
        } while (p > 0.001);
        return values;
    }


    /**
     * This is the probability density function for the Gaussian
     * distribution.
     */
    private double getExpectedProbability(double x)
    {
        double y = 1 / (standardDeviation * Math.sqrt(Math.PI * 2));
        double z = -(Math.pow(x - mean, 2) / (2 * Math.pow(standardDeviation, 2)));
        return y * Math.exp(z);
    }


    public double getExpectedMean()
    {
        return mean;
    }


    public double getExpectedStandardDeviation()
    {
        return standardDeviation;
    }


    public String getDescription()
    {
        return "Gaussian Distribution (\u03bc = " + mean + ", \u03c3 = " + standardDeviation +")";
    }

    
    public boolean isDiscrete()
    {
        return false;
    }
}
