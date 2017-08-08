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
package org.uncommons.maths;

import java.math.BigInteger;
import org.testng.annotations.Test;

/**
 * Unit test for mathematical utility methods.
 * @author Daniel Dyer
 */
public class MathsTest
{
    private static final long SIX_FACTORIAL = 720;
    private static final long TWENTY_FACTORIAL = 2432902008176640000L;
    private static final BigInteger TWENTY_FIVE_FACTORIAL
        = BigInteger.valueOf(TWENTY_FACTORIAL).multiply(BigInteger.valueOf(6375600));
    private static final BigInteger TWO_FIFTY_SEVEN_FACTORIAL
        = new BigInteger("220459168263110562108604143822076784665549850711827804303931502146958249"
            + "30615893047000215487963601197174660253298050216668239958651627999415782834714383848"
            + "85996667944687857781157675283600862019683079990686809338300794339243481380264302611"
            + "42807914049215705624478058833647833721754384415782383297000511532172635054974112781"
            + "08558566790908817370179655474533249971224720020932804837008803000049133703914383084"
            + "35554328097612458008701226571919651262955520000000000000000000000000000000000000000"
            + "00000000000000000000000"); // Computed by Wolfram Alpha

    @Test
    public void testFactorial()
    {
        // Make sure that the correct value (1) is returned for zero
        // factorial.
        assert Maths.factorial(0) == 1 : "0! should be 1." ;
        // Make sure that the correct value (1) is returned for one
        // factorial.
        assert Maths.factorial(1) == 1 : "1! should be 1." ;
        // Make sure that the correct results are returned for other values.
        assert Maths.factorial(6) == SIX_FACTORIAL : "6! should be " + SIX_FACTORIAL ;
        assert Maths.factorial(20) == TWENTY_FACTORIAL : "20! should be " + TWENTY_FACTORIAL ;
    }


    @Test
    public void testBigFactorial()
    {
        // Make sure that the correct value (1) is returned for zero
        // factorial.
        assert Maths.bigFactorial(0).equals(BigInteger.ONE) : "0! should be 1." ;
        // Make sure that the correct value (1) is returned for one
        // factorial.
        assert Maths.bigFactorial(1).equals(BigInteger.ONE) : "1! should be 1." ;
        // Make sure that the correct results are returned for other values
        assert Maths.bigFactorial(6).longValue() == SIX_FACTORIAL : "6! should be " + SIX_FACTORIAL ;
        assert Maths.bigFactorial(20).longValue() == TWENTY_FACTORIAL : "20! should be " + TWENTY_FACTORIAL ;
        // Make sure that the correct value is returned for factorials
        // outside of the range of longs.
        assert Maths.bigFactorial(25).equals(TWENTY_FIVE_FACTORIAL) : "25! should be " + TWENTY_FIVE_FACTORIAL;
        assert Maths.bigFactorial(257).equals(TWO_FIFTY_SEVEN_FACTORIAL) : "257! should be " + TWO_FIFTY_SEVEN_FACTORIAL;
    }


    /**
     * Factorials of negative integers are not supported.  This test
     * checks that an appropriate exception is thrown.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeFactorial()
    {
        Maths.factorial(-1); // Should throw an exception.
    }


    /**
     * The standard factorial method (the one that uses longs rather than
     * BigIntegers) cannot calculate factorials larger than 20!.  It should
     * throw an exception rather than overflow silently.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFactorialTooBig()
    {
        Maths.factorial(21); // Should throw an exception.
    }


    /**
     * Factorials of negative integers are not supported.  This test
     * checks that an appropriate exception is thrown.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeBigFactorial()
    {
        Maths.bigFactorial(-1); // Should throw an exception.
    }


    @Test
    public void testRaiseToPower()
    {
        assert Maths.raiseToPower(0, 0) == 1 : "Any value raised to the power of zero should equal 1.";
        assert Maths.raiseToPower(5, 0) == 1 : "Any value raised to the power of zero should equal 1.";
        assert Maths.raiseToPower(123, 1) == 123 : "Any value raised to the power of one should be unchanged.";
        assert Maths.raiseToPower(250, 2) == 62500 : "250^2 incorrectly calculated,";
        assert Maths.raiseToPower(2, 10) == 1024 : "2^10 incorrectly calculated.";
        // Check values that generate a result outside of the range of an int.
        assert Maths.raiseToPower(2, 34) == 17179869184L : "2^34 incorrectly calculated.";
    }


    /**
     * Negative powers are not supported by the raiseToPower method.  This
     * test checks that an appropriate exception is thrown.
     */
    @Test(dependsOnMethods = "testRaiseToPower",
          expectedExceptions = IllegalArgumentException.class)
    public void testNegativePower()
    {
        Maths.raiseToPower(1, -2); // Should throw an exception.
    }


