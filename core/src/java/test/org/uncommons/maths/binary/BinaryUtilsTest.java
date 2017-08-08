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
package org.uncommons.maths.binary;

import java.util.Arrays;
import org.testng.annotations.Test;

/**
 * Unit test for binary/hex utility methods.
 * @author Daniel Dyer
 */
public class BinaryUtilsTest
{
    @Test
    public void testBytesToHexString()
    {
        byte[] seed = {124, 11, 0, -76, -3, 127, -128, -1};
        String expectedHex = "7C0B00B4FD7F80FF";
        String generatedHex = BinaryUtils.convertBytesToHexString(seed);
        assert generatedHex.equals(expectedHex) : "Wrong hex string: " + generatedHex;
    }


    @Test
    public void testHexStringToBytes()
    {
        String hex = "7C0B00B4FD7F80FF";
        byte[] expectedData = {124, 11, 0, -76, -3, 127, -128, -1};
        byte[] generatedData = BinaryUtils.convertHexStringToBytes(hex);
        assert Arrays.equals(generatedData, expectedData) : "Wrong byte array: " + Arrays.toString(generatedData);
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidHexStringLength()
    {
        // Hex string should have even number of characters (2 per byte), so
        // this should throw an exception.
        BinaryUtils.convertHexStringToBytes("F2C");
    }


    /**
     * Make sure that the conversion method correctly converts 4 bytes to an
     * integer assuming big-endian convention.
     */
    @Test
    public void testConvertBytesToInt()
    {
        byte[] bytes = {8, 4, 2, 1};
        final int expected = 134480385;
        int result = BinaryUtils.convertBytesToInt(bytes, 0);
        assert expected == result : "Expected " + expected + ", was " + result;
    }


    /**
     * Make sure that the conversion method correctly converts multiples of 4 bytes to an
     * array of integers assuming big-endian convention.
     */
    @Test
    public void testConvertBytesToInts()
    {
        byte[] bytes = {0, 0, 0, 16, 8, 4, 2, 1};
        final int expected1 = 16;
        int[] result = BinaryUtils.convertBytesToInts(bytes);
        assert expected1 == result[0] : "Expected first int to be " + expected1 + ", was " + result[0];
        final int expected2 = 134480385;
        assert expected2 == result[1] : "Expected second int to be " + expected2 + ", was " + result[1];
    }


    /**
     * Make sure that the conversion method throws an exception if the number of bytes is
     * not a multiple of 4.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConvertWrongNumberOfBytesToInts()
    {
        byte[] bytes = {0, 0, 16, 8, 4, 2, 1};
        BinaryUtils.convertBytesToInts(bytes);
    }


    /**
     * Make sure that the conversion method correctly converts 8 bytes to a
     * long assuming big-endian convention.
     */
    @Test
    public void testConvertBytesToLong()
    {
        byte[] bytes = {0, 0, 0, 16, 8, 4, 2, 1};
        final long expected = 68853957121L;
        long result = BinaryUtils.convertBytesToLong(bytes, 0);
        assert expected == result : "Expected " + expected + ", was " + result;
    }


    /**
     * Regression test for failure to correctly convert values that contain negative bytes.
     */
    @Test
    public void testConvertNegativeBytesToLong()
    {
        byte[] bytes = {-121, 30, 107, -100, -76, -8, 53, 81};
        final long expected = -510639L;
        long result = BinaryUtils.convertBytesToLong(bytes, 0);
        assert expected == result : "Expected " + expected + ", was " + result;
    }


    @Test
    public void testConvertFixedPoint()
    {
        final double value = 0.6875d;
        BitString bits = BinaryUtils.convertDoubleToFixedPointBits(value);
        assert "1011".equals(bits.toString()) : "Binary representation should be 1011, is " + bits;
    }


    /**
     * Makes sure that zero is dealt with correctly by the fixed point conversion
     * method.
     */
    @Test(dependsOnMethods = "testConvertFixedPoint")
    public void testConvertFixedPointZero()
    {
        BitString bits = BinaryUtils.convertDoubleToFixedPointBits(0d);
        assert bits.countSetBits() == 0 : "Binary representation should be 0";
    }


    /**
     * An attempt to convert a value of 1 or greater should result in an exception
     * since there is no way to represent these values in our fixed point scheme
     * (which has no places to the left of the decimal point).  Not throwing an
     * exception would be a bug.
     */
    @Test(dependsOnMethods = "testConvertFixedPoint",
          expectedExceptions = IllegalArgumentException.class)
    public void testConvertFixedPointTooHigh()
    {
        BinaryUtils.convertDoubleToFixedPointBits(1d);
    }


    /**
     * An attempt to convert a negative value should result in an exception
     * since there is no way to represent these values in our fixed point scheme
     * (there is no sign bit).  Not throwing an exception would be a bug.
     */
    @Test(dependsOnMethods = "testConvertFixedPoint",
          expectedExceptions = IllegalArgumentException.class)
    public void testConvertFixedPointNegative()
    {
        BinaryUtils.convertDoubleToFixedPointBits(-0.5d);
    }
}
