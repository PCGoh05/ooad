// StudentPanel.java
import javax.swing.*;
import java.awt.*;

public class org {
    public JPanel createOrganizerPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Back button
        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MAIN"));
        panel.add(backButton, BorderLayout.NORTH);

        // Organizer content
        JLabel label = new JLabel("Organizer Panel", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
}
