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
import java.util.TreeMap;
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


    public void generateGraph(int[] values)
    {
        XYSeriesCollection dataSet = new XYSeriesCollection();
        XYSeries series = new XYSeries("Test");
        dataSet.addSeries(series);
        Map<Integer, Integer> valueCounts = new TreeMap<Integer, Integer>();
        for (int value : values)
        {
            Integer count = valueCounts.get(value);
            count = count == null ? 0 : count;
            valueCounts.put(value, ++count);
        }
        for (Map.Entry<Integer, Integer> entry : valueCounts.entrySet())
        {
            series.add(entry.getKey(), entry.getValue());
        }
        JFreeChart chart = ChartFactory.createXYLineChart("Distribution",
                                                          "Count",
                                                          "Frequency",
                                                          dataSet,
                                                          PlotOrientation.VERTICAL,
                                                          false,
                                                          false,
                                                          false);
        chartPanel.setChart(chart);
    }
}
