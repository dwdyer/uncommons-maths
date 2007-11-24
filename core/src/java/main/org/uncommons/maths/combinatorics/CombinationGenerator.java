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
package org.uncommons.maths.combinatorics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import org.uncommons.maths.Maths;

/**
 * Combination generator for generating all combinations for all sets up to
 * 20 elements in size.  The reason for the size restriction is in order to
 * provide the best possible performance for small sets by avoiding the need
 * to perform the internal arithmetic using {@link java.math.BigInteger} (which
 * would be necessary to calculate any factorial greater than 20!).
 * @param <T> The type of element that the combinations are made from.
 * @author Daniel Dyer (modified from the original version written by Michael
 * Gilleland of Merriam Park Software -
 * <a href="http://www.merriampark.com/perm.htm">http://www.merriampark.com/comb.htm</a>).
 * @see PermutationGenerator
 */
public class CombinationGenerator<T> implements Iterable<List<T>>
{
    // Maximum number of elements that can be combined by this class.
    private static final int MAX_SET_LENGTH = 20;

    private final T[] elements;
    private final int[] combinationIndices;
    private long remainingCombinations;
    private long totalCombinations;

    /**
     * Create a combination generator that generates all combinations of
     * a specified length from the given set.
     * @param elements The set from which to generate combinations.
     * @param combinationLength The length of the combinations to be generated.
     */
    public CombinationGenerator(T[] elements,
                                int combinationLength)
    {
        if (combinationLength > elements.length)
        {
            throw new IllegalArgumentException("Combination length cannot be greater than set size.");
        }
        if (elements.length > MAX_SET_LENGTH)
        {
            throw new IllegalArgumentException("Combination length must be less than or equal to 20.");
        }

        this.elements = elements.clone();
        this.combinationIndices = new int[combinationLength];

        long sizeFactorial = Maths.factorial(elements.length);
        long lengthFactorial = Maths.factorial(combinationLength);
        long differenceFactorial = Maths.factorial(elements.length - combinationLength);
        totalCombinations = sizeFactorial / (lengthFactorial * differenceFactorial);
        reset();
    }


    /**
     * Create a combination generator that generates all combinations of
     * a specified length from the given set.
     * @param elements The set from which to generate combinations.
     * @param combinationLength The length of the combinations to be generated.
     */
    @SuppressWarnings("unchecked")
    public CombinationGenerator(Collection<T> elements,
                                int combinationLength)
    {
        this(elements.toArray((T[]) new Object[elements.size()]),
             combinationLength);
    }


    /**
     * Reset the combination generator.
     */
    public final void reset()
    {
        for (int i = 0; i < combinationIndices.length; i++)
        {
            combinationIndices[i] = i;
        }
        remainingCombinations = totalCombinations;
    }


    /**
     * @return The number of combinations not yet generated.
     */
    public long getRemainingCombinations()
    {
        return remainingCombinations;
    }


    /**
     * Are there more combinations?
     * @return true if there are more combinations available, false otherwise.
     */
    public boolean hasMore()
    {
        return remainingCombinations > 0;
    }


    /**
     * @return The total number of combinations.
     */
    public long getTotalCombinations()
    {
        return totalCombinations;
    }


    /**
     * Generate the next combination and return an array containing
     * the appropriate elements.
     * @see #nextCombinationAsArray(Object[])
     * @see #nextCombinationAsList()
     * @return An array containing the elements that make up the next combination.
     */
    @SuppressWarnings("unchecked")
    public T[] nextCombinationAsArray()
    {
        T[] combination = (T[]) Array.newInstance(elements.getClass().getComponentType(),
                                                  combinationIndices.length);
        return nextCombinationAsArray(combination);
    }


