import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.AbstractBorder;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * A Swing-based view component that creates a modern dashboard interface for housing data visualization.
 * This class extends JFrame and implements a responsive UI with a sidebar navigation, charts, and statistics.
 */
public class View extends JFrame {

    // Inner class for RoundedBorder
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        /**
         * Constructs a rounded border with specified radius and color.
         * @param radius The corner radius in pixels
         * @param color The border color
         */
        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }

    private JPanel mainContent;
    private CardLayout cardLayout;
    private JPanel contentCards;
    private List<JPanel> navItems = new ArrayList<>();
    private JLabel headerTitle;
    private ViewHandler viewHandler;

    /**
     * Constructs a new View with the specified data for visualization.
     * @param viewHandler The ViewHandler instance
     */
    public View(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        DataController controller = viewHandler.getControllersList().get(0);
        
        // Get real data from controller
        List<String> cities = controller.getCityNames();
        List<Integer> fundingValues = controller.getFundingValues();
        
        // Sample data for charts (you'll need to add methods to get this data from your controller)
        List<Float> pieData = List.of(25f, 25f, 25f, 25f);
        List<String> pieLabels = List.of("Ontario", "British Columbia", "Nova Scotia", "Other");
        List<String> barCategories = List.of("Ontario", "BC", "NS", "Other");
        List<Integer> barValues = List.of(340, 31, 79, 150);

        registerFont();
        // Basic frame setup
        setTitle("OpenHome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        
        // Create modern sidebar
        JPanel sidebar = createModernSidebar();
        
        // Create main content area with CardLayout
        mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header with search
        JPanel headerPanel = createModernHeader();
        
        // Initialize CardLayout and content panel
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(Color.WHITE);
        
        // Add the main dashboard content
        JPanel dashboardContent = createDashboardContent(cities, fundingValues, pieData, pieLabels, 
                        barCategories, barValues);
        contentCards.add(dashboardContent, "Federal Housing Funds");
        
        // Add placeholder panels for other tabs
        contentCards.add(createComingSoonPanel(), "Future Housing Plan");
        contentCards.add(createComingSoonPanel(), "Housing Initiatives");
        contentCards.add(createComingSoonPanel(), "Available Housing");
        
        mainContent.add(headerPanel, BorderLayout.NORTH);
        mainContent.add(contentCards, BorderLayout.CENTER);
        
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
        
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Attempts to register the Inter font for use in the application.
     * Falls back to system fonts if the Inter font is not available.
     */
    private void registerFont() {
        try {
            // Try to register the Inter font if it exists in the system
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("path/to/Inter.ttf")));
        } catch (Exception e) {
            // If Inter font is not available, fall back to system fonts
            System.out.println("Inter font not available, using system fonts");
        }
    }
    
    /**
     * Creates the modern sidebar navigation panel.
     * @return JPanel containing the sidebar with logo and navigation items
     */
    private JPanel createModernSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245, 247, 250));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setPreferredSize(new Dimension(250, 0));
        
        // Create a wrapper panel for the logo with FlowLayout.LEFT
        JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoWrapper.setBackground(new Color(245, 247, 250));
        logoWrapper.setMaximumSize(new Dimension(250, 35));  // Control the height
        
        JLabel logo = new JLabel("OpenHome");
        logo.setFont(new Font("Inter", Font.BOLD, 24));
        
        logoWrapper.add(logo);
        sidebar.add(logoWrapper);
        sidebar.add(Box.createVerticalStrut(40));
        
        // Add modern nav items
        addModernNavItem(sidebar, "Federal Housing Funds", true);
        addModernNavItem(sidebar, "Future Housing Plan", false);
        addModernNavItem(sidebar, "Housing Initiatives", false);
        addModernNavItem(sidebar, "Available Housing", false);
        
        return sidebar;
    }
    
    /**
     * Adds a navigation item to the sidebar.
     * @param sidebar The sidebar panel to add the item to
     * @param text The text label for the navigation item
     * @param selected Whether this item should be initially selected
     */
    private void addModernNavItem(JPanel sidebar, String text, boolean selected) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        item.setMaximumSize(new Dimension(250, 45));
        item.setBackground(selected ? new Color(220, 235, 255) : new Color(245, 247, 250));
        item.setBorder(new RoundedBorder(10, new Color(200, 200, 200)));
        
        JLabel label = new JLabel(text);
        label.setForeground(selected ? new Color(30, 100, 255) : Color.DARK_GRAY);
        label.setFont(new Font("Inter", selected ? Font.BOLD : Font.PLAIN, 14));
        
        item.add(label);
        navItems.add(item);
        
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!item.getBackground().equals(new Color(220, 235, 255))) {
                    item.setBackground(new Color(235, 242, 255)); // Lighter hover color
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!item.getBackground().equals(new Color(220, 235, 255))) { // If not selected
                    item.setBackground(new Color(245, 247, 250));
                }
            }
            
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Update nav items visual state
                for (JPanel navItem : navItems) {
                    navItem.setBackground(new Color(245, 247, 250));
                    ((JLabel)navItem.getComponent(0)).setForeground(Color.DARK_GRAY);
                    ((JLabel)navItem.getComponent(0)).setFont(new Font("Inter", Font.PLAIN, 14));
                }
                
                // Update the clicked item
                item.setBackground(new Color(220, 235, 255));
                label.setForeground(new Color(30, 100, 255));
                label.setFont(new Font("Inter", Font.BOLD, 14));
                
                // Update header title based on selected tab
                switch(text) {
                    case "Federal Housing Funds":
                        headerTitle.setText("Federal Funding Towards Housing Across Various Cities in Canada");
                        break;
                    case "Future Housing Plan":
                        headerTitle.setText("Future Housing Development Plans and Projections");
                        break;
                    case "Housing Initiatives":
                        headerTitle.setText("Current Housing Initiatives and Programs");
                        break;
                    case "Available Housing":
                        headerTitle.setText("Available Housing Units and Properties");
                        break;
                }
                
                // Show the corresponding card
                cardLayout.show(contentCards, text);
            }
        });
        
        sidebar.add(item);
        sidebar.add(Box.createVerticalStrut(5));
    }
    
    /**
     * Creates the header panel with title.
     * @return JPanel containing the header elements
     */
    private JPanel createModernHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Create and store the title label
        headerTitle = new JLabel("Federal Funding Towards Housing Across Various Cities in Canada");
        headerTitle.setFont(new Font("Inter", Font.BOLD, 24));
        header.add(headerTitle, BorderLayout.WEST);
        
        return header;
    }
    
    /**
     * Creates a list of cities with their funding information.
     * @return JPanel containing the city list
     */
    private JPanel createModernCityList() {
        // Create a panel to hold the list
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        
        // Get data from controller
        DataController controller = viewHandler.getControllersList().get(0);
        List<String> cities = controller.getCityNames();
        List<Integer> fundingValues = controller.getFundingValues();
        
        // Create cards for each city
        for (int i = 0; i < cities.size(); i++) {
            String city = cities.get(i);
            String funding = "$" + fundingValues.get(i) + " Million";
            listPanel.add(createModernCityCard(city, funding));
            listPanel.add(Box.createVerticalStrut(15));
        }
        
        // Create a scroll pane and customize it
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(null);  // Remove border
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);  // Smooth scrolling
        
        // Create a wrapper panel to hold the scroll pane
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);
        
        return wrapperPanel;
    }
    
    /**
     * Creates a card component for displaying city information.
     * @param city The name of the city
     * @param funding The funding amount for the city
     * @return JPanel containing the city card
     */
    private JPanel createModernCityCard(String city, String funding) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(new Color(250, 252, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Create textPanel first and make it final
        final JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(new Color(250, 252, 255));
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(240, 245, 255));
                textPanel.setBackground(new Color(240, 245, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(250, 252, 255));
                textPanel.setBackground(new Color(250, 252, 255));
            }
        });

        JLabel cityLabel = new JLabel(city);
        cityLabel.setFont(new Font("Inter", Font.BOLD, 14));
        
        JLabel fundingLabel = new JLabel("Federal Funding: " + funding);
        fundingLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        fundingLabel.setForeground(Color.GRAY);
        
        textPanel.add(cityLabel);
        textPanel.add(fundingLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Creates a panel containing key statistics.
     * @return JPanel containing statistics cards
     */
    private JPanel createModernStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 20));
        panel.setBackground(Color.WHITE);
        
        panel.add(createModernStatCard("$3.7 Billion", "Total Federal Funding Budget", "📊"));
        panel.add(createModernStatCard("39.8 Million", "Total Canadian Population", "👥"));
        panel.add(createModernStatCard("687,271", "Total New Homes Over 10 Years", "🏠"));
        
        return panel;
    }
    
    /**
     * Creates a card component for displaying a statistic.
     * @param value The numerical value or metric
     * @param label The description of the statistic
     * @param icon The emoji icon to display
     * @return JPanel containing the statistic card
     */
    private JPanel createModernStatCard(String value, String label, String icon) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Layout with minimal gaps
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(3, 1, 0, 0)); // Removed vertical gap completely
        textPanel.setBackground(Color.WHITE);
        
        // Components remain the same, just closer together
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Inter", Font.PLAIN, 36));
        iconLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 24));
        valueLabel.setForeground(new Color(33, 37, 41));
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel descLabel = new JLabel(label);
        descLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        descLabel.setForeground(new Color(108, 117, 125));
        descLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        textPanel.add(iconLabel);
        textPanel.add(valueLabel);
        textPanel.add(descLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Creates a card container for content with a title.
     * @param content The component to be displayed in the card
     * @param title The title of the card
     * @return JPanel containing the card
     */
    private JPanel createCard(JComponent content, String title) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Add title with modern font
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 37, 41));
        card.add(titleLabel, BorderLayout.NORTH);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /**
     * Creates a placeholder panel for upcoming features.
     * @return JPanel containing the "Coming Soon" message
     */
    private JPanel createComingSoonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("Coming Soon...");
        label.setFont(new Font("Inter", Font.BOLD, 24));
        label.setForeground(new Color(150, 150, 150));
        
        panel.add(label);
        return panel;
    }

    /**
     * Creates the main dashboard content with charts and statistics.
     * @param cities List of city names
     * @param fundingValues List of funding amounts for cities
     * @param pieData List of values for pie chart segments
     * @param pieLabels List of labels for pie chart segments
     * @param barCategories List of categories for bar graph
     * @param barValues List of values for bar graph
     * @return JPanel containing the dashboard content
     */
    private JPanel createDashboardContent(List<String> cities, List<Integer> fundingValues, List<Float> pieData, List<String> pieLabels, List<String> barCategories, List<Integer> barValues) {
        // Get provincial data from controller
        DataController controller = viewHandler.getControllersList().get(0);
        Map<String, Integer> provincialFunding = controller.getProvincialFunding();
        
        // Convert to lists for charts
        List<String> provinceLabels = new ArrayList<>();
        List<Float> pieFunding = new ArrayList<>();
        List<Integer> barFunding = new ArrayList<>();
        
        // Convert funding values to millions and add to lists
        for (Map.Entry<String, Integer> entry : provincialFunding.entrySet()) {
            provinceLabels.add(entry.getKey());
            pieFunding.add(entry.getValue().floatValue());
            barFunding.add(entry.getValue());
        }
        
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Create left panel for city list
        JPanel citiesPanel = createModernCityList();
        citiesPanel.setPreferredSize(new Dimension(350, 0));
        citiesPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Create right panel for charts and stats
        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        rightPanel.setBackground(Color.WHITE);
        
        // Create charts panel with vertical layout
        JPanel chartsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        chartsPanel.setBackground(Color.WHITE);
        
        // Add charts with provincial data
        JPanel pieChartCard = createCard(new PieChart(pieFunding, provinceLabels), "Provincial Funding Distribution");
        chartsPanel.add(pieChartCard);
        
        JPanel barGraphCard = createCard(new BarGraph(provinceLabels, barFunding), "Funding by Province");
        chartsPanel.add(barGraphCard);
        
        rightPanel.add(chartsPanel);
        
        JPanel statsPanel = createModernStatsPanel();
        rightPanel.add(statsPanel);
        
        contentPanel.add(citiesPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }

    /**
     * Main method to launch the application with sample data.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ViewHandler viewHandler = new ViewHandler();
            DataController controller = new DataController("data/csv/Funding.csv");
            viewHandler.addController(controller);
            new View(viewHandler);
        });
    }
}
