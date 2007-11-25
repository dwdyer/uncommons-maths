package org.uncommons.maths.demo;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.Random;
import javax.swing.JPanel;
import org.uncommons.maths.NumberGenerator;

/**
 * @author Daniel Dyer
 */
abstract class ParametersPanel extends JPanel
{
    protected ParametersPanel()
    {
        this(new FlowLayout(FlowLayout.LEFT));
    }


    protected ParametersPanel(LayoutManager layout)
    {
        super(layout);
    }

    public abstract NumberGenerator<?> createProbabilityDistribution(Random rng);
}
