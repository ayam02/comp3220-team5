import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.AbstractBorder;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

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
    private JPanel mapPanel; // Placeholder for the map
    private JPanel graphPanel; // Panel for the graph and stats
    private JButton toggleButton; // Button to toggle between map and graph
    private JPanel cityInfoPanel; // Panel to display city information
    private boolean isShowingMap = true;  // Track current view state

    /**
     * Constructs a new View with the specified data for visualization.
     * @param viewHandler The ViewHandler instance
     */
    public View(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        this.navItems = new ArrayList<>();
        
        // Get initial data
        DataController controller = viewHandler.getController("Funding Insights");
        List<String> cities = controller.getCityNames();
        List<Integer> fundingValues = controller.getFundingValues();
        
        // Set up the GUI
        createAndShowGUI(cities, fundingValues);
        setupFrame();
        
        // Trigger initial data load
        updateMapAndGraphViews(controller, true);
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
                
                // Update header title and content based on selected tab
                switch(text) {
                    case "Federal Housing Funds":
                        headerTitle.setText("Federal Funding Towards Housing Across Various Cities in Canada");
                        DataController controller = viewHandler.getController("Funding Insights");
                        updateMapAndGraphViews(controller, true);
                        cardLayout.show(contentCards, "Map View");
                        toggleButton.setText("Switch to Graph View");
                        toggleButton.setVisible(true);
                        cityInfoPanel.setVisible(true);
                        displayDefaultCityInfo();
                        isShowingMap = true;
                        break;
                    case "Future Housing Plan":
                        headerTitle.setText("Future Housing Development Plans and Projections");
                        controller = viewHandler.getController("Housing Starts Insights");
                        updateMapAndGraphViews(controller, false);
                        cardLayout.show(contentCards, "Map View");
                        toggleButton.setText("Switch to Graph View");
                        toggleButton.setVisible(true);
                        cityInfoPanel.setVisible(true);
                        displayDefaultCityInfo();
                        isShowingMap = true;
                        break;
                    case "Housing Initiatives":
                    case "Available Housing":
                        headerTitle.setText(text.equals("Housing Initiatives") ? 
                            "Current Housing Initiatives and Programs" : 
                            "Available Housing Units and Properties");
                        contentCards.removeAll();
                        contentCards.add(createComingSoonPanel(), "Coming Soon");
                        cardLayout.show(contentCards, "Coming Soon");
                        toggleButton.setVisible(false);
                        cityInfoPanel.setVisible(false);
                        contentCards.revalidate();
                        contentCards.repaint();
                        break;
                }
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
        
        // Create a panel for the title and toggle button
        JPanel titlePanel = new JPanel(new BorderLayout(20, 0));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(headerTitle, BorderLayout.CENTER);
        
        toggleButton = new JButton("Switch to Graph View");
        toggleButton.setBounds(275, 60, 250, 30);
        toggleButton.addActionListener(e -> {
            if (toggleButton.getText().equals("Switch to Graph View")) {
                toggleButton.setText("Switch to Map View");
                cardLayout.show(contentCards, "Graph View");
            } else {
                toggleButton.setText("Switch to Graph View");
                cardLayout.show(contentCards, "Map View");
                displayDefaultCityInfo();
            }
        });
        add(toggleButton);
        
        // Show map view by default
        cardLayout.show(contentCards, "Map View");
        
        // Update the bounds of the main content to accommodate the toggle button
        mainContent.setBounds(250, 0, 950, 800 - 30); // Adjust height to account for toggle button
        
        header.add(titlePanel, BorderLayout.WEST);
        
        return header;
    }
    
    /**
     * Creates a list of cities with their funding information.
     * @return JPanel containing the city list
     */
    private JPanel createModernCityList(List<String> cities, List<Integer> fundingValues) {
        // Create a panel to hold the list
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
    
        // Create a panel for the Sort buttons
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setBackground(Color.WHITE);
    
        // Create the Ascending Sort button
        JButton sortAscendingButton = new JButton("Ascending");
        sortAscendingButton.setFont(new Font("Inter", Font.BOLD, 14));
        sortPanel.add(sortAscendingButton);
    
        // Create the Descending Sort button
        JButton sortDescendingButton = new JButton("Descending");
        sortDescendingButton.setFont(new Font("Inter", Font.BOLD, 14));
        sortPanel.add(sortDescendingButton);
    
        // Add sortPanel to the top
        listPanel.add(sortPanel);
        listPanel.add(Box.createVerticalStrut(15)); // Add space after the Sort buttons
    
        // Create city cards
        List<JPanel> cityCards = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            String city = cities.get(i);
            String funding;
            if (headerTitle.getText().contains("Federal Funding")) {    
                funding = "$" + fundingValues.get(i) + " Million";
            } else {
                funding = fundingValues.get(i) + " New Homes";
            }
            cityCards.add(createModernCityCard(city, funding));
        }
    
        // Add city cards to the list panel
        for (JPanel cityCard : cityCards) {
            listPanel.add(cityCard);
            listPanel.add(Box.createVerticalStrut(15));
        }
    
        // ActionListener for the Ascending button
        sortAscendingButton.addActionListener(e -> {
            // Sort city cards in ascending order
            sortCityCards(cityCards, true); // Ascending order
            rebuildCityList(listPanel, sortPanel, cityCards);
        });
    
        // ActionListener for the Descending button
        sortDescendingButton.addActionListener(e -> {
            // Sort city cards in descending order
            sortCityCards(cityCards, false); // Descending order
            rebuildCityList(listPanel, sortPanel, cityCards);
        });
    
        // Create a scroll pane and customize it
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(null); // Remove border
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
    
        // Create a wrapper panel to hold the scroll pane
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);
    
        return wrapperPanel;
    }
     
    /**
 * Helper method to rebuild the city list after sorting.
 */
