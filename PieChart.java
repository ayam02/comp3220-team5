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
        int diameter = Math.min(width, height) - 50; // Set the diameter of the pie chart
        int x = (width - diameter) / 2;  // X position for the pie chart
        int y = (height - diameter) / 2; // Y position for the pie chart

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

            // Draw outline for the slice
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));  // Stroke for the outline
            g2.drawArc(x, y, diameter, diameter, (int) startAngle, (int) angle); // Draw arc for outline

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

        // Draw a border around the pie chart for a neat outline
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(3));  // Thicker outline for the border
        g2.drawOval(x, y, diameter, diameter); // Draw circle border
    }

    // Method to generate colors for each slice
    private Color getColorForSlice(int index) {
        Color[] colors = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN,
            Color.MAGENTA, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY
        };
        return colors[index % colors.length]; // Cycle through colors if more slices than colors
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
