// StudentPanel.java
import javax.swing.*;
import java.awt.*;

public class student {
    public JPanel createStudentStaffPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Back button
        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MAIN"));
        panel.add(backButton, BorderLayout.NORTH);

        // Student/Staff content
        JLabel label = new JLabel("Student & Staff Panel", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
}
