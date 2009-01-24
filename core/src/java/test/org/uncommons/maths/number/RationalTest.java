// ============================================================================
//   Copyright 2006, 2007, 2008 Daniel W. Dyer
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
package org.uncommons.maths.number;

import java.math.BigDecimal;
import org.testng.annotations.Test;

/**
 * Unit test for the {@link Rational} numeric type.
 * @author Daniel Dyer
 */
public class RationalTest
{
    @Test
    public void testEquality()
    {
        Rational r1 = new Rational(3, 4);
        Rational r2 = new Rational(3, 4);
        assert r1.equals(r2) : "Numerically equivalent rationals should be considered equal.";
        assert r2.equals(r1) : "Equality must be reflective.";
        assert r1.hashCode() == r2.hashCode() : "Equal values must have identical hash codes.";
        assert !r1.equals(Double.valueOf(0.75)) : "Objects of different types should not be considered equal.";

        assert !Rational.ONE.equals(Rational.HALF) : "Numerically distinct rationals should not be considered equal.";
        assert Rational.ONE.equals(Rational.ONE) : "Equality must be reflexive.";
        assert !Rational.ONE.equals(null) : "No object should be considered equal to null.";

    }


    @Test(dependsOnMethods = "testEquality")
    public void testComparisons()
    {
        Rational r1 = new Rational(3, 4);
        Rational r2 = new Rational(3, 4);
        Rational r3 = new Rational(9, 10);
        assert r1.compareTo(r1) == 0 : "Equality must be reflexive.";
        assert r1.compareTo(r2) == 0 : "equals() must be consitent with compareTo()";
        assert r1.compareTo(r3) < 0 : "First argument should be less than second.";
        assert r3.compareTo(r1) > 0 : "First argument should be greater than second.";
    }


    /**
     * Fractions should always be stored in their simplest form (so 9/12
     * should be converted to 3/4).
     */
    @Test
    public void testSimplification()
    {
        Rational rational = new Rational(3, 6);
        assert rational.getNumerator() == 1 : "Numerator should be 1, is " + rational.getNumerator();
        assert rational.getDenominator() == 2 : "Denominator should be 2, is " + rational.getDenominator();
    }


    @Test
    public void testMultiply()
    {
        Rational a = new Rational(2, 3);
        Rational b = new Rational(1, 2);
        Rational result = a.multiply(b);
        assert result.getNumerator() == 1 : "Numerator should be 1, is " + result.getNumerator();
        assert result.getDenominator() == 3 : "Denominator should be 3, is " + result.getDenominator();
    }


    @Test
    public void testDivide()
    {
        Rational a = new Rational(2, 3);
        Rational b = new Rational(1, 2);
        Rational result = a.divide(b);
        assert result.getNumerator() == 4 : "Numerator should be 4, is " + result.getNumerator();
        assert result.getDenominator() == 3 : "Denominator should be 3, is " + result.getDenominator();
    }


    @Test
    public void testAddSameDenominator()
    {
        Rational a = new Rational(2, 5);
        Rational b = new Rational(1, 5);
        Rational result = a.add(b);
        assert result.getNumerator() == 3 : "Numerator should be 3, is " + result.getNumerator();
        assert result.getDenominator() == 5 : "Denominator should be 5, is " + result.getDenominator();
    }


    @Test
    public void testAddDifferentDenominators()
    {
        Rational a = new Rational(1, 3);
        Rational b = new Rational(1, 6);
        Rational result = a.add(b);
        assert result.getNumerator() == 1 : "Numerator should be 1, is " + result.getNumerator();
        assert result.getDenominator() == 2 : "Denominator should be 2, is " + result.getDenominator();        
    }


    @Test
    public void testSubtractSameDenominator()
    {
        Rational a = new Rational(3, 5);
        Rational b = new Rational(1, 5);
        Rational result = a.subtract(b);
        assert result.getNumerator() == 2 : "Numerator should be 2, is " + result.getNumerator();
        assert result.getDenominator() == 5 : "Denominator should be 5, is " + result.getDenominator();
    }


    @Test
    public void testSubtractDifferentDenominators()
    {
        Rational a = new Rational(11, 20);
        Rational b = new Rational(1, 7);
        Rational result = a.subtract(b);
        assert result.getNumerator() == 57 : "Numerator should be 57, is " + result.getNumerator();
        assert result.getDenominator() == 140 : "Denominator should be 140, is " + result.getDenominator();
    }


    @Test
    public void testToStringProperFraction()
    {
        String string = new Rational(4, 5).toString();
        assert string.equals("4/5") : "String value should be \"4/5\", is \"" + string + "\"";
    }


    @Test
    public void testToStringTopHeavyFraction()
    {
        String string = new Rational(3, 2).toString();
        assert string.equals("3/2") : "String value should be \"3/2\", is \"" + string + "\"";
    }    


    @Test
    public void testToStringInteger()
    {
        String string = new Rational(8, 2).toString();
        assert string.equals("4") : "String value should be \"4\", is \"" + string + "\"";
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroDenominator()
    {
        new Rational(2, 0); // Division by zero not permitted, should throw IllegalArgumentException.
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeDenominator()
    {
        new Rational(1, -2); // Negative denominator not permitted, should throw IllegalArgumentException.
    }


    /**
     * Tests that a rational is correctly constructed from a BigDecimal value.
     */
    @Test
    public void testBigDecimalConversion()
    {
        Rational rational = new Rational(new BigDecimal("1.2"));
        assert rational.getNumerator() == 6 : "Numerator should be 6, is " + rational.getNumerator();
        assert rational.getDenominator() == 5 : "Denominator should be 5, is " + rational.getDenominator();
    }


    @Test
    public void testBigDecimalConversionExcessPrecision()
    {
        Rational rational = new Rational(new BigDecimal("1.20000000000000000000000000"));
        assert rational.getNumerator() == 6 : "Numerator should be 6, is " + rational.getNumerator();
        assert rational.getDenominator() == 5 : "Denominator should be 5, is " + rational.getDenominator();
    }
}
