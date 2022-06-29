import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
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
        double[] populationSPMVAtTime = new double[creatures.size()];
        int time = 50;
        for(int i = 0; i < creatures.size(); i++) {
            Double spmv = statRecord.getSPMVHistoryAtTime(creatures.get(i), time);
            populationSPMVAtTime[i] = spmv.doubleValue();
        }

        //TODO: histogram isn't the best option; maybe scatter plot? Maybe price volume chart, whatever that is?
        //Create JFreeChart to display data
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", populationSPMVAtTime, 20);
        JFreeChart histogram = ChartFactory.createHistogram("JFreeChart Historgram from ChartTest.java", "Creature", "SPMV", dataset);
//        ChartUtils.saveChartAsPNG(new File("C://Users/CodersLegacy/Desktop/histogram.png"), histogram, 600, 400);

        //Java UI to display graph
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("chart");

                frame.setSize(600, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                ChartPanel cp = new ChartPanel(histogram);

                frame.getContentPane().add(cp);
            }
        });
    }
}