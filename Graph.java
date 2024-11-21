import javax.swing.*;
import java.awt.*;

/**
 * The Graph class serves as a base class for different types of graphs
 * like PieChart and LineGraph. It provides common features and behaviors
 * for graph visualization in a Swing application.
 */
public abstract class Graph extends JPanel {

    /**
     * Constructor to set up default configurations for the graph panel.
     */
    public Graph() {
        setBackground(Color.WHITE); // Default background color
    }

    /**
     * Abstract method to be implemented by subclasses to draw the specific graph.
     *
     * @param g The Graphics object used for drawing
     */
    @Override
    protected abstract void paintComponent(Graphics g);

    /**
     * Abstract method to render the graph. Subclasses should implement this to
     * trigger the repaint process, which will call paintComponent().
     */
    public abstract void renderGraph();

    /**
     * Utility method to display the graph in a JFrame.
     *
     * @param title The title of the window
     * @param width The width of the window
     * @param height The height of the window
     */
    public void displayGraph(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
    }
}
