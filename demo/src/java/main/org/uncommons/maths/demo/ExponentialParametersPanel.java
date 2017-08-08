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
package org.uncommons.maths.demo;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import org.uncommons.swing.SpringUtilities;

/**
 * @author Daniel Dyer
 */
class ExponentialParametersPanel extends ParametersPanel
{
    private static final long serialVersionUID = 4873052029434918480L;
    private final SpinnerNumberModel rateNumberModel = new SpinnerNumberModel(2.0d, 0.1d, 100.0d, 0.1d);

    ExponentialParametersPanel()
    {
        JPanel wrapper = new JPanel(new SpringLayout());
        wrapper.add(new JLabel("Rate: "));
        wrapper.add(new JSpinner(rateNumberModel));
        SpringUtilities.makeCompactGrid(wrapper, 2, 1, 6, 6, 6, 6);
        add(wrapper, BorderLayout.NORTH);
    }


    public ExponentialDistribution createProbabilityDistribution()
    {
        return new ExponentialDistribution(rateNumberModel.getNumber().doubleValue());
    }
}