private void rebuildCityList(JPanel listPanel, JPanel sortPanel, List<JPanel> cityCards) {
    listPanel.removeAll();
    listPanel.add(sortPanel);
    listPanel.add(Box.createVerticalStrut(15)); // Re-add space after the Sort buttons
    for (JPanel cityCard : cityCards) {
        listPanel.add(cityCard);
        listPanel.add(Box.createVerticalStrut(15));
    }

    // Refresh the view
    listPanel.revalidate();
    listPanel.repaint();
}

    
     
    /**
     * Creates a card component for displaying city information.
     * @param city The name of the city
     * @param value The funding amount for the city
     * @return JPanel containing the city card
     */
    private JPanel createModernCityCard(String city, String value) {
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
        
        // Update the funding label based on selected tab
        String labelPrefix = headerTitle.getText().contains("Federal Funding") ? 
            "Federal Funding: " : "Future Homes: ";
        
        JLabel valueLabel;
        if (value.equals("No Info")) {
            valueLabel = new JLabel("No Info available at this time");
        } else {
            valueLabel = new JLabel(labelPrefix + value);
        }
        
        valueLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        valueLabel.setForeground(Color.GRAY);
        
        textPanel.add(cityLabel);
        textPanel.add(valueLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    

     private void sortCityCards(List<JPanel> cityCards, boolean ascending) {
        // Get the corresponding data from controller for sorting
        DataController controller = viewHandler.getControllersList().get(0);
        List<Integer> fundingValues = controller.getFundingValues();
        
        // Create a map to store funding values for each city
        Map<String, Integer> cityFundingMap = new HashMap<>();
        List<String> cities = controller.getCityNames();
        
        for (int i = 0; i < cities.size(); i++) {
            cityFundingMap.put(cities.get(i), fundingValues.get(i));
        }
        
        // Sort the city cards based on funding values
        Collections.sort(cityCards, (card1, card2) -> {
            String city1 = ((JLabel)((JPanel)card1.getComponent(0)).getComponent(0)).getText();
            String city2 = ((JLabel)((JPanel)card2.getComponent(0)).getComponent(0)).getText();
            
            int funding1 = cityFundingMap.getOrDefault(city1, 0);
            int funding2 = cityFundingMap.getOrDefault(city2, 0);
            
            return ascending ? funding1 - funding2 : funding2 - funding1;
        });
    }

    /**
     * Creates a panel containing key statistics.
     * @return JPanel containing statistics cards
     */
    private JPanel createModernStatsPanel() {
        // Panel for holding statistics cards
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 20));
        panel.setBackground(Color.WHITE);    
        
        panel.add(createModernStatCard("$3.7 Billion", "Total Federal Funding Budget", "📊"));
        panel.add(createModernStatCard("39.8 Million", "Total Canadian Population", "👥"));
        panel.add(createModernStatCard("687,271", "Total New Homes Over 10 Years", ""));
    
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
    private JPanel createDashboardContent(
        List<String> cities, 
        List<Integer> values,
        List<Float> pieData,
        List<String> pieLabels,
        List<String> barCategories,
        List<Integer> barValues) {
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 20));
        contentPanel.setBackground(Color.WHITE);

        // Left panel - City list
        JPanel citiesPanel = createModernCityList(cities, values);
        citiesPanel.setPreferredSize(new Dimension(300, 0));
        contentPanel.add(citiesPanel, BorderLayout.WEST);

        // Middle panel - Graphs
        JPanel graphsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        graphsPanel.setBackground(Color.WHITE);
        graphsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        // Check if we have data before creating charts
        if (pieData != null && !pieData.isEmpty() && pieLabels != null && !pieLabels.isEmpty()) {
            // Add pie chart with provincial distribution
            PieChart pieChart = new PieChart(pieData, pieLabels);
            JPanel pieChartCard = createGraphCard(pieChart, "Distribution by Province");
            graphsPanel.add(pieChartCard);
        }

        if (barCategories != null && !barCategories.isEmpty() && barValues != null && !barValues.isEmpty()) {
            // Add bar graph with provincial data
            BarGraph barGraph = new BarGraph(barCategories, barValues);
            JPanel barGraphCard = createGraphCard(barGraph, "Provincial Breakdown");
            graphsPanel.add(barGraphCard);
        }

        contentPanel.add(graphsPanel, BorderLayout.CENTER);

        // Right panel - Stats
        JPanel statsPanel = createModernStatsPanel();
        statsPanel.setPreferredSize(new Dimension(250, 0));
        contentPanel.add(statsPanel, BorderLayout.EAST);

        return contentPanel;
    }

    /**
     * Creates a placeholder panel for the map with city markers.
     * @return JPanel containing the map with city markers
     */
    private JPanel createMapPanel() {
        JPanel panel = new JPanel(null); // Use null layout for absolute positioning
        panel.setBackground(Color.WHITE);

        // Load the image
        ImageIcon mapImage = new ImageIcon("Canada_blank_map.svg.png");

        // Scale the image to fit within the desired dimensions
        Image scaledImage = mapImage.getImage().getScaledInstance(900, 650, Image.SCALE_SMOOTH);
        ImageIcon scaledMapImage = new ImageIcon(scaledImage);

        // Create a label with the scaled image
        JLabel imageLabel = new JLabel(scaledMapImage);
        imageLabel.setBounds(0, 0, 900, 650); // Set bounds for the map image
        panel.add(imageLabel);

        // Add city markers with adjusted coordinates
        addCityMarker(panel, "Toronto", 602, 595);
        addCityMarker(panel, "Vancouver", 73, 468);
        addCityMarker(panel, "Calgary", 190, 480);
        addCityMarker(panel, "Edmonton", 200, 450);
        addCityMarker(panel, "Ottawa", 630, 560);
        addCityMarker(panel, "London", 573, 615);
        addCityMarker(panel, "Vaughan", 599, 589);
        addCityMarker(panel, "Hamilton", 595, 605);
        addCityMarker(panel, "Halifax", 786, 520);
        addCityMarker(panel, "Brampton", 594, 592);
        addCityMarker(panel, "Kelowna", 120, 470);
        addCityMarker(panel, "Kitchener", 584, 602);
        addCityMarker(panel, "Province of Quebec", 630, 470);
        addCityMarker(panel, "Moncton", 760, 500);
        addCityMarker(panel, "Richmond Hill", 604, 588);
        addCityMarker(panel, "Mississauga", 597, 598); 
        addCityMarker(panel, "Burnaby", 76, 470);
        addCityMarker(panel, "Winnipeg", 370, 522);
        addCityMarker(panel, "Iqaluit", 595, 280);
        addCityMarker(panel, "Summerside", 771, 494);
        addCityMarker(panel, "Surrey", 79, 472);
        addCityMarker(panel, "Guelph", 587, 598);
        addCityMarker(panel, "Burlington", 596, 602);
        addCityMarker(panel, "St. Catharines", 606, 607);
        addCityMarker(panel, "Saint John", 760, 517);
        addCityMarker(panel, "Kingston", 634, 580);
        addCityMarker(panel, "Ajax", 607, 593);
        addCityMarker(panel, "Richmond", 73, 473);
        addCityMarker(panel, "Milton", 591, 600);
        addCityMarker(panel, "Fredericton", 750, 505);
        addCityMarker(panel, "Whitby", 612, 590);
        addCityMarker(panel, "Squamish", 73, 460);
        addCityMarker(panel, "Waterloo", 579, 607);
        addCityMarker(panel, "Regina", 290, 505);
        addCityMarker(panel, "Coquitlam", 81, 467);
        addCityMarker(panel, "Charlottetown", 790, 490);
        addCityMarker(panel, "Abbotsford", 83, 478);
        addCityMarker(panel, "Victoria", 66, 482);
        addCityMarker(panel, "Channel-Port Aux Basques", 806, 455);
        addCityMarker(panel, "Banff", 170, 470);
        addCityMarker(panel, "Campbellton", 730, 487);
        addCityMarker(panel, "Marathon", 495, 535);
        addCityMarker(panel, "Wolfville", 775, 515);
        addCityMarker(panel, "Cape Breton", 811, 483); 
        addCityMarker(panel, "Woolwich", 583, 593);
        addCityMarker(panel, "New Glasgow", 788, 500);
        addCityMarker(panel, "Cornwall", 785, 492);
        addCityMarker(panel, "Mount Pearl", 870, 420);
        addCityMarker(panel, "Saskatoon", 265, 480);
        addCityMarker(panel, "Whitehorse", 50, 400);
        // addCityMarker(panel, "Thunder Bay", 420, 550);
        // addCityMarker(panel, "Shippagan", 780, 490);
        // addCityMarker(panel, "North Vancouver", 105, 465);
        // addCityMarker(panel, "North Grenville", 625, 570);
        // addCityMarker(panel, "Cap-Acadie", 785, 485);
        // addCityMarker(panel, "Grand Bouctouche", 790, 480);
        // addCityMarker(panel, "Tecumseh", 570, 620);
        // addCityMarker(panel, "Airdrie", 220, 495);
        // addCityMarker(panel, "Pemberton", 115, 470);
        // addCityMarker(panel, "Cambridge", 600, 610);
        // addCityMarker(panel, "Kings County", 805, 510);
        // addCityMarker(panel, "West Hants", 810, 505);
        // addCityMarker(panel, "Markham", 610, 595);
        // addCityMarker(panel, "Antigonish", 815, 500);
        // addCityMarker(panel, "St. John's", 840, 530);
        // addCityMarker(panel, "Gibsons", 120, 465);
        // addCityMarker(panel, "Stratford", 820, 495);
        // addCityMarker(panel, "Barrie", 605, 605);
        // addCityMarker(panel, "Three Rivers", 825, 490);
        // addCityMarker(panel, "Grand Bay-Westfield", 795, 475);
        // addCityMarker(panel, "Bowen Island", 125, 460);
        // addCityMarker(panel, "O'Leary", 830, 485);
        // addCityMarker(panel, "Edmundston", 775, 480);
        // addCityMarker(panel, "East Hants", 820, 500);
        // addCityMarker(panel, "Dawson", 60, 390);

        return panel;
    }

    /**
     * Adds a city marker to the map panel.
     * @param panel The panel to add the marker to
     * @param cityName The name of the city
     * @param x The x-coordinate for the marker
     * @param y The y-coordinate for the marker
     */
    private void addCityMarker(JPanel panel, String cityName, int x, int y) {
        JPanel cityMarker = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                if ("Province of Quebec".equals(cityName)) {
                    g2d.fillOval(0, 0, 20, 20); // Larger circle for Quebec
                } else {
                    g2d.fillOval(0, 0, 5, 5); // Default circle size
                }
            }
        };
        cityMarker.setBounds(x, y, "Province of Quebec".equals(cityName) ? 20 : 10, "Province of Quebec".equals(cityName) ? 20 : 10); // Adjust bounds for Quebec
        cityMarker.setOpaque(false); // Make the panel transparent
        cityMarker.setBackground(Color.RED); // Initial color

        // Add hover effect
        cityMarker.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cityMarker.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                cityMarker.setBackground(Color.BLUE); // Change color on hover
                cityMarker.setToolTipText(cityName); // Set tooltip to show city name
                cityMarker.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cityMarker.setBackground(Color.RED); // Revert color when not hovered
                cityMarker.repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Display city information when clicked
                displayCityInfo(cityName);
            }
        });

        // Ensure tooltips show immediately
        ToolTipManager.sharedInstance().setInitialDelay(0);

        panel.add(cityMarker, 0); // Add with index 0 to ensure it's on top
    }

    /**
     * Creates a placeholder panel with a specified message.
     * @param message The message to display in the panel
     * @return JPanel containing the placeholder message
     */
    private JPanel createEmptyPanel(String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(message);
        label.setFont(new Font("Inter", Font.BOLD, 24));
        label.setForeground(new Color(150, 150, 150));

        panel.add(label);
        return panel;
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

private void displayCityInfo(String cityName) {
    // Get data from controller
    DataController controller = viewHandler.getControllersList().get(0);
    List<String> cities = controller.getCityNames();
    
    // Determine which data to display based on selected tab
    List<Integer> values;
    String valuePrefix;
    if (headerTitle.getText().contains("Federal Funding")) {
        values = controller.getFundingValues();
        valuePrefix = "$";
    } else {
        values = controller.getFutureHousingPlans();
        valuePrefix = "";
    }

    // Find the index of the city based on the first word
    String[] cityNameParts = cityName.split(" ");
    String firstWord = cityNameParts[0];

    int index = -1;
    for (int i = 0; i < cities.size(); i++) {
        if (cities.get(i).startsWith(firstWord)) {
            index = i;
            break;
        }
    }

    // Create the card layout
    JPanel card = new JPanel(new BorderLayout(5, 0));
    card.setBackground(new Color(250, 252, 255));
    card.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(10, new Color(230, 230, 230)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    // Create a more compact layout for the text
    JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 0));
    textPanel.setBackground(new Color(250, 252, 255));

    // Add population emoji label
    JLabel populationLabel = new JLabel("👥");
    populationLabel.setFont(new Font("Inter", Font.PLAIN, 30));
    populationLabel.setHorizontalAlignment(SwingConstants.LEFT);

    // Create labels with specified font sizes
    JLabel cityLabel = new JLabel(cityName);
    cityLabel.setFont(new Font("Inter", Font.BOLD, 18));
    
    JLabel fundingLabel;
    if (index != -1) {
        String valueText;
        if (headerTitle.getText().contains("Federal Funding")) {
            valueText = valuePrefix + values.get(index) + " Million";
        } else {
            valueText = values.get(index) + " New Homes";
        }
        fundingLabel = new JLabel(valueText);
    } else {
        fundingLabel = new JLabel("No Info available");
    }
    
    fundingLabel.setFont(new Font("Inter", Font.BOLD, 24));
    fundingLabel.setForeground(Color.GRAY);

    textPanel.add(populationLabel);
    textPanel.add(cityLabel);
    textPanel.add(fundingLabel);
    card.add(textPanel, BorderLayout.CENTER);

    // Clear previous content and add new city card
    cityInfoPanel.removeAll();
    cityInfoPanel.add(card, BorderLayout.CENTER);
    cityInfoPanel.revalidate();
    cityInfoPanel.repaint();
}


