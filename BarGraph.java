import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BarGraph extends JPanel {
    private List<String> categories;
    private List<Integer> values;
    private Color backgroundColor = Color.WHITE;  // 
    private Color gridColor = new Color(226, 232, 240, 60);   // Even more subtle grid
    private Color[] barColors = {
        new Color(168, 218, 220),  // Pastel turquoise
        new Color(69, 123, 157),   // Soft blue
        new Color(241, 180, 187),  // Soft pink
        new Color(171, 219, 227),  // Light blue
        new Color(147, 197, 114),  // Sage green
        new Color(230, 190, 138),  // Soft orange
        new Color(177, 156, 217),  // Lavender
        new Color(255, 179, 186),  // Peach
        new Color(152, 206, 180),  // Mint
        new Color(215, 189, 226),  // Light purple
        new Color(255, 214, 165),  // Light orange
        new Color(176, 191, 226)   // Powder blue
    };
    private Color axisColor = new Color(71, 85, 105, 180);    // More subtle slate gray for axes
    private Color textColor = new Color(51, 65, 85);          // Dark slate for text

    public BarGraph(List<String> categories, List<Integer> values) {
        this.categories = categories;
        this.values = values;
        setBackground(backgroundColor);
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
            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 
                0, new float[]{5}, 0));  // Dotted line
            g2.drawLine(padding, y, width - padding, y);

            // Draw y-axis label with updated color
            g2.setColor(textColor);
            String yLabel = String.valueOf(maxValue * i / numGridLines);
            g2.drawString(yLabel, padding - labelPadding, y + 5);
        }

        // Draw bars with modern gradient
        for (int i = 0; i < values.size(); i++) {
            int barHeight = (int) (values.get(i) * scale);
            int x = padding + i * barWidth;
            int y = height - padding - barHeight;

            // Get base color for this bar
            Color baseColor = barColors[i % barColors.length];
            
            // Create subtle gradient
            GradientPaint gradient = new GradientPaint(
                x, y, baseColor,
                x, y + barHeight, 
                new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 245)
            );
            
            g2.setPaint(gradient);
            
            // Draw rectangular bar (no rounded corners)
            g2.fillRect(x, y, barWidth - 10, barHeight);

            // Draw category label
            g2.setColor(textColor);
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            FontMetrics metrics = g2.getFontMetrics();
            String category = categories.get(i);
            int labelWidth = metrics.stringWidth(category);
            g2.drawString(category, x + (barWidth - labelWidth)/2, height - padding + labelPadding/2);

            // Draw value label above bar
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String value = String.valueOf(values.get(i));
            labelWidth = metrics.stringWidth(value);
            g2.drawString(value, x + (barWidth - labelWidth)/2, y - 5);
        }

        // Draw axes with updated style
        g2.setColor(axisColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2.drawLine(padding, height - padding, padding, padding);                  // Y-axis

        // Draw axis labels with updated style
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(textColor);
        g2.drawString("Categories", width / 2, height - 15);
        
        // Rotate Y-axis label for better readability
        g2.rotate(-Math.PI / 2);
        g2.drawString("Values", -height / 2, 25);
        g2.rotate(Math.PI / 2);
    }
}

