import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel{

    private int width = 500;
    private int height = 500;

    /**
     * Called by the runtime system whenever the panel needs painting.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        World w = new World(width, height);
        ArrayList<Creature> c = w.getCreatures();
        for(int i = 0; i < c.size(); i++) {
            int outsideColorVal = (int)(c.get(i).getMV() * 25.5);
            Color outsideColor = new Color(100, outsideColorVal, outsideColorVal);
            System.out.println(outsideColorVal + outsideColor.toString());
            g.setColor(outsideColor);
            g.fillRect(c.get(i).getLocation()[0], c.get(i).getLocation()[1], 10, 10);

            int centerColor = (int)(c.get(i).getSPMV() * 25.5);
            g.setColor(new Color(200, centerColor, centerColor));
            g.fillOval(c.get(i).getLocation()[0], c.get(i).getLocation()[1], 10, 10);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var panel = new Main();
            panel.setBackground(Color.GREEN.darker());
            var frame = new JFrame("Eden");
            frame.setSize(panel.width, panel.height);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
