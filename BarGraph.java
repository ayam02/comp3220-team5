import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A custom graph component that renders a modern bar graph visualization.
 * Extends the Graph class to provide specific bar chart functionality with
 * customizable styling including gradients, grid lines, and dynamic scaling.
 */
public class BarGraph extends Graph {
    /** Category labels displayed along the x-axis */
    private List<String> categories;
    
    /** Numerical values represented by each bar */
    private List<Integer> values;
    
    /** Background color of the graph panel */
    private Color backgroundColor = Color.WHITE;
    
    /** Color used for grid lines */
    private Color gridColor = new Color(226, 232, 240, 60);
    
    /** Sequential colors used for bars, providing a consistent visual theme */
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
    
    /** Color used for x and y axes */
    private Color axisColor = new Color(71, 85, 105, 180);
    
    /** Color used for all text elements */
    private Color textColor = new Color(51, 65, 85);

    /**
     * Constructs a new BarGraph with the specified data.
     * 
     * @param categories List of category labels for the x-axis
     * @param values List of numerical values for the bars
     * @throws IllegalArgumentException if categories and values lists are not the same size
     */
    public BarGraph(List<String> categories, List<Integer> values) {
        this.categories = categories;
        this.values = values;
        setBackground(backgroundColor);
    }

    /**
     * Triggers a repaint of the graph component.
     * Called when the graph needs to be redrawn due to data or size changes.
     */
    @Override
    public void renderGraph() {
        repaint();
    }

    /**
     * Renders the complete bar graph with all its components.
     * Handles the drawing of grid lines, bars, labels, and axes.
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

        if (values == null || values.isEmpty() || categories == null || categories.isEmpty()) {
            drawNoDataMessage(g2, width, height);
            return;
        }

        int maxValue = values.stream().max(Integer::compare).orElse(1);
        double scale = (double)(height - 2 * padding - labelPadding) / maxValue;
        int barWidth = (width - 2 * padding) / values.size();

        drawGridLinesAndLabels(g2, width, height, padding, labelPadding, maxValue);
        drawBarsAndLabels(g2, width, height, padding, labelPadding, scale, barWidth);
        drawAxesAndLabels(g2, width, height, padding);
    }

    /**
     * Draws grid lines and their corresponding value labels.
     * 
     * @param g2 Graphics context
     * @param width Total width of the component
     * @param height Total height of the component
     * @param padding Padding from the edges
     * @param labelPadding Additional padding for labels
     * @param maxValue Maximum value in the dataset
     */
    private void drawGridLinesAndLabels(Graphics2D g2, int width, int height, int padding, 
                                      int labelPadding, int maxValue) {
        int numGridLines = 5;
        for (int i = 0; i <= numGridLines; i++) {
            int y = height - padding - (int) (i * (height - 2 * padding) / (double) numGridLines);
            
            // Draw dashed grid line
            g2.setColor(gridColor);
            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 
                0, new float[]{5}, 0));
            g2.drawLine(padding, y, width - padding, y);

            // Draw value label
            g2.setColor(textColor);
            String yLabel = String.valueOf(maxValue * i / numGridLines);
            g2.drawString(yLabel, padding - labelPadding, y + 5);
        }
    }

    /**
     * Draws the bars, their value labels, and category labels.
     * 
     * @param g2 Graphics context
     * @param width Total width of the component
     * @param height Total height of the component
     * @param padding Padding from the edges
     * @param labelPadding Additional padding for labels
     * @param scale Scaling factor for bar heights
     * @param barWidth Width of each bar
     */
    private void drawBarsAndLabels(Graphics2D g2, int width, int height, int padding, 
                                 int labelPadding, double scale, int barWidth) {
        g2.setFont(new Font("Inter", Font.PLAIN, 11));
        FontMetrics metrics = g2.getFontMetrics();
        
        for (int i = 0; i < values.size(); i++) {
            int barHeight = (int) (values.get(i) * scale);
            int x = padding + i * barWidth;
            int y = height - padding - barHeight;

            int actualBarWidth = barWidth - 15;
            int barX = x + (barWidth - actualBarWidth) / 2;

            // Create and draw gradient bar
            Color baseColor = barColors[i % barColors.length];
            GradientPaint gradient = new GradientPaint(
                barX, y, baseColor,
                barX, y + barHeight, 
                new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 245)
            );
            
            g2.setPaint(gradient);
            g2.fillRect(barX, y, actualBarWidth, barHeight);

            // Draw category label
            String category = categories.get(i);
            int labelWidth = metrics.stringWidth(category);
            g2.setColor(textColor);
            g2.drawString(category, 
                         x + (barWidth - labelWidth)/2, 
                         height - padding + labelPadding/2);

            // Draw value label
            g2.setFont(new Font("Inter", Font.BOLD, 12));
            String value = String.valueOf(values.get(i));
            labelWidth = metrics.stringWidth(value);
            g2.drawString(value, 
                         x + (barWidth - labelWidth)/2, 
                         y - 8);
        }
    }

    /**
     * Draws the x and y axes with their labels.
     * 
     * @param g2 Graphics context
     * @param width Total width of the component
     * @param height Total height of the component
     * @param padding Padding from the edges
     */
    private void drawAxesAndLabels(Graphics2D g2, int width, int height, int padding) {
        g2.setColor(axisColor);
        g2.setStroke(new BasicStroke(1.5f));
        
        // Draw axes
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2.drawLine(padding, height - padding, padding, padding);                  // Y-axis

        // Draw axis labels
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(textColor);
        g2.drawString("Categories", width / 2, height - 15);
        
        // Rotate for y-axis label
        g2.rotate(-Math.PI / 2);
        g2.drawString("Values", -height / 2, 25);
        g2.rotate(Math.PI / 2);
    }

    /**
     * Draws a message when no data is available to display.
     * 
     * @param g2 Graphics context
     * @param width Total width of the component
     * @param height Total height of the component
     */
    private void drawNoDataMessage(Graphics2D g2, int width, int height) {
        g2.setColor(textColor);
        g2.setFont(new Font("Inter", Font.BOLD, 14));
        String message = "No data available";
        FontMetrics metrics = g2.getFontMetrics();
        int messageWidth = metrics.stringWidth(message);
        g2.drawString(message, 
            (width - messageWidth) / 2, 
            height / 2);
    }
}

