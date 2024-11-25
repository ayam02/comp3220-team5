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
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (numbers == null || numbers.isEmpty() || labels == null || labels.isEmpty()) {
            drawNoDataMessage(g2);
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) - 100;
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        float total = numbers.stream().reduce(0f, Float::sum);
        if (total == 0) {
            drawNoDataMessage(g2);
            return;
        }

        // Define colors for provinces
        Color[] colors = {
            new Color(69, 123, 157),
            new Color(168, 218, 220),
            new Color(241, 180, 187),
            new Color(147, 197, 114),
            new Color(230, 190, 138),
            new Color(177, 156, 217),
            new Color(255, 179, 186),
            new Color(152, 206, 180),
            new Color(215, 189, 226),
            new Color(255, 214, 165),
            new Color(176, 191, 226),
            new Color(171, 219, 227)
        };

        float currentAngle = 0;
        DecimalFormat df = new DecimalFormat("#.#");

        for (int i = 0; i < numbers.size(); i++) {
            float percentage = (numbers.get(i) / total) * 100;
            float arcAngle = (numbers.get(i) / total) * 360;

            // Draw slice
            g2.setColor(colors[i % colors.length]);
            g2.fillArc(x, y, diameter, diameter, (int) currentAngle, (int) arcAngle);

            // Draw percentage label
            double radian = Math.toRadians(currentAngle + arcAngle / 2);
            int labelX = x + diameter / 2 + (int) ((diameter / 3) * Math.cos(radian));
            int labelY = y + diameter / 2 + (int) ((diameter / 3) * Math.sin(radian));

            String label = labels.get(i) + " (" + df.format(percentage) + "%)";
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Inter", Font.PLAIN, 11));
            g2.drawString(label, labelX, labelY);

            currentAngle += arcAngle;
        }
    }

    private void drawNoDataMessage(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Inter", Font.BOLD, 14));
        String message = "No data available";
        FontMetrics fm = g2.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2.drawString(message, (getWidth() - messageWidth) / 2, getHeight() / 2);
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
