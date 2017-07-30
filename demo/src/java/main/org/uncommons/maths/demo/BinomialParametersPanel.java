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
class BinomialParametersPanel extends ParametersPanel
{

  private static final long serialVersionUID = 5622619456049233544L;
  private final SpinnerNumberModel trialsNumberModel = new SpinnerNumberModel(50, 1, 100, 1);
    private final SpinnerNumberModel probabilityNumberModel = new SpinnerNumberModel(0.5d, 0.0d, 1.0d, 0.01d);

    BinomialParametersPanel()
    {
        JPanel wrapper = new JPanel(new SpringLayout());
        wrapper.add(new JLabel("No. Trials: "));
        wrapper.add(new JSpinner(trialsNumberModel));
        wrapper.add(new JLabel("Probability: "));
        wrapper.add(new JSpinner(probabilityNumberModel));
        SpringUtilities.makeCompactGrid(wrapper, 4, 1, 6, 6, 6, 6);
        add(wrapper, BorderLayout.NORTH);
    }

    
    public BinomialDistribution createProbabilityDistribution()
    {
        return new BinomialDistribution(trialsNumberModel.getNumber().intValue(),
                                        probabilityNumberModel.getNumber().doubleValue());
    }
}