private void createAndShowGUI(List<String> cities, List<Integer> fundingValues) {
    // Initialize card layout
    cardLayout = new CardLayout();
    contentCards = new JPanel(cardLayout);
    contentCards.setBackground(Color.WHITE);
    
    // Create sidebar
    JPanel sidebar = createModernSidebar();
    
    // Create main content
    mainContent = new JPanel(new BorderLayout(20, 20));
    mainContent.setBackground(Color.WHITE);
    mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // Create header
    JPanel headerPanel = createModernHeader();
    
    // Initialize city info panel
    cityInfoPanel = new JPanel(new BorderLayout());
    cityInfoPanel.setBackground(Color.WHITE);
    cityInfoPanel.setBounds(875, 70, 300, 200);
    displayDefaultCityInfo();
    
    // Create initial panels
    mapPanel = createMapPanel();
    
    // Initialize with Federal Funding data
    DataController controller = viewHandler.getController("Funding Insights");
    updateMapAndGraphViews(controller, true);  // true for Federal Funding
    
    JPanel comingSoonPanel = createComingSoonPanel();
    
    // Add panels to card layout
    contentCards.add(mapPanel, "Map View");
    contentCards.add(graphPanel, "Graph View");
    contentCards.add(comingSoonPanel, "Coming Soon");
    
    // Add components to main content
    mainContent.add(headerPanel, BorderLayout.NORTH);
    mainContent.add(contentCards, BorderLayout.CENTER);
    
    // Set layout to null for absolute positioning
    setLayout(null);
    add(sidebar);
    add(mainContent);
    add(cityInfoPanel);
    
    // Set bounds for other components
    sidebar.setBounds(0, 0, 250, 800);
    mainContent.setBounds(250, 0, 960, 810);

    
    // Debugging output
    System.out.println("Initial Toggle Button Text: " + toggleButton.getText());
    
    // Show map view by default
    cardLayout.show(contentCards, "Map View");
}

    public void updateDisplay() {
        DataController controller = viewHandler.getControllersList().get(0);
        List<String> cities = controller.getCityNames();
        List<Integer> fundingValues = controller.getFundingValues();
        
        // Update city list
        JPanel citiesPanel = createModernCityList(cities, fundingValues);
        mainContent.remove(1); // Remove old city list
        mainContent.add(citiesPanel, BorderLayout.WEST);
        
        // Update charts
        graphPanel = createDashboardContent(cities, fundingValues, 
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        contentCards.add(graphPanel, "Graph View");
        
        // Refresh display
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void setupFrame() {
        setTitle("Canadian Housing Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates a panel containing charts and statistics.
     * @param cities List of city names
     * @param fundingValues List of funding values
     * @return JPanel containing the charts and statistics
     */
    private JPanel createChartsPanel(List<String> cities, List<String> fundingValues) {
        JPanel chartsPanel = new JPanel(new BorderLayout(20, 20));
        chartsPanel.setBackground(Color.WHITE);
        
        // Create a wrapper panel for the stats panel
        JPanel statsWrapper = new JPanel(new BorderLayout());
        statsWrapper.setBackground(Color.WHITE);
        statsWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Add stats panel
        JPanel statsPanel = createModernStatsPanel();
        statsWrapper.add(statsPanel, BorderLayout.CENTER);
        
        // Add the stats wrapper to the main panel
        chartsPanel.add(statsWrapper, BorderLayout.NORTH);
        
        // Create a panel for future chart implementations
        JPanel chartArea = new JPanel(new BorderLayout());
        chartArea.setBackground(Color.WHITE);
        chartArea.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Add a placeholder message for future chart implementation
        JLabel placeholderLabel = new JLabel("Chart visualization coming soon...");
        placeholderLabel.setFont(new Font("Inter", Font.BOLD, 16));
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setForeground(new Color(150, 150, 150));
        chartArea.add(placeholderLabel, BorderLayout.CENTER);
        
        // Add the chart area to the main panel
        chartsPanel.add(chartArea, BorderLayout.CENTER);
        
        return chartsPanel;
    }

    private void updateMapAndGraphViews(DataController controller, boolean isFunding) {
        List<String> cities = controller.getCityNames();
        List<Integer> values;
        
        if (isFunding) {
            values = controller.getFundingValues();
        } else {
            values = controller.getFutureHousingPlans();
        }
        
        // Get provincial data for graphs
        Map<String, Integer> provincialData = isFunding ? 
            controller.getProvincialFunding() : 
            controller.getProvincialHousingPlans();
        
        // Convert map to lists for graphs
        List<String> provinceLabels = new ArrayList<>(provincialData.keySet());
        List<Integer> provinceValues = new ArrayList<>();
        for (String province : provinceLabels) {
            provinceValues.add(provincialData.get(province));
        }
        
        // Convert to float for pie chart
        List<Float> pieData = provinceValues.stream()
            .map(Float::valueOf)
            .collect(Collectors.toList());
        
        // Debug output
        System.out.println("\nProcessing " + (isFunding ? "Funding" : "Housing Plans") + " data:");
        System.out.println("Raw Provincial Data: " + provincialData);
        provincialData.forEach((province, value) -> {
            System.out.println(province + ": " + value);
        });
        System.out.println("Number of provinces: " + provinceLabels.size());
        System.out.println("Number of values: " + provinceValues.size());
        
        // Update graph panel with new data
        graphPanel = createDashboardContent(
            cities, 
            values,
            pieData,
            provinceLabels,
            provinceLabels,
            provinceValues
        );
        
        // Update the cards panel
        contentCards.removeAll();
        contentCards.add(mapPanel, "Map View");
        contentCards.add(graphPanel, "Graph View");
        
        // Show map view by default
        cardLayout.show(contentCards, "Map View");
        displayDefaultCityInfo();
        
        // Refresh the display
        contentCards.revalidate();
        contentCards.repaint();
    }

    private void displayDefaultCityInfo() {
        JPanel card = new JPanel(new BorderLayout(5, 0));
        card.setBackground(new Color(250, 252, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)  // Increased padding
        ));

        // Create a panel for the text content with GridLayout
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(new Color(250, 252, 255));

        // Add icon
        JLabel iconLabel = new JLabel("🔍");
        iconLabel.setFont(new Font("Inter", Font.PLAIN, 30));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add message
        JLabel messageLabel = new JLabel("Please select a marker to see info");
        messageLabel.setFont(new Font("Inter", Font.BOLD, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(Color.GRAY);

        // Add components to the text panel
        textPanel.add(iconLabel);
        textPanel.add(messageLabel);

        // Add text panel to the card
        card.add(textPanel, BorderLayout.CENTER);

        // Clear the city info panel and add the new card
        cityInfoPanel.removeAll();
        cityInfoPanel.add(card);
        
        // Make sure the panel is visible
        cityInfoPanel.setVisible(true);
        
        // Refresh the display
        cityInfoPanel.revalidate();
        cityInfoPanel.repaint();
    }

    private JPanel createGraphCard(JComponent graph, String title) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Add title with modern font
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 37, 41));
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Set preferred size for the graph
        graph.setPreferredSize(new Dimension(0, 230));
        card.add(graph, BorderLayout.CENTER);
        
        return card;
    }
}
