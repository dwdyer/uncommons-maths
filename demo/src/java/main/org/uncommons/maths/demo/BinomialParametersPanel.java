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

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * @author Daniel Dyer
 */
class BinomialParametersPanel extends ParametersPanel
{
    private final SpinnerNumberModel trialsNumberModel = new SpinnerNumberModel(50, 1, 1000, 1);
    private final SpinnerNumberModel probabilityNumberModel = new SpinnerNumberModel(0.5d, 0.0d, 1.0d, 0.01d);

    public BinomialParametersPanel()
    {
        add(new JLabel("No. Trials: "));
        JSpinner trialsSpinner = new JSpinner(trialsNumberModel);
        add(trialsSpinner);
        add(new JLabel("Probability: "));
        JSpinner probabilitySpinner = new JSpinner(probabilityNumberModel);
        // Size of this spinner doesn't seem to get set sensibly, so we make
        // it the same size as the other one.
        probabilitySpinner.setPreferredSize(trialsSpinner.getPreferredSize());
        add(probabilitySpinner);
    }

    
    public BinomialDistribution createProbabilityDistribution()
    {
        return new BinomialDistribution(trialsNumberModel.getNumber().intValue(),
                                        probabilityNumberModel.getNumber().doubleValue());
    }
}
