import javax.swing.*;
import java.awt.*;
import java.util.List;

public class View extends JFrame {

    public View(List<Integer> xValues, List<Integer> yValues, List<Float> pieData, List<String> pieLabels, List<String> barCategories, List<Integer> barValues) {
        // Basic frame setup
        setTitle("Federal Housing Funds Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        
        // Create modern sidebar
        JPanel sidebar = createModernSidebar();
        
        // Create main content area
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header with search
        JPanel headerPanel = createModernHeader();
        
        // Create content panel with cards
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        
        // Create left panel for city list
        JPanel citiesPanel = createModernCityList();
        citiesPanel.setPreferredSize(new Dimension(400, 0));
        
        // Create right panel for charts and stats
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        rightPanel.setBackground(Color.WHITE);
        
        // Add charts panel
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBackground(Color.WHITE);
        
        // Create modern card for pie chart
        JPanel pieChartCard = createCard(new PieChart(pieData, pieLabels), "Funding Distribution");
        chartsPanel.add(pieChartCard);
        
        // Create modern card for bar graph
        JPanel barGraphCard = createCard(new BarGraph(barCategories, barValues), "Funding by Region");
        chartsPanel.add(barGraphCard);
        
        rightPanel.add(chartsPanel);
        
        // Add statistics cards
        JPanel statsPanel = createModernStatsPanel();
        rightPanel.add(statsPanel);
        
        // Assemble the panels
        contentPanel.add(citiesPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);
        
        mainContent.add(headerPanel, BorderLayout.NORTH);
        mainContent.add(contentPanel, BorderLayout.CENTER);
        
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
        
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createModernSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245, 247, 250));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setPreferredSize(new Dimension(250, 0));
        
        // Add logo
        JLabel logo = new JLabel("Logo");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(40));
        
        // Add modern nav items
        addModernNavItem(sidebar, "Federal Housing Funds", true);
        addModernNavItem(sidebar, "Future Housing Plan", false);
        addModernNavItem(sidebar, "Housing Initiatives", false);
        addModernNavItem(sidebar, "Available Housing", false);
        
        return sidebar;
    }
    
    private void addModernNavItem(JPanel sidebar, String text, boolean selected) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        item.setMaximumSize(new Dimension(250, 45));
        item.setBackground(selected ? new Color(230, 240, 255) : new Color(245, 247, 250));
        
        JLabel label = new JLabel(text);
        label.setForeground(selected ? new Color(30, 100, 255) : Color.DARK_GRAY);
        label.setFont(new Font("Arial", selected ? Font.BOLD : Font.PLAIN, 14));
        
        item.add(label);
        sidebar.add(item);
        sidebar.add(Box.createVerticalStrut(5));
    }
    
    private JPanel createModernHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Title
        JLabel title = new JLabel("Federal Funding Towards Housing Across Various Cities in Canada");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        
        JTextField searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"Sort by: Default"});
        sortBox.setBackground(Color.WHITE);
        
        searchPanel.add(searchField);
        searchPanel.add(sortBox);
        
        header.add(title, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createModernCityList() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        String[] cities = {
            "London, Ontario|$74 Million",
            "Vaughan, Ontario|$59 Million",
            "Hamilton, Ontario|$93.5 Million",
            "Halifax, Nova Scotia|$79.3 Million",
            "Brampton, Ontario|$114 Million",
            "Kelowna, British Columbia|$31.5 Million"
        };
        
        for (String cityData : cities) {
            String[] parts = cityData.split("\\|");
            panel.add(createModernCityCard(parts[0], parts[1]));
            panel.add(Box.createVerticalStrut(10));
        }
        
        return panel;
    }
    
    private JPanel createModernCityCard(String city, String funding) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(new Color(250, 252, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel cityLabel = new JLabel(city);
        cityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel fundingLabel = new JLabel("Federal Funding: " + funding);
        fundingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        fundingLabel.setForeground(Color.GRAY);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(new Color(250, 252, 255));
        textPanel.add(cityLabel);
        textPanel.add(fundingLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createModernStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(Color.WHITE);
        
        panel.add(createModernStatCard("$3.7 Billion", "Total Federal Funding Budget", "📊"));
        panel.add(createModernStatCard("39.8 Million", "Total Canadian Population", "👥"));
        panel.add(createModernStatCard("687,271", "Total New Homes Over 10 Years", "🏠"));
        
        return panel;
    }
    
    private JPanel createModernStatCard(String value, String label, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(label);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(descLabel);
        
        return card;
    }
    
    private JPanel createCard(JComponent component, String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(component, BorderLayout.CENTER);
        
        return card;
    }

    public static void main(String[] args) {
        // Sample data for the line graph
        List<Integer> xValues = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> yValues = List.of(5, 9, 6, 7, 8, 6, 7, 10, 9, 8);

        // Sample data for the pie chart
        List<Float> pieData = List.of(10f, 20f, 30f, 40f);
        List<String> pieLabels = List.of("Category 1", "Category 2", "Category 3", "Category 4");

        // Sample data for the bar graph
        List<String> barCategories = List.of("A", "B", "C", "D", "E");
        List<Integer> barValues = List.of(5, 9, 3, 7, 6);

        // Invoke the Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new View(xValues, yValues, pieData, pieLabels, barCategories, barValues); // Create the View with sample data
        });
    }
}
