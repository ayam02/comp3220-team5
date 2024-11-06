import javax.swing.*;
import java.awt.*;

public class CityList {
    private JPanel createCityListItem(String city, String funding, String details) {
        JPanel item = new JPanel(new BorderLayout(10, 5));
        item.setBackground(new Color(248, 249, 250));
        item.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(240, 242, 245)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // City name
        JLabel cityLabel = new JLabel(city);
        cityLabel.setFont(new Font("Inter", Font.BOLD, 16));
        cityLabel.setForeground(new Color(33, 37, 41));

        // Funding amount
        JLabel fundingLabel = new JLabel(funding);
        fundingLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        fundingLabel.setForeground(new Color(108, 117, 125));

        // Details link
        JLabel detailsLink = new JLabel(details);
        detailsLink.setFont(new Font("Inter", Font.PLAIN, 14));
        detailsLink.setForeground(new Color(13, 110, 253));
        detailsLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Layout
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setBackground(new Color(248, 249, 250));
        textPanel.add(cityLabel);
        textPanel.add(fundingLabel);
        textPanel.add(detailsLink);

        item.add(textPanel, BorderLayout.CENTER);
        return item;
    }
} 