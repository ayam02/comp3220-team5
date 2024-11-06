import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class PieChart extends JPanel {
    private List<Float> numbers;
    private List<String> labels;

    public PieChart(List<Float> numbers, List<String> labels) {
        this.numbers = numbers;
        this.labels = labels;
        setBackground(Color.WHITE); // Set background color for the panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) - 80; // Increased margin for better spacing
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        // Calculate the total value of the numbers
        float total = 0;
        for (float num : numbers) {
            total += num;
        }

        // Create a DecimalFormat to format the percentage labels
        DecimalFormat df = new DecimalFormat("#.##");

        // Draw each segment of the pie chart
        float startAngle = 0;
        for (int i = 0; i < numbers.size(); i++) {
            float percentage = numbers.get(i) / total;
            float angle = percentage * 360; // Calculate angle for each slice

            // Set color for the slice
            g2.setColor(getColorForSlice(i));
            g2.fillArc(x, y, diameter, diameter, (int) startAngle, (int) angle); // Draw the slice

            // Remove thick outline, just add a subtle separator
            g2.setColor(new Color(255, 255, 255, 128)); // Semi-transparent white
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawArc(x, y, diameter, diameter, (int) startAngle, (int) angle);

            // Label for each slice (percentage inside the slice)
            String percentageLabel = df.format(percentage * 100) + "%";
            float middleAngle = startAngle + angle / 2;
            int labelX = (int) (x + diameter / 2 + (diameter / 4) * Math.cos(Math.toRadians(middleAngle)));
            int labelY = (int) (y + diameter / 2 + (diameter / 4) * Math.sin(Math.toRadians(middleAngle)));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));  // Larger font for percentage labels
            g2.drawString(percentageLabel, labelX - 20, labelY); // Draw percentage label inside slice

            // Label for each category (outside the slice)
            int labelOutsideX = (int) (x + diameter / 2 + (diameter / 2) * Math.cos(Math.toRadians(middleAngle)));
            int labelOutsideY = (int) (y + diameter / 2 + (diameter / 2) * Math.sin(Math.toRadians(middleAngle)));
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));  // Smaller font for category labels
            g2.drawString(labels.get(i), labelOutsideX + 10, labelOutsideY + 10); // Draw label text

            // Update the start angle for the next slice
            startAngle += angle;
        }
    }

    // Updated color method with a modern, pleasing palette
    private Color getColorForSlice(int index) {
        Color[] colors = {
            new Color(69, 123, 157),   // Soft blue
            new Color(29, 53, 87),     // Dark blue
            new Color(168, 218, 220),  // Light turquoise
            new Color(241, 250, 238),  // Soft white
            new Color(230, 57, 70),    // Coral red
            new Color(241, 136, 5),    // Orange
            new Color(128, 237, 153),  // Mint green
            new Color(146, 83, 161),   // Purple
            new Color(240, 138, 93),   // Peach
            new Color(86, 192, 204),   // Turquoise
            new Color(223, 120, 87),   // Terracotta
            new Color(105, 116, 175)   // Muted purple
        };
        return colors[index % colors.length];
    }

    // Main method to create and show the pie chart
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
