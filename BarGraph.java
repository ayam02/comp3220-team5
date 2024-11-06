import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BarGraph extends JPanel {
    private List<String> categories;
    private List<Integer> values;
    private Color barColor = new Color(70, 130, 180);  // Custom color for bars
    private Color gridColor = new Color(200, 200, 200); // Light gray for grid lines

    public BarGraph(List<String> categories, List<Integer> values) {
        this.categories = categories;
        this.values = values;
        setBackground(Color.WHITE); // Set background color for the panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 60;
        int labelPadding = 30;

        // Determine the scaling for the bar heights
        int maxValue = values.stream().max(Integer::compare).orElse(1);
        double scale = (height - 2 * padding - labelPadding) / (double) maxValue;

        // Width of each bar
        int barWidth = (width - 2 * padding) / values.size();

        // Draw horizontal grid lines and y-axis labels
        int numGridLines = 5;
        for (int i = 0; i <= numGridLines; i++) {
            int y = height - padding - (int) (i * (height - 2 * padding) / (double) numGridLines);
            g2.setColor(gridColor);
            g2.drawLine(padding, y, width - padding, y); // Draw grid line

            // Draw y-axis label
            g2.setColor(Color.BLACK);
            String yLabel = String.valueOf(maxValue * i / numGridLines);
            g2.drawString(yLabel, padding - labelPadding, y + 5);
        }

        // Draw bars with gradient color
        for (int i = 0; i < values.size(); i++) {
            int barHeight = (int) (values.get(i) * scale);
            int x = padding + i * barWidth;
            int y = height - padding - barHeight;

            // Create a gradient color for the bars
            GradientPaint gradient = new GradientPaint(x, y, barColor.brighter(), x, y + barHeight, barColor.darker());
            g2.setPaint(gradient);
            g2.fillRect(x, y, barWidth - 10, barHeight);  // Adjust bar width for spacing

            // Draw category label
            g2.setColor(Color.BLACK);
            g2.drawString(categories.get(i), x + (barWidth / 2) - 10, height - padding + labelPadding / 2);

            // Draw value label above bar
            g2.drawString(String.valueOf(values.get(i)), x + (barWidth / 2) - 10, y - 5);
        }

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2.drawLine(padding, height - padding, padding, padding);                  // Y-axis

        // Draw X and Y axis labels
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("Categories", width / 2, height - 15); // X-axis label
        g2.drawString("Values", 15, height / 2);             // Y-axis label
    }
}

