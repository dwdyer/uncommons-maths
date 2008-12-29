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
package org.uncommons.maths.combinatorics;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

/**
 * Unit test for the {@link PermutationGenerator} class.
 * @author Daniel Dyer
 */
public class PermutationGeneratorTest
{
    private final String[] elements = new String[]{"1", "2", "3"};


    /**
     * This is the main test case and ensures that the permutation generator
     * produces the correct output.
     */
    @Test
    public void testPermutations()
    {
        PermutationGenerator<String> generator = new PermutationGenerator<String>(elements);
        assert generator.getTotalPermutations() == 6 : "Possible permutations should be 6.";
        assert generator.getRemainingPermutations() == 6 : "Remaining permutations should be 6.";

        String[] permutation1 = generator.nextPermutationAsArray();
        assert permutation1.length == 3 : "Permutation length should be 3.";
        assert generator.getRemainingPermutations() == 5 : "Remaining permutations should be 5.";
        assert !permutation1[0].equals(permutation1[1]) : "Permutation elements should be different.";
        assert !permutation1[0].equals(permutation1[2]) : "Permutation elements should be different.";
        assert !permutation1[1].equals(permutation1[2]) : "Permutation elements should be different.";

        List<String> permutation2 = generator.nextPermutationAsList();
        assert permutation2.size() == 3 : "Permutation length should be 3.";
        assert generator.getRemainingPermutations() == 4: "Remaining permutations should be 4.";
        // Make sure this combination is different from the previous one.
        assert !permutation2.get(0).equals(permutation2.get(1)) : "Permutation elements should be different.";
        assert !permutation2.get(0).equals(permutation2.get(2)) : "Permutation elements should be different.";
        assert !permutation2.get(1).equals(permutation2.get(2)) : "Permutation elements should be different.";

        String perm1String = permutation1[0] + permutation1[1] + permutation1[2];
        String perm2String = permutation2.get(0) + permutation2.get(1) + permutation2.get(2);
        assert !(perm1String).equals(perm2String) : "Permutation should be different from previous one.";
    }


    /**
     * Make sure that the permutation generator correctly calculates how many
     * permutations are remaining.
     */
    @Test
    public void testPermutationCount()
    {
        PermutationGenerator<String> generator = new PermutationGenerator<String>(elements);
        assert generator.getTotalPermutations() == 6 : "Possible permutations should be 6.";
        assert generator.hasMore() : "Generator should have more permutations available.";
        assert generator.getRemainingPermutations() == 6 : "Remaining permutations should be 6.";
        // Generate all of the permutations.
        List<String> temp = new LinkedList<String>();
        for (int i = 6; i > 0; i--)
        {
            generator.nextPermutationAsList(temp);
        }
        assert generator.getTotalPermutations() == 6 : "Total permutations should be unchanged.";
        assert generator.getRemainingPermutations() == 0 : "Remaining permutations should be zero.";
        assert !generator.hasMore() : "Should be no more permutations.";
    }


    /**
     * Ensures that the permutation generator works correctly with the "for-each" style
     * loop.
     */
    @Test
    public void testIterable()
    {
        PermutationGenerator<String> generator = new PermutationGenerator<String>(elements);
        Set<String> distinctPermutations = new HashSet<String>();
        for (List<String> permutation : generator)
        {
            assert permutation.size() == 3 : "Wrong permutation length: " + permutation.size();
            // Flatten to a single string for easier comparison.
            distinctPermutations.add(permutation.get(0) + permutation.get(1) + permutation.get(2));
        }
        assert distinctPermutations.size() == 6 : "Wrong number of permutations: " + distinctPermutations.size();
    }


    /**
     * When generating a permutation into an existing array, that
     * array must be big enough to hold the permutation.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDestinationArrayTooShort()
    {
        PermutationGenerator<String> generator = new PermutationGenerator<String>(elements);
        generator.nextPermutationAsArray(new String[2]); // Should throw an exception.
    }


    /**
     * When generating a permutation into an existing array, that array should
     * not be bigger than required.  Otherwise subtle bugs may occur in programs
     * that use the permutation generator when the end of the array contains nulls
     * or zeros.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDestinationArrayTooLong()
    {
        PermutationGenerator<String> generator = new PermutationGenerator<String>(elements);
        generator.nextPermutationAsArray(new String[4]); // Should throw an exception.
    }


    /**
     * Zero-length permutations for an emtpy set of elements is not an error.
     * It should not return any permutations other than a single empty one.
     */
    @Test
    public void testZeroLength()
    {
        PermutationGenerator<String> generator = new PermutationGenerator<String>(Collections.<String>emptyList());
        assert generator.getTotalPermutations() == 1 : "Should be only one permutation.";
        List<String> permutation = generator.nextPermutationAsList();
        assert permutation.isEmpty() : "Permutation should be zero-length.";
        assert !generator.hasMore() : "Should be no more permutations.";
    }


    @Test(dependsOnMethods = "testZeroLength",
          expectedExceptions = IllegalStateException.class)
    public void testExhaustion()
    {
        PermutationGenerator<String> generator = new PermutationGenerator<String>(Collections.<String>emptyList());
        generator.nextPermutationAsList(); // First one should succeed.
        generator.nextPermutationAsList(); // Second one should throw an exception.
    }
}
