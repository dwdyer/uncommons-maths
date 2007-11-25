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
package org.uncommons.maths.demo;

import org.uncommons.maths.NumberGenerator;
import java.util.Random;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * @author Daniel Dyer
 */
public abstract class AbstractProbabilityDistribution implements ProbabilityDistribution
{
    protected abstract NumberGenerator<Integer> createValueGenerator(Random rng);

    public Map<Integer, Double> generateValues(int count,
                                               Random rng)
    {
        NumberGenerator<Integer> generator = createValueGenerator(rng);
        Map<Integer, Double> values = new LinkedHashMap<Integer, Double>();
        for (int i = 0; i < count; i++)
        {
            int value = generator.nextValue();
            Double aggregate = values.get(value);
            aggregate = aggregate == null ? 0 : aggregate;
            values.put(value, ++aggregate);
        }
        for (Integer key : values.keySet())
        {
            values.put(key, values.get(key) / count);
        }
        return values;
    }
}
