import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Value;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChartTest {

    public static void main(String[] args) throws IOException {
        //world testing taken from Wrold.java main function

        //init world
        World world = new World();
        ArrayList<Creature> creatures = world.getCreatures();
        //init statRecord with initial world state
        StatRecord statRecord = new StatRecord(creatures);
        for(Creature c : creatures) {
            statRecord.addEntry(c, null, false, c.getMV(), c.getSPMV());
        }
        int epoch = 200;
        for(int i = 0; i < epoch; i++) {
            //for every creature give them a partner
            //A will make a decision on if it wants to mate. B will accept or not
            for(Creature foo : creatures) {
                Creature pursuingTarget = world.getPotentialMateFor(foo);
                boolean result = world.interact(foo, pursuingTarget);
                statRecord.addEntry(foo, pursuingTarget, result, foo.getMV(), foo.getSPMV());
            }
            System.out.println("epoch " + i + " =============================");
            world.getStats();
        }

        //JFreeChart display
        //Create JFreeChart to display data
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries dataSeries = new XYSeries("cycle");
        for(int i = 0; i < creatures.size(); i++) {
            Double mv = creatures.get(i).getMV();
            Double spmv = statRecord.getSPMVHistoryAtTime(creatures.get(i), 0);
            dataSeries.add(mv, spmv);
        }
        dataset.addSeries(dataSeries);
        JFreeChart scatter = ChartFactory.createScatterPlot("MV vs SPMV Scatter plot", "MV", "SPMV", dataset);

        //Java UI to display graph
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("chart");

                frame.setSize(700, 700);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                XYPlot plot = (XYPlot)scatter.getPlot();
                plot.setBackgroundPaint(new Color(250, 200, 200));
                ValueAxis domainAxis = plot.getDomainAxis();
                ValueAxis rangeAxis = plot.getRangeAxis();
                domainAxis.setRange(0.0, 10.0);
                rangeAxis.setRange(0.0, 10.0);
                ChartPanel cp = new ChartPanel(scatter);

                frame.getContentPane().add(cp, BorderLayout.CENTER);

                //add button
                JPanel controlPanel = new JPanel();

                JTextField textField = new JTextField(5);
                textField.setText("0");
                controlPanel.add(textField, BorderLayout.WEST);
                controlPanel.add(new JButton(new AbstractAction("Next") {
                    int activeCycle = 0;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dataSeries.clear();
                        if(!textField.getText().equals(String.valueOf(activeCycle))) {
                            activeCycle = Integer.valueOf(textField.getText());
                        }else{
                            activeCycle++;
                        }
                        textField.setText(String.valueOf(activeCycle));
                        for(int i = 0; i < creatures.size(); i++) {
                            Double mv = creatures.get(i).getMV();
                            Double spmv = statRecord.getSPMVHistoryAtTime(creatures.get(i), activeCycle);
                            dataSeries.add(mv, spmv);
                        }
                    }
                }));
                frame.getContentPane().add(controlPanel, BorderLayout.EAST);
            }
        });
    }
}
