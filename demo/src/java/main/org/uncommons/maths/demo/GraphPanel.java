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

import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Daniel Dyer
 */
class GraphPanel extends JPanel
{
    private final ChartPanel chartPanel = new ChartPanel(null);

    public GraphPanel()
    {
        super(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }


    public void generateGraph(String title,
                              Map<Integer, Double> observedValues,
                              Map<Integer, Double> expectedValues)
    {
        XYSeriesCollection dataSet = new XYSeriesCollection();
        XYSeries observedSeries = new XYSeries("Observed");
        dataSet.addSeries(observedSeries);
        XYSeries expectedSeries = new XYSeries("Expected");
        dataSet.addSeries(expectedSeries);

        for (Map.Entry<Integer, Double> entry : observedValues.entrySet())
        {
            observedSeries.add(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Integer, Double> entry : expectedValues.entrySet())
        {
            expectedSeries.add(entry.getKey(), entry.getValue());
        }


        JFreeChart chart = ChartFactory.createXYLineChart(title,
                                                          "Count",
                                                          "Frequency",
                                                          dataSet,
                                                          PlotOrientation.VERTICAL,
                                                          true,
                                                          false,
                                                          false);
        chartPanel.setChart(chart);
    }
}
