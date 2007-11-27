package org.uncommons.maths.demo;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 * @author Daniel Dyer
 */
abstract class ParametersPanel extends JPanel
{
    protected ParametersPanel()
    {
        super(new BorderLayout());
    }


    public abstract ProbabilityDistribution createProbabilityDistribution();
}
