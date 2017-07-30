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

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import org.uncommons.maths.random.AESCounterRNG;
import org.uncommons.maths.random.CMWC4096RNG;
import org.uncommons.maths.random.CellularAutomatonRNG;
import org.uncommons.maths.random.JavaRNG;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.XORShiftRNG;
import org.uncommons.swing.SpringUtilities;

/**
 * Controls for selecing a random number generator and a number of values
 * to generate.
 * @author Daniel Dyer
 */
class RNGPanel extends JPanel
{

  private static final long serialVersionUID = 6371975789624655739L;
  private final JComboBox<String> rngCombo = new JComboBox<>();
    private final SpinnerNumberModel iterationsNumberModel = new SpinnerNumberModel(10000, 10, 1000000, 100);

    private final SortedMap<String, Random> rngs = new TreeMap<String, Random>();
    {
        try
        {
            rngs.put("AES", new AESCounterRNG());
            rngs.put("Cellular Automaton", new CellularAutomatonRNG());
            rngs.put("CMWC 4096", new CMWC4096RNG());
            rngs.put("JDK RNG", new JavaRNG());
            rngs.put("Mersenne Twister", new MersenneTwisterRNG());
            rngs.put("SecureRandom", new SecureRandom());
            rngs.put("XOR Shift", new XORShiftRNG());
        }
        catch (GeneralSecurityException ex)
        {
            throw new IllegalStateException("Failed to initialise RNGs.", ex);
        }
    }


    RNGPanel()
    {
        super(new SpringLayout());
        for (String name : rngs.keySet())
        {
            rngCombo.addItem(name);
        }
        rngCombo.setSelectedIndex(3); // Mersenne Twister.
        add(rngCombo);
        add(new JLabel("No. Values: "));
        add(new JSpinner(iterationsNumberModel));
        setBorder(BorderFactory.createTitledBorder("RNG"));
        SpringUtilities.makeCompactGrid(this, 3, 1, 6, 6, 6, 6);
    }


    public Random getRNG()
    {
        return rngs.get(rngCombo.getSelectedItem());
    }


    /**
     * Returns the number of values to be generated, as specified by the user.
     */
    public int getIterations()
    {
        return iterationsNumberModel.getNumber().intValue();
    }
}
