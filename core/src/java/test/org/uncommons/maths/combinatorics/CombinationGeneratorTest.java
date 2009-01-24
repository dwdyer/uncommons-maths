// ============================================================================
//   Copyright 2006-2009 Daniel W. Dyer
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
package org.uncommons.maths.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

/**
 * Unit test for the {@link CombinationGenerator} class.
 * @author Daniel Dyer
 */
public class CombinationGeneratorTest
{
    private final String[] elements = new String[]{"1", "2", "3"};

    /**
     * This is the main test case and ensures that the combination generator
     * produces the correct output.
     */
    @Test
    public void testCombinations()
    {
        CombinationGenerator<String> generator = new CombinationGenerator<String>(elements, 2);
        assert generator.hasMore() : "Generator should have more combinations available.";
        assert generator.getTotalCombinations() == 3 : "Possible combinations should be 3.";
        assert generator.getRemainingCombinations() == 3: "Remaining combinations should be 3.";

        String[] combination1 = generator.nextCombinationAsArray();
        assert generator.hasMore() : "Generator should have more combinations available.";
        assert combination1.length == 2 : "Combination length should be 2.";
        assert generator.getRemainingCombinations() == 2: "Remaining combinations should be 2.";
        assert !combination1[0].equals(combination1[1]) : "Combination elements should be different.";

        List<String> combination2 = generator.nextCombinationAsList(); // Use different "next" method to exercise other options.
        assert generator.hasMore() : "Generator should have more combinations available.";
        assert combination2.size() == 2 : "Combination length should be 2.";
        assert generator.getRemainingCombinations() == 1: "Remaining combinations should be 1.";
        // Make sure this combination is different from the previous one.
        assert !combination2.get(0).equals(combination2.get(1)) : "Combination elements should be different.";
        assert !(combination1[0] + combination1[1]).equals(combination2.get(0) + combination2.get(1))
            : "Combination should be different from previous one.";

        List<String> combination3 = new ArrayList<String>(2);
        generator.nextCombinationAsList(combination3); // Use different "next" method to exercise other options.
        assert !generator.hasMore() : "Generator should have no more combinations available.";
        assert combination3.size() == 2 : "Combination length should be 2.";
        assert generator.getRemainingCombinations() == 0: "Remaining combinations should be 0.";
        // Make sure this combination is different from the others generated.
        assert !combination3.get(0).equals(combination3.get(1)) : "Combination elements should be different.";
        assert !(combination2.get(0) + combination2.get(1)).equals(combination3.get(0) + combination3.get(1))
            : "Combination should be different from previous one.";
        assert !(combination1[0] + combination1[1]).equals(combination3.get(0) + combination3.get(1))
            : "Combination should be different from previous one.";
    }


    /**
     * Ensures that the combination generator works correctly with the "for-each" style
     * loop.
     */
    @Test
    public void testIterable()
    {
        CombinationGenerator<String> generator = new CombinationGenerator<String>(elements, 2);
        Set<String> distinctCombinations = new HashSet<String>();
        for (List<String> combination : generator)
        {
            assert combination.size() == 2 : "Wrong combination length: " + combination.size();
            // Flatten to a single string for easier comparison.
            distinctCombinations.add(combination.get(0) + combination.get(1));
        }
        assert distinctCombinations.size() == 3 : "Wrong number of combinations: " + distinctCombinations.size();
    }



    /**
     * When generating a combination into an existing array, that
     * array must be big enough to hold the combination.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDestinationArrayTooShort()
    {
        CombinationGenerator<String> generator = new CombinationGenerator<String>(elements, 2);
        generator.nextCombinationAsArray(new String[1]); // Should throw an exception.
    }


    /**
     * When generating a combination into an existing array, that array should
     * not be bigger than required.  Otherwise subtle bugs may occur in programs
     * that use the combination generator when the end of the array contains nulls
     * or zeros.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDestinationArrayTooLong()
    {
        CombinationGenerator<String> generator = new CombinationGenerator<String>(elements, 2);
        generator.nextCombinationAsArray(new String[3]); // Should throw an exception.
    }


    /**
     * Combinations cannot contain duplicates, therefore the maximum length of a combination
     * is the size of the set of elements from which the combination is formed.  This test
     * ensures that an appropriate exception is thrown if an attempt is made to create a
     * generator that does not observe this constraint.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCombinationLengthTooLong()
    {
        new CombinationGenerator<String>(Arrays.asList(elements), 4);
    }


    /**
     * Zero-length combinations for an emtpy set of elements is not an error.
     * It should not return any combinations other than a single empty one.
     */
    @Test
    public void testZeroLength()
    {
        CombinationGenerator<String> generator = new CombinationGenerator<String>(Collections.<String>emptyList(), 0);
        assert generator.getTotalCombinations() == 1 : "Should be only one combination.";
        List<String> combination = generator.nextCombinationAsList();
        assert combination.isEmpty() : "Combination should be zero-length.";
        assert !generator.hasMore() : "Should be no more combinations.";
    }


    /**
     * If the initial parameters result in more than 2^63 combinations, then an
     * exception should be thrown as this is outside the range of a long.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTooManyCombinations()
    {
        Integer [] elements = new Integer[100];
        for (int i = 0; i < elements.length; i++)
        {
            elements[i] = i;
        }
        new CombinationGenerator<Integer>(elements, 40); // Should throw an exception.
    }


    @Test(dependsOnMethods = "testZeroLength",
          expectedExceptions = IllegalStateException.class)
    public void testExhaustion()
    {
        CombinationGenerator<String> generator = new CombinationGenerator<String>(Collections.<String>emptyList(), 0);
        generator.nextCombinationAsList(); // First one should succeed.
        generator.nextCombinationAsList(); // Second one should throw an exception.
    }


    /**
     * If the original set of elements is sorted, then all of the combinations should
     * be sorted as well since the algorithm does not re-order them, it just omits some when
     * creating each combination.
     */
    @Test
    public void testOrdering()
    {
        Integer[] elements = new Integer[]{1, 2, 3, 4, 5, 6};
        CombinationGenerator<Integer> generator = new CombinationGenerator<Integer>(elements, 4);
        for (List<Integer> combination : generator)
        {
            for (int i = 1; i < combination.size(); i++)
            {
                assert combination.get(i - 1) < combination.get(i) : "Combination is not ordered correctly.";
            }
        }
    }

}
