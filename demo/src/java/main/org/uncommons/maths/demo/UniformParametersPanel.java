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

import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.uncommons.maths.random.DiscreteUniformGenerator;

/**
 * @author Daniel Dyer
 */
class UniformParametersPanel extends ParametersPanel
{
    private final SpinnerNumberModel minNumberModel = new SpinnerNumberModel(1, 0, 100, 1);
    private final SpinnerNumberModel maxNumberModel = new SpinnerNumberModel(10, 1, 100, 1);

    public UniformParametersPanel()
    {
        add(new JLabel("Minimum: "));
        add(new JSpinner(minNumberModel));
        add(new JLabel("Maximum: "));
        add(new JSpinner(maxNumberModel));
    }


    public DiscreteUniformGenerator createProbabilityDistribution(Random rng)
    {
        return new DiscreteUniformGenerator(minNumberModel.getNumber().intValue(),
                                            maxNumberModel.getNumber().intValue(),
                                            rng);
    }
}
