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
package org.uncommons.maths.demo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.Maths;
import org.uncommons.maths.random.BinomialGenerator;

/**
 * @author Daniel Dyer
 */
class BinomialDistribution extends ProbabilityDistribution
{
    private final int n;
    private final double p;


    public BinomialDistribution(int n, double p)
    {
        this.n = n;
        this.p = p;
    }


    public Map<Double, Double> getExpectedValues()
    {
        Map<Double, Double> values = new HashMap<Double, Double>();
        for (int i = 0; i <= n; i++)
        {
            values.put((double) i, getExpectedProbability(i));
        }
        return values;
    }


    /**
     * This is the probability mass function
     * (http://en.wikipedia.org/wiki/Probability_mass_function) of
     * the Binomial distribution represented by this number generator.
     * @param successes The number of successful trials to determine
     * the probability for.
     * @return The probability of obtaining the specified number of
     * successful trials given the current values of n and p.
     */
    private double getExpectedProbability(int successes)
    {
        double prob = Math.pow(p, successes) * Math.pow(1 - p, n - successes);
        BigDecimal coefficient = new BigDecimal(binomialCoefficient(n, successes));
        return coefficient.multiply(new BigDecimal(prob)).doubleValue();
    }


    private BigInteger binomialCoefficient(int n, int k)
    {
        BigInteger nFactorial = Maths.bigFactorial(n);
        BigInteger kFactorial = Maths.bigFactorial(k);
        BigInteger nMinusKFactorial = Maths.bigFactorial(n - k);
        BigInteger divisor = kFactorial.multiply(nMinusKFactorial);
        return nFactorial.divide(divisor);
    }


    protected BinomialGenerator createValueGenerator(Random rng)
    {
        return new BinomialGenerator(n, p, rng);
    }


    public double getExpectedMean()
    {
        return n * p;
    }


    public double getExpectedStandardDeviation()
    {
        return Math.sqrt(n * p * (1 - p));
    }


    public String getDescription()
    {
        return "Binomial Distribution (n = " + n + ", p = " + p + ")";
    }


    public boolean isDiscrete()
    {
        return true;
    }
}
