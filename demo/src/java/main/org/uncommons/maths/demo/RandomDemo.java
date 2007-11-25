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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import org.uncommons.swing.SwingBackgroundTask;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.NumberGenerator;

/**
 * @author Daniel Dyer
 */
public class RandomDemo extends JFrame
{
    private static final Random RANDOM = new MersenneTwisterRNG();

    private final DistributionPanel distributionPanel = new DistributionPanel();
    private final GraphPanel graphPanel = new GraphPanel();

    public RandomDemo()
    {
        super("Uncommons Maths - Random Numbers Demo");
        setLayout(new BorderLayout());
        add(createControls(), BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        validate();
    }


    private JComponent createControls()
    {
        JPanel controls = new JPanel(new BorderLayout());
        controls.add(distributionPanel, BorderLayout.CENTER);

        Box execution = new Box(BoxLayout.X_AXIS);
        execution.add(new JLabel("Iterations: "));
        final SpinnerNumberModel iterationsNumberModel = new SpinnerNumberModel(10000, 10, 100000, 1);
        execution.add(new JSpinner(iterationsNumberModel));
        JButton executeButton = new JButton("Go");
        executeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                new SwingBackgroundTask<int[]>()
                {
                    protected int[] performTask()
                    {
                        NumberGenerator<Integer> distribution
                            = (NumberGenerator<Integer>) distributionPanel.createProbabilityDistribution(RANDOM);
                        int iterations = iterationsNumberModel.getNumber().intValue();
                        int[] values = new int[iterations];
                        for (int i = 0; i < iterations; i++)
                        {
                            values[i] = distribution.nextValue();
                        }
                        return values;
                    }

                    protected void postProcessing(int[] result)
                    {
                        graphPanel.generateGraph(result);
                    }
                }.execute();
            }
        });
        execution.add(executeButton);
        execution.setBorder(BorderFactory.createTitledBorder("Execution"));
        controls.add(execution, BorderLayout.EAST);
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
}
