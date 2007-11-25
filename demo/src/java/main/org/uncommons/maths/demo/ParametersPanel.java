package org.uncommons.maths.demo;

import java.awt.FlowLayout;
import javax.swing.JPanel;

/**
 * @author Daniel Dyer
 */
abstract class ParametersPanel extends JPanel
{
    protected ParametersPanel()
    {
        super(new FlowLayout(FlowLayout.LEFT));
    }


    public abstract ProbabilityDistribution createProbabilityDistribution();
}
