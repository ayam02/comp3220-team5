import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The LineGraph class represents a customizable line graph component.
 * It supports drawing grid lines, axis labels, and a line connecting data points.
 */
public class LineGraph extends Graph {
    private List<Integer> xValues;
    private List<Integer> yValues;
    private Color lineColor = new Color(44, 102, 230, 180); // Semi-transparent blue for line
    private Color pointColor = new Color(220, 20, 60);      // Crimson for points
    private Color gridColor = new Color(200, 200, 200);     // Light gray for grid lines

    /**
     * Constructor to initialize the LineGraph with x and y values.
     *
     * @param xValues List of x-axis values
     * @param yValues List of y-axis values
     */
    public LineGraph(List<Integer> xValues, List<Integer> yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
        setBackground(Color.WHITE); // Set background color for the graph
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int labelPadding = 30;

        // Find the maximum x and y values for scaling
        int maxX = xValues.stream().max(Integer::compare).orElse(1);
        int maxY = yValues.stream().max(Integer::compare).orElse(1);

        // Calculate scaling factors
        double xScale = (width - 2.0 * padding) / maxX;
        double yScale = (height - 2.0 * padding - labelPadding) / maxY;

        // Draw grid lines and y-axis labels
        int numGridLines = 5;
        for (int i = 0; i <= numGridLines; i++) {
            int y = height - padding - (int) (i * (height - 2.0 * padding - labelPadding) / numGridLines);
            g2.setColor(gridColor);
            g2.drawLine(padding, y, width - padding, y); // Horizontal grid line

            // Draw y-axis label
            g2.setColor(Color.BLACK);
            String yLabel = String.valueOf(maxY * i / numGridLines);
            g2.drawString(yLabel, padding - labelPadding, y + 5);
        }

        // Draw x-axis labels
        for (int i = 0; i < xValues.size(); i++) {
            int x = padding + (int) (xValues.get(i) * xScale);
            g2.setColor(Color.BLACK);
            String xLabel = String.valueOf(xValues.get(i));
            g2.drawString(xLabel, x - g2.getFontMetrics().stringWidth(xLabel) / 2, height - padding + labelPadding / 2);
        }

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2.drawLine(padding, height - padding, padding, padding);                  // Y-axis

        // Draw the line graph
        g2.setColor(lineColor);
        g2.setStroke(new BasicStroke(2f));
        for (int i = 0; i < xValues.size() - 1; i++) {
            int x1 = padding + (int) (xValues.get(i) * xScale);
            int y1 = height - padding - (int) (yValues.get(i) * yScale);
            int x2 = padding + (int) (xValues.get(i + 1) * xScale);
            int y2 = height - padding - (int) (yValues.get(i + 1) * yScale);
            g2.drawLine(x1, y1, x2, y2);
        }

        // Draw data points
        g2.setColor(pointColor);
        for (int i = 0; i < xValues.size(); i++) {
            int x = padding + (int) (xValues.get(i) * xScale);
            int y = height - padding - (int) (yValues.get(i) * yScale);
            g2.fillOval(x - 4, y - 4, 8, 8); // Draw each point as a circle
        }

        // Draw axis labels
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("X Axis", width / 2, height - 10); // X-axis label
        g2.drawString("Y Axis", 15, height / 2);         // Y-axis label
    }

    /**
     * Render the graph by showing it in a JFrame.
     */
    public void renderGraph() {
        JFrame frame = new JFrame("Line Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this); // Add this LineGraph component to the frame
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Launches a JFrame to display the line graph.
     *
     * @param xValues List of x-axis values
     * @param yValues List of y-axis values
     */
    public static void createAndShowGui(List<Integer> xValues, List<Integer> yValues) {
        LineGraph lineGraph = new LineGraph(xValues, yValues);
        lineGraph.renderGraph(); // Call the renderGraph method
    }
}
