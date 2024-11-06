import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LineGraph extends JPanel {
    private List<Integer> xValues;
    private List<Integer> yValues;
    private Color lineColor = Color.BLUE;
    private Color pointColor = Color.RED;
    private Color gridColor = new Color(200, 200, 200);

    public LineGraph(List<Integer> xValues, List<Integer> yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
        setBackground(Color.WHITE); // Set background color for the panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int labelPadding = 30;

        // Find the maximum x and y values to scale the graph
        int maxX = xValues.stream().max(Integer::compare).orElse(1);
        int maxY = yValues.stream().max(Integer::compare).orElse(1);

        // Calculate scaling factors for x and y axes
        double xScale = (width - 2 * padding) / (double) maxX;
        double yScale = (height - 2 * padding - labelPadding) / (double) maxY;

        // Draw grid lines and y-axis labels
        int numGridLines = 5;
        for (int i = 0; i <= numGridLines; i++) {
            int y = height - padding - (int) (i * (height - 2 * padding) / (double) numGridLines);
            g2.setColor(gridColor);
            g2.drawLine(padding, y, width - padding, y); // Draw grid line

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
            g2.drawString(xLabel, x - 10, height - padding + labelPadding / 2);
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
            g2.fillOval(x - 4, y - 4, 8, 8);
        }

        // Draw axis labels
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("X Axis", width / 2, height - 10); // X-axis label
        g2.drawString("Y Axis", 15, height / 2);         // Y-axis label
    }
}


