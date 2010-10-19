// ============================================================================
//   Copyright 2006-2010 Daniel W. Dyer
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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.uncommons.swing.SwingBackgroundTask;

/**
 * Demo application that demonstrates the generation of random values using
 * different probability distributions.
 * @author Daniel Dyer
 */
public class RandomDemo extends JFrame
{
    private final DistributionPanel distributionPanel = new DistributionPanel();
    private final RNGPanel rngPanel = new RNGPanel();
    private final GraphPanel graphPanel = new GraphPanel();

    public RandomDemo()
    {
        super("Uncommons Maths - Random Numbers Demo");
        setLayout(new BorderLayout());
        add(createControls(), BorderLayout.WEST);
        add(graphPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setMinimumSize(new Dimension(500, 250));
        validate();
    }


    private JComponent createControls()
    {
        Box controls = new Box(BoxLayout.Y_AXIS);
        controls.add(distributionPanel);
        controls.add(rngPanel);

        JButton executeButton = new JButton("Go");
        executeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                RandomDemo.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                new SwingBackgroundTask<GraphData>()
                {
                    private ProbabilityDistribution distribution;

                    protected GraphData performTask()
                    {
                        distribution = distributionPanel.createProbabilityDistribution();

                        Map<Double, Double> observedValues = distribution.generateValues(rngPanel.getIterations(),
                                                                                         rngPanel.getRNG());
                        Map<Double, Double> expectedValues = distribution.getExpectedValues();
                        return new GraphData(observedValues,
                                             expectedValues,
                                             distribution.getExpectedMean(),
                                             distribution.getExpectedStandardDeviation());
                    }

                    protected void postProcessing(GraphData data)
                    {
                        graphPanel.generateGraph(distribution.getDescription(),
                                                 data.getObservedValues(),
                                                 data.getExpectedValues(),
                                                 data.getExpectedMean(),
                                                 data.getExpectedStandardDeviation(),
                                                 distribution.isDiscrete());
                        RandomDemo.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }.execute();
            }
        });
        controls.add(executeButton);
        return controls;
    }

    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new RandomDemo().setVisible(true);
            }
        });
    }


    private static class GraphData
    {
        private final Map<Double, Double> observedValues;
        private final Map<Double, Double> expectedValues;
        private final double expectedMean;
        private final double expectedStandardDeviation;


        public GraphData(Map<Double, Double> observedValues,
                         Map<Double, Double> expectedValues,
                         double expectedMean,
                         double expectedStandardDeviation)
        {
            this.observedValues = observedValues;
            this.expectedValues = expectedValues;
            this.expectedMean = expectedMean;
            this.expectedStandardDeviation = expectedStandardDeviation;
        }


        public Map<Double, Double> getObservedValues()
        {
            return observedValues;
        }


        public Map<Double, Double> getExpectedValues()
        {
            return expectedValues;
        }


        public double getExpectedMean()
        {
            return expectedMean;
        }

        public double getExpectedStandardDeviation()
        {
            return expectedStandardDeviation;
        }
    }
}
