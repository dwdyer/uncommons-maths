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

import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JSpinner;

/**
 * @author Daniel Dyer
 */
class GaussianParametersPanel extends ParametersPanel
{
    private final SpinnerNumberModel meanNumberModel = new SpinnerNumberModel(0, -1000, 1000, 1);
    private final SpinnerNumberModel deviationNumberModel = new SpinnerNumberModel(1d, 0.01d, 1000d, 1d);

    public GaussianParametersPanel()
    {
        add(new JLabel("Mean: "));
        add(new JSpinner(meanNumberModel));
        add(new JLabel("Standard Deviation: "));
        add(new JSpinner(deviationNumberModel));
    }


    public GaussianDistribution createProbabilityDistribution()
    {
        return new GaussianDistribution(meanNumberModel.getNumber().doubleValue(),
                                        deviationNumberModel.getNumber().doubleValue());
    }
}
