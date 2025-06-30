import javax.swing.*;
import java.awt.*;

public class StudentStaff {
    private int registerFees;
    private org organizer; // Reference to shared organizer

    // Constructor that accepts organizer instance
    public StudentStaff(org organizer) {
        this.organizer = organizer;
    }

    // Default constructor for backward compatibility
    public StudentStaff() {
        this.organizer = null;
    }

    public int getRegisterFees() {
        return registerFees;
    }

    public void setRegisterFees(int registerFees) {
        this.registerFees = registerFees;
    }

    public JPanel createStudentStaffPanel(CardLayout cardLayout, JPanel cardPanel) {
        StudentStaffPanel panel = new StudentStaffPanel(this, organizer);
        return panel.createStudentStaffPanel(cardLayout, cardPanel);
    }
}
