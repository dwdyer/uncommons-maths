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
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * GUI component for selecting a probability distribution.  Displays appropriate
 * configuration options for each distribution.
 * @author Daniel Dyer
 */
class DistributionPanel extends JPanel
{
    private final SortedMap<String, ParametersPanel> parameterPanels = new TreeMap<String, ParametersPanel>();
    private final JComboBox distributionCombo = new JComboBox();

    {
        parameterPanels.put("Binomial", new BinomialParametersPanel());
        parameterPanels.put("Exponential", new ExponentialParametersPanel());
        parameterPanels.put("Gaussian", new GaussianParametersPanel());
        parameterPanels.put("Poisson", new PoissonParametersPanel());
        parameterPanels.put("Uniform", new UniformParametersPanel());
    }

    
    public DistributionPanel()
    {
        super(new BorderLayout());
        final CardLayout parametersLayout = new CardLayout();
        final JPanel parametersPanel = new JPanel(parametersLayout);
        for (Map.Entry<String, ParametersPanel> entry : parameterPanels.entrySet())
        {
            distributionCombo.addItem(entry.getKey());
            parametersPanel.add(entry.getValue(), entry.getKey());
        }
        parametersLayout.first(parametersPanel);

        distributionCombo.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent itemEvent)
            {
                parametersLayout.show(parametersPanel,
                                      (String) distributionCombo.getSelectedItem());
            }
        });

        add(distributionCombo, BorderLayout.NORTH);
        add(parametersPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("Probability Distribution"));
    }


    public ProbabilityDistribution createProbabilityDistribution()
    {
        ParametersPanel panel = parameterPanels.get(distributionCombo.getSelectedItem().toString());
        return panel.createProbabilityDistribution();
    }
}
