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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.Maths;
import org.uncommons.maths.random.PoissonGenerator;

/**
 * @author Daniel Dyer
 */
class PoissonDistribution extends ProbabilityDistribution
{
    private final double mean;

    PoissonDistribution(double mean)
    {
        this.mean = mean;
    }


    public Map<Double, Double> getExpectedValues()
    {
        Map<Double, Double> values = new HashMap<Double, Double>();
        int index = 0;
        double p;
        do
        {
            p = getExpectedProbability(index);
            values.put((double) index, p);
            ++index;
        } while (p > 0.001);
        return values;
    }


    /**
     * This is the probability mass function
     * (http://en.wikipedia.org/wiki/Probability_mass_function) of
     * the Poisson distribution represented by this number generator.
     * @param events The number of occurrences to determine the
     * probability for.
     * @return The probability of the specified number of events
     * occurring given the current value of lamda.
     */
    private double getExpectedProbability(int events)
    {
        BigDecimal kFactorial = new BigDecimal(Maths.bigFactorial(events));
        double numerator = Math.exp(-mean) * Math.pow(mean, events);
        return new BigDecimal(numerator).divide(kFactorial, RoundingMode.HALF_UP).doubleValue();
    }


    
    protected PoissonGenerator createValueGenerator(Random rng)
    {
        return new PoissonGenerator(mean, rng);
    }


    public double getExpectedMean()
    {
        return mean;
    }


    public double getExpectedStandardDeviation()
    {
        return Math.sqrt(mean); 
    }


    public String getDescription()
    {
        return "Poisson Distribution (\u03bb = " + mean + ')';
    }


    public boolean isDiscrete()
    {
        return true;
    }    
}
