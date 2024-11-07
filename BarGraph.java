import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A custom JPanel that renders a bar graph visualization.
 * This component creates a modern-styled bar graph with customizable colors,
 * gradients, and grid lines. It supports dynamic scaling based on data values
 * and panel size.
 */
public class BarGraph extends JPanel {
    /** List of category labels for the x-axis */
    private List<String> categories;
    
    /** List of numerical values for the bars */
    private List<Integer> values;
    
    /** Background color of the graph panel */
    private Color backgroundColor = Color.WHITE;
    
    /** Color used for grid lines */
    private Color gridColor = new Color(226, 232, 240, 60);
    
    /** Array of colors used for the bars in sequence */
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
    
    /** Color used for the axes */
    private Color axisColor = new Color(71, 85, 105, 180);
    
    /** Color used for text elements */
    private Color textColor = new Color(51, 65, 85);

    /**
     * Constructs a new BarGraph with the specified data.
     * 
     * @param categories List of category labels for the x-axis
     * @param values List of numerical values for the bars
     */
    public BarGraph(List<String> categories, List<Integer> values) {
        this.categories = categories;
        this.values = values;
        setBackground(backgroundColor);
    }

    /**
     * Paints the bar graph component.
     * This method handles the rendering of:
     * - Grid lines and axis labels
     * - Bars with gradients
     * - Category labels
     * - Value labels
     * - Axes
     * 
     * @param g The Graphics object to paint on
     */
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
        drawGridLinesAndLabels(g2, width, height, padding, labelPadding, maxValue);

        // Draw bars with values and category labels
        drawBarsAndLabels(g2, width, height, padding, labelPadding, scale, barWidth);

        // Draw axes and their labels
        drawAxesAndLabels(g2, width, height, padding);
    }

    /**
     * Draws the grid lines and corresponding y-axis labels.
     */
    private void drawGridLinesAndLabels(Graphics2D g2, int width, int height, int padding, 
                                      int labelPadding, int maxValue) {
        int numGridLines = 5;
        for (int i = 0; i <= numGridLines; i++) {
            int y = height - padding - (int) (i * (height - 2 * padding) / (double) numGridLines);
            g2.setColor(gridColor);
            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 
                0, new float[]{5}, 0));
            g2.drawLine(padding, y, width - padding, y);

            g2.setColor(textColor);
            String yLabel = String.valueOf(maxValue * i / numGridLines);
            g2.drawString(yLabel, padding - labelPadding, y + 5);
        }
    }

    /**
     * Draws the bars with gradients, value labels, and category labels.
     */
    private void drawBarsAndLabels(Graphics2D g2, int width, int height, int padding, 
                                 int labelPadding, double scale, int barWidth) {
        // Use consistent font metrics
        g2.setFont(new Font("Inter", Font.PLAIN, 11));
        FontMetrics metrics = g2.getFontMetrics();
        
        for (int i = 0; i < values.size(); i++) {
            int barHeight = (int) (values.get(i) * scale);
            int x = padding + i * barWidth;
            int y = height - padding - barHeight;

            // Adjust bar width for better spacing
            int actualBarWidth = barWidth - 15;
            int barX = x + (barWidth - actualBarWidth) / 2;

            Color baseColor = barColors[i % barColors.length];
            GradientPaint gradient = new GradientPaint(
                barX, y, baseColor,
                barX, y + barHeight, 
                new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 245)
            );
            
            g2.setPaint(gradient);
            g2.fillRect(barX, y, actualBarWidth, barHeight);

            // Category label
            String category = categories.get(i);
            int labelWidth = metrics.stringWidth(category);
            g2.setColor(textColor);
            g2.drawString(category, 
                         x + (barWidth - labelWidth)/2, 
                         height - padding + labelPadding/2);

            // Value label
            g2.setFont(new Font("Inter", Font.BOLD, 12));
            String value = String.valueOf(values.get(i));
            labelWidth = metrics.stringWidth(value);
            g2.drawString(value, 
                         x + (barWidth - labelWidth)/2, 
                         y - 8);  // Increased spacing above bar
        }
    }

    /**
     * Draws the axes and their labels.
     */
    private void drawAxesAndLabels(Graphics2D g2, int width, int height, int padding) {
        g2.setColor(axisColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2.drawLine(padding, height - padding, padding, padding);                  // Y-axis

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(textColor);
        g2.drawString("Categories", width / 2, height - 15);
        
        // Rotate Y-axis label for better readability
        g2.rotate(-Math.PI / 2);
        g2.drawString("Values", -height / 2, 25);
        g2.rotate(Math.PI / 2);
    }
}

