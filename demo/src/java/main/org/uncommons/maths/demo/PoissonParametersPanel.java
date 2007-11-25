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
class PoissonParametersPanel extends ParametersPanel
{
    private final SpinnerNumberModel meanNumberModel = new SpinnerNumberModel(5.0d, 0.1d, 100.0d, 0.1d);

    public PoissonParametersPanel()
    {
        add(new JLabel("Mean: "));
        add(new JSpinner(meanNumberModel));
    }


    public PoissonDistribution createProbabilityDistribution()
    {
        return new PoissonDistribution(meanNumberModel.getNumber().doubleValue());
    }
}
