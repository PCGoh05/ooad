import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class main_page {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new main_page().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Campus Event Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create the card panel with CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create and add your panels
        cardPanel.add(createMainPanel(), "MAIN");
        cardPanel.add(new org().createOrganizerPanel(cardLayout, cardPanel), "ORGANIZER");
        cardPanel.add(new student().createStudentStaffPanel(cardLayout, cardPanel), "STUDENT_STAFF");
        

        frame.add(cardPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Add glue to center vertically
        panel.add(Box.createVerticalGlue());

        JButton orgButton = new JButton("Organizers");
        orgButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        orgButton.addActionListener(e -> cardLayout.show(cardPanel, "ORGANIZER"));

        JButton studentButton = new JButton("Students and Staff");
        studentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentButton.addActionListener(e -> cardLayout.show(cardPanel, "STUDENT_STAFF"));

        panel.add(orgButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(studentButton);

        // Add glue to center vertically
        panel.add(Box.createVerticalGlue());

        return panel;
    }
}