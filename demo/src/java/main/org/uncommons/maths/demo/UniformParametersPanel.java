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
class UniformParametersPanel extends ParametersPanel
{
    private static final long serialVersionUID = 3332535142883292218L;

    private final SpinnerNumberModel minNumberModel = new SpinnerNumberModel(1, 0, 100, 1);
    private final SpinnerNumberModel maxNumberModel = new SpinnerNumberModel(10, 1, 100, 1);

    UniformParametersPanel()
    {
        JPanel wrapper = new JPanel(new SpringLayout());
        wrapper.add(new JLabel("Minimum: "));
        wrapper.add(new JSpinner(minNumberModel));
        wrapper.add(new JLabel("Maximum: "));
        wrapper.add(new JSpinner(maxNumberModel));
        SpringUtilities.makeCompactGrid(wrapper, 4, 1, 6, 6, 6, 6);
        add(wrapper, BorderLayout.NORTH);
    }


    public UniformDistribution createProbabilityDistribution()
    {
        return new UniformDistribution(minNumberModel.getNumber().intValue(),
                                       maxNumberModel.getNumber().intValue());
    }
}
