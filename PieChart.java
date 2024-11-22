import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * The PieChart class represents a custom pie chart component
 * that displays a pie chart with segments based on provided numbers and labels.
 * It includes features like percentage labels inside the slices and category labels outside the slices.
 */
public class PieChart extends Graph {
    private List<Float> numbers; // List of numbers representing the data for each pie slice
    private List<String> labels; // List of labels corresponding to each data entry

    /**
     * Constructor to initialize the PieChart with the provided numbers and labels.
     *
     * @param numbers A list of numbers representing the values for each pie slice
     * @param labels A list of labels corresponding to each data entry
     */
    public PieChart(List<Float> numbers, List<String> labels) {
        this.numbers = numbers;
        this.labels = labels;
        setBackground(Color.WHITE); // Set the background color of the chart
    }

    /**
     * Renders the graph by calling repaint to trigger the paintComponent method.
     */
    @Override
    public void renderGraph() {
        repaint();
    }

    /**
     * Paints the pie chart on the JPanel.
     * This method calculates the angles for each pie slice and draws them.
     *
     * @param g The Graphics object used for painting the pie chart.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) - 100; // Adjust for margins and labels
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        // Calculate the total value of the numbers
        float total = numbers.stream().reduce(0f, Float::sum);

        // Create a DecimalFormat to format the percentage labels
        DecimalFormat df = new DecimalFormat("#.#");

        // Draw each segment of the pie chart
        float startAngle = 0;
        for (int i = 0; i < numbers.size(); i++) {
            float percentage = numbers.get(i) / total;
            float angle = percentage * 360; // Calculate angle for each slice

            // Set the color for the slice
            g2.setColor(getColorForSlice(i));
            g2.fillArc(x, y, diameter, diameter, Math.round(startAngle), Math.round(angle));

            // Add a subtle separator line between slices
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawArc(x, y, diameter, diameter, Math.round(startAngle), Math.round(angle));

            // Draw percentage labels inside the slice
            drawPercentageLabel(g2, x, y, diameter, startAngle, angle, percentage, df);

            // Draw category labels outside the slice
            drawCategoryLabel(g2, x, y, diameter, startAngle, angle, labels.get(i));

            startAngle += angle; // Update the start angle for the next slice
        }
    }

    /**
     * Draws the percentage label inside the pie slice.
     */
    private void drawPercentageLabel(Graphics2D g2, int x, int y, int diameter, float startAngle, float angle,
                                      float percentage, DecimalFormat df) {
        float middleAngle = startAngle + angle / 2;
        int labelX = (int) (x + diameter / 2 + (diameter / 4.5) * Math.cos(Math.toRadians(middleAngle)));
        int labelY = (int) (y + diameter / 2 + (diameter / 4.5) * Math.sin(Math.toRadians(middleAngle)));

        String percentageLabel = df.format(percentage * 100) + "%";
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString(percentageLabel, labelX - g2.getFontMetrics().stringWidth(percentageLabel) / 2, labelY);
    }

    /**
     * Draws the category label outside the pie slice.
     */
    private void drawCategoryLabel(Graphics2D g2, int x, int y, int diameter, float startAngle, float angle,
                                    String label) {
        float middleAngle = startAngle + angle / 2;
        int labelX = (int) (x + diameter / 2 + (diameter / 1.9) * Math.cos(Math.toRadians(middleAngle)));
        int labelY = (int) (y + diameter / 2 + (diameter / 1.9) * Math.sin(Math.toRadians(middleAngle)));

        g2.setColor(new Color(51, 65, 85));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString(label, labelX - g2.getFontMetrics().stringWidth(label) / 2, labelY);
    }

    /**
     * Returns a color for a pie slice based on its index.
     * The colors are chosen from a predefined palette.
     *
     * @param index The index of the pie slice
     * @return The color associated with the slice
     */
    private Color getColorForSlice(int index) {
        Color[] colors = {
            new Color(69, 123, 157),   // Soft blue
            new Color(168, 218, 220),  // Light turquoise
            new Color(230, 57, 70),    // Coral red
            new Color(241, 136, 5),    // Bright orange
            new Color(128, 237, 153),  // Mint green
            new Color(146, 83, 161),   // Soft purple
            new Color(240, 138, 93),   // Peach
            new Color(86, 192, 204)    // Vibrant turquoise
        };
        return colors[index % colors.length];
    }

    /**
     * Launches a JFrame to display the pie chart.
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
