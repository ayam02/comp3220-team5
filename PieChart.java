import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * A custom graph component that renders a modern pie chart visualization.
 * Extends the Graph class to provide specific pie chart functionality with
 * features like percentage labels inside slices and category labels outside.
 */
public class PieChart extends Graph {
    /** List of numerical values for each pie slice */
    private List<Float> numbers;
    
    /** List of labels corresponding to each pie slice */
    private List<String> labels;
    
    /** Color palette for pie slices */
    private static final Color[] SLICE_COLORS = {
        new Color(69, 123, 157),   // Blue
        new Color(168, 218, 220),  // Light turquoise
        new Color(241, 180, 187),  // Pink
        new Color(147, 197, 114),  // Green
        new Color(230, 190, 138),  // Orange
        new Color(177, 156, 217),  // Purple
        new Color(255, 179, 186),  // Coral
        new Color(152, 206, 180),  // Mint
        new Color(215, 189, 226),  // Lavender
        new Color(255, 214, 165),  // Peach
        new Color(176, 191, 226),  // Light blue
        new Color(171, 219, 227)   // Sky blue
    };

    /**
     * Constructs a new PieChart with the specified data.
     * 
     * @param numbers List of numerical values for each pie slice
     * @param labels List of labels corresponding to each slice
     * @throws IllegalArgumentException if numbers and labels lists are not the same size
     */
    public PieChart(List<Float> numbers, List<String> labels) {
        this.numbers = numbers;
        this.labels = labels;
        setBackground(Color.WHITE);
    }

    /**
     * Triggers a repaint of the pie chart component.
     * Called when the chart needs to be redrawn due to data or size changes.
     */
    @Override
    public void renderGraph() {
        repaint();
    }

    /**
     * Renders the complete pie chart with all its components.
     * Handles the drawing of slices, percentage labels, and category labels.
     * 
     * @param g The Graphics object to paint on
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Check for valid data
        if (numbers == null || numbers.isEmpty() || labels == null || labels.isEmpty()) {
            drawNoDataMessage(g2);
            return;
        }

        // Calculate dimensions
        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) - 100;
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        // Calculate total for percentages
        float total = numbers.stream().reduce(0f, Float::sum);
        if (total == 0) {
            drawNoDataMessage(g2);
            return;
        }

        // Draw pie slices and labels
        float currentAngle = 0;
        DecimalFormat df = new DecimalFormat("#.#");

        for (int i = 0; i < numbers.size(); i++) {
            float percentage = (numbers.get(i) / total) * 100;
            float arcAngle = (numbers.get(i) / total) * 360;

            // Draw slice
            g2.setColor(SLICE_COLORS[i % SLICE_COLORS.length]);
            g2.fillArc(x, y, diameter, diameter, (int) currentAngle, (int) arcAngle);

            // Calculate and draw label position
            double radian = Math.toRadians(currentAngle + arcAngle / 2);
            int labelX = x + diameter / 2 + (int) ((diameter / 3) * Math.cos(radian));
            int labelY = y + diameter / 2 + (int) ((diameter / 3) * Math.sin(radian));

            // Draw label with percentage
            String label = labels.get(i) + " (" + df.format(percentage) + "%)";
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Inter", Font.PLAIN, 11));
            g2.drawString(label, labelX, labelY);

            currentAngle += arcAngle;
        }
    }

    /**
     * Draws a message when no data is available to display.
     * 
     * @param g2 Graphics context for drawing
     */
    private void drawNoDataMessage(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Inter", Font.BOLD, 14));
        String message = "No data available";
        FontMetrics fm = g2.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2.drawString(message, 
            (getWidth() - messageWidth) / 2, 
            getHeight() / 2);
    }

    /**
     * Creates and displays a pie chart in a new window.
     * 
     * @param numbers List of numerical values for each pie slice
     * @param labels List of labels corresponding to each slice
     */
    public static void createAndShowGui(List<Float> numbers, List<String> labels) {
        PieChart pieChart = new PieChart(numbers, labels);
        pieChart.setPreferredSize(new Dimension(800, 600));

        JFrame frame = new JFrame("Pie Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(pieChart);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
