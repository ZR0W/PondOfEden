import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
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
        XYSeries series10 = new XYSeries("cycle10");
        for(int i = 0; i < creatures.size(); i++) {
            Double mv = creatures.get(i).getMV();
            Double spmv = statRecord.getSPMVHistoryAtTime(creatures.get(i), 10);
            series10.add(mv, spmv);
        }
        dataset.addSeries(series10);
        //another data series for comparison
        XYSeries series200 = new XYSeries("cycle200");
        for(int i = 0; i < creatures.size(); i++) {
            Double mv = creatures.get(i).getMV();
            Double spmv = statRecord.getSPMVHistoryAtTime(creatures.get(i), 200);
            series200.add(mv, spmv);
        }
        dataset.addSeries(series200);
        JFreeChart scatter = ChartFactory.createScatterPlot("MV vs SPMV Scatter plot", "MV", "SPMV", dataset);
//        ChartUtils.saveChartAsPNG(new File("C://Users/CodersLegacy/Desktop/histogram.png"), histogram, 600, 400);

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
                ChartPanel cp = new ChartPanel(scatter);

                frame.getContentPane().add(cp);
            }
        });
    }
}