    /**
     * Generate the next combination and return an array containing
     * the appropriate elements.  This overloaded method allows the caller
     * to provide an array that will be used and returned.
     * The purpose of this is to improve performance when iterating over
     * combinations.  If the {@link #nextCombinationAsArray()} method is
     * used it will create a new array every time.  When iterating over
     * combinations this will result in lots of short-lived objects that
     * have to be garbage collected.  This method allows a single array
     * instance to be reused in such circumstances.
     * @param destination Provides an array to use to create the
     * combination.  The specified array must be the same length as a
     * combination.
     * @return The provided array now containing the elements of the combination.
     */
    public T[] nextCombinationAsArray(T[] destination)
    {
        if (destination.length != combinationIndices.length)
        {
            throw new IllegalArgumentException("Destination array must be the same length as combinations.");
        }
        generateNextCombinationIndices();
        for (int i = 0; i < combinationIndices.length; i++)
        {
            destination[i] = elements[combinationIndices[i]];
        }
        return destination;
    }


    /**
     * Generate the next combination and return a list containing the
     * appropriate elements.
     * @see #nextCombinationAsList(List)
     * @see #nextCombinationAsArray()
     * @return A list containing the elements that make up the next combination.
     */
    public List<T> nextCombinationAsList()
    {
        return nextCombinationAsList(new ArrayList<T>(elements.length));
    }


    /**
     * Generate the next combination and return a list containing
     * the appropriate elements.  This overloaded method allows the caller
     * to provide a list that will be used and returned.
     * The purpose of this is to improve performance when iterating over
     * combinations.  If the {@link #nextCombinationAsList()} method is
     * used it will create a new list every time.  When iterating over
     * combinations this will result in lots of short-lived objects that
     * have to be garbage collected.  This method allows a single list
     * instance to be reused in such circumstances.
     * @param destination Provides a list to use to create the
     * combination.
     * @return The provided list now containing the elements of the combination.
     */
    public List<T> nextCombinationAsList(List<T> destination)
    {
        generateNextCombinationIndices();
        // Generate actual combination.
        destination.clear();
        for (int i : combinationIndices)
        {
            destination.add(elements[i]);
        }
        return destination;
    }



    /**
     * Generate the indices into the elements array for the next combination. The
     * algorithm is from Kenneth H. Rosen, Discrete Mathematics and Its Applications,
     * 2nd edition (NY: McGraw-Hill, 1991), p. 286.
     */
    private void generateNextCombinationIndices()
    {
        if (remainingCombinations == 0)
        {
            throw new IllegalStateException("There are no combinations remaining.  " +
                                            "Generator must be reset to continue using.");
        }
        else if (remainingCombinations < totalCombinations)
        {
            int i = combinationIndices.length - 1;
            while (combinationIndices[i] == elements.length - combinationIndices.length + i)
            {
                i--;
            }
            ++combinationIndices[i];
            for (int j = i + 1; j < combinationIndices.length; j++)
            {
                combinationIndices[j] = combinationIndices[i] + j - i;
            }
        }
        --remainingCombinations;
    }


    /**
     * <p>Provides a read-only iterator for iterating over the combinations
     * generated by this object.  This method is the implementation of the
     * {@link Iterable} interface that permits instances of this class to be
     * used with the new-style for loop.</p>
     * <p>For example:</p>
     * <pre>
     * List&lt;Integer&gt; elements = Arrays.asList(1, 2, 3);
     * CombinationGenerator&lt;Integer&gt; combinations = new CombinationGenerator(elements, 2);
     * for (List&lt;Integer&gt; c : combinations)
     * {
     *     // Do something with each combination.
     * }
     * </pre>
     * @return An iterator.
     * @since 1.1
     */
    public Iterator<List<T>> iterator()
    {
        return new Iterator<List<T>>()
        {
            public boolean hasNext()
            {
                return hasMore();
            }


            public List<T> next()
            {
                return nextCombinationAsList();
            }


            public void remove()
            {
                throw new UnsupportedOperationException("Iterator does not support removal.");
            }
        };
    }

}
