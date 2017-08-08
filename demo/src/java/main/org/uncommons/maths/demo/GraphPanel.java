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
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Daniel Dyer
 */
class GraphPanel extends JPanel
{
    private static final long serialVersionUID = 7221055190103508945L;

    private final ChartPanel chartPanel = new ChartPanel(null);

    GraphPanel()
    {
        super(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }


    public void generateGraph(String title,
                              Map<Double, Double> observedValues,
                              Map<Double, Double> expectedValues,
                              double expectedMean,
                              double expectedStandardDeviation,
                              boolean discrete)
    {
        XYSeriesCollection dataSet = new XYSeriesCollection();
        XYSeries observedSeries = new XYSeries("Observed");
        dataSet.addSeries(observedSeries);
        XYSeries expectedSeries = new XYSeries("Expected");
        dataSet.addSeries(expectedSeries);

        for (Entry<Double, Double> entry : observedValues.entrySet())
        {
            observedSeries.add(entry.getKey(), entry.getValue());
        }

        for (Entry<Double, Double> entry : expectedValues.entrySet())
        {
            expectedSeries.add(entry.getKey(), entry.getValue());
        }


        JFreeChart chart = ChartFactory.createXYLineChart(title,
                                                          "Value",
                                                          "Probability",
                                                          dataSet,
                                                          PlotOrientation.VERTICAL,
                                                          true,
                                                          false,
                                                          false);
        XYPlot plot = (XYPlot) chart.getPlot();
        if (discrete)
        {
            // Render markers at each data point (these discrete points are the
            // distibution, not the lines between them).
            plot.setRenderer(new XYLineAndShapeRenderer());
        }
        else
        {
            // Render smooth lines between points for a continuous distribution.
            XYSplineRenderer renderer = new XYSplineRenderer();
            renderer.setBaseShapesVisible(false);
            plot.setRenderer(renderer);
        }

        chartPanel.setChart(chart);
    }
}