    @Test
    public void testLog()
    {
        double log = Maths.log(2, 8);
        assert Math.round(log) == 3 : "Base-2 logarithm of 8 should be 3, is " + log;
    }


    @Test
    public void testApproxEquals()
    {
        assert Maths.approxEquals(1.1d, 1.2d, 0.1d) : "1.1 and 1.2 should be equal with tolerance of 10%";
        assert !Maths.approxEquals(1.1d, 1.3d, 0.1d) : "1.1 and 1.3 should be unequal with tolerance of 10%";
    }


    @Test
    public void testApproxEqualsToleranceZero()
    {
        // Zero tolerance should allow exactly equivalent values.
        assert Maths.approxEquals(1d, 1d, 0d) : "Identical values should be equal with zero tolerance.";
    }
    

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testApproxEqualsToleranceTooLow()
    {
        Maths.approxEquals(0d, 1d, -0.1d); // Tolerance too low (<0), should throw exception.
    }

    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testApproxEqualsToleranceTooHigh()
    {
        Maths.approxEquals(0d, 1d, 1.2d); // Tolerance too high (>1), should throw exception.
    }


    @Test
    public void testRestrictRangeValueWithinRange()
    {
        assert Maths.restrictRange(5, 0, 10) == 5 : "Value should not be altered when it is in-range.";
        assert Maths.restrictRange(5L, 0L, 10L) == 5L : "Value should not be altered when it is in-range.";
        assert Maths.restrictRange(5d, 0d, 10d) == 5d : "Value should not be altered when it is in-range.";
    }


    @Test
    public void testRestrictRangeValueTooLow()
    {
        assert Maths.restrictRange(-1, 0, 10) == 0 : "Minimum should be returned when value is too low.";
        assert Maths.restrictRange(-1L, 0L, 10L) == 0L : "Minimum should be returned when value is too low.";
        assert Maths.restrictRange(-0.1d, 0d, 10d) == 0d : "Minimum should be returned when value is too low.";
    }


    @Test
    public void testRestrictRangeValueTooHigh()
    {
        assert Maths.restrictRange(11, 0, 10) == 10 : "Maximum should be returned when value is too high.";
        assert Maths.restrictRange(11L, 0L, 10L) == 10L : "Maximum should be returned when value is too high.";
        assert Maths.restrictRange(10.1d, 0d, 10d) == 10d : "Maximum should be returned when value is too high.";
    }


    @Test
    public void testGreatestCommonDivisor()
    {
        long gcd = Maths.greatestCommonDivisor(9, 12);
        assert gcd == 3 : "GCD should be 3, is " + gcd;
    }


    @Test
    public void testNoCommonDivisorGreaterThanOne()
    {
        long gcd = Maths.greatestCommonDivisor(11, 12);
        assert gcd == 1 : "GCD should be 1, is " + gcd;
    }


    @Test
    public void testGreatestCommonDivisorNegatives()
    {
        long gcd = Maths.greatestCommonDivisor(-9, 12);
        assert gcd == 3 : "GCD should be 3, is " + gcd;
        gcd = Maths.greatestCommonDivisor(10, -12);
        assert gcd == 2 : "GCD should be 2, is " + gcd;
    }
}
