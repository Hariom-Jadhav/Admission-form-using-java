package coderClassAdmission;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.util.List; // Needed for List handling

@SuppressWarnings("serial")
public class AdmissionForm extends JFrame implements ActionListener {

    // 1. Variables ---
    JLabel calculatedAgeDisplay;
    JTextField fNameField, mNameField, lNameField, mobileField; 
    JComboBox<String> dayBox, monthBox, yearBox;
    JButton submitBtn;
    
    // The List Component
    JList<String> langList;

    // 2. Database Config 
    final String DB_URL = "jdbc:mysql://localhost:3306/admission_db";
    final String USER = "root";
    final String PASS = "YOUR_PASSWORD_HERE"; 

    public AdmissionForm() {
        setTitle("Coder Class Admission Form");
        setBounds(100, 100, 500, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); 
        initializeComponents();
    }

    public void initializeComponents() {
    	getContentPane().setBackground(Color.CYAN); // Options: RED, BLUE, GREEN, YELLOW, WHITE, etc.
    	
        // Standard UI Setup 
        JLabel titleLabel = new JLabel("Coder Class Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(120, 10, 300, 30);
        add(titleLabel);

        addLabelAndField("First Name:", 60, fNameField = new JTextField());
        addLabelAndField("Middle Name:", 90, mNameField = new JTextField());
        addLabelAndField("Last Name:", 120, lNameField = new JTextField());
        addLabelAndField("Mobile No:", 150, mobileField = new JTextField());

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(30, 190, 100, 20);
        add(dobLabel);

        String[] days = new String[31];
        for(int i=0; i<31; i++) days[i] = String.valueOf(i+1);
        dayBox = new JComboBox<>(days);
        dayBox.setBounds(140, 190, 50, 20);
        add(dayBox);

        String[] months = {"1","2","3","4","5","6","7","8","9","10","11","12"};
        monthBox = new JComboBox<>(months);
        monthBox.setBounds(200, 190, 50, 20);
        add(monthBox);

        String[] years = new String[50];
        int currentYear = Year.now().getValue();
        for(int i=0; i<50; i++) years[i] = String.valueOf(currentYear - i);
        yearBox = new JComboBox<>(years);
        yearBox.setBounds(260, 190, 80, 20);
        add(yearBox);

        JLabel ageLabel = new JLabel("Calculated Age:");
        ageLabel.setBounds(30, 230, 120, 20);
        add(ageLabel);

        calculatedAgeDisplay = new JLabel("Select DOB");
        calculatedAgeDisplay.setForeground(Color.BLUE);
        calculatedAgeDisplay.setBounds(140, 230, 200, 20);
        add(calculatedAgeDisplay);

        ActionListener dateListener = e -> calculateAge();
        dayBox.addActionListener(dateListener);
        monthBox.addActionListener(dateListener);
        yearBox.addActionListener(dateListener);

        // SCROLLABLE LIST (CLICK-TO-TOGGLE) SECTION 
        JLabel langLabel = new JLabel("Select Languages:");
        langLabel.setBounds(30, 270, 120, 20);
        add(langLabel);
        
        JLabel hintLabel = new JLabel("(Click to select/deselect)");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        hintLabel.setBounds(30, 290, 120, 15);
        add(hintLabel);

        // 1. Data
        String[] languages = { 
            "Java", "Python", "C", "C++", "HTML", "CSS", 
            "JavaScript", "SQL", "PHP", "Ruby", "Swift", "Kotlin" 
        };

        // 2. Create JList
        langList = new JList<>(languages);
        
        // 3. THE TRICK: Override the selection behavior
        // This makes a normal click behave like a "Ctrl+Click" (Toggle)
        langList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1); // If selected, remove it
                } else {
                    super.addSelectionInterval(index0, index1);    // If not, add it
                }
            }
        });

        // 4. ScrollPane
        JScrollPane scrollPane = new JScrollPane(langList);
        scrollPane.setBounds(140, 270, 150, 80); 
        add(scrollPane);

        submitBtn = new JButton("Submit Admission");
        submitBtn.setBounds(150, 370, 150, 30);
        submitBtn.addActionListener(this);
        add(submitBtn);
    }

    private void addLabelAndField(String text, int y, JTextField field) {
        JLabel label = new JLabel(text);
        label.setBounds(30, y, 100, 20);
        add(label);
        field.setBounds(140, y, 200, 20);
        add(field);
    }

    public int calculateAge() {
        try {
            int d = Integer.parseInt((String) dayBox.getSelectedItem());
            int m = Integer.parseInt((String) monthBox.getSelectedItem());
            int y = Integer.parseInt((String) yearBox.getSelectedItem());
            LocalDate birthDate = LocalDate.of(y, m, d);
            LocalDate currentDate = LocalDate.now();
            if (birthDate.isAfter(currentDate)) {
                calculatedAgeDisplay.setText("Invalid Date"); return -1;
            }
            int age = Period.between(birthDate, currentDate).getYears();
            calculatedAgeDisplay.setText(age + " years old");
            return age;
        } catch (Exception e) {
            calculatedAgeDisplay.setText("Error"); return -1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            saveDataToMySQL();
        }
    }

    public void saveDataToMySQL() {
        String fName = fNameField.getText();
        String mName = mNameField.getText();
        String lName = lNameField.getText();
        String mobile = mobileField.getText();
        int age = calculateAge();
        String dob = yearBox.getSelectedItem() + "-" + monthBox.getSelectedItem() + "-" + dayBox.getSelectedItem();

        // --- NEW LOGIC: Get selections from JList ---
        List<String> selectedLangs = langList.getSelectedValuesList();
        String langString = String.join(", ", selectedLangs);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            String query = "INSERT INTO students (first_name, middle_name, last_name, mobile_number, dob, age, languages) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            
            pst.setString(1, fName);
            pst.setString(2, mName);
            pst.setString(3, lName);
            pst.setString(4, mobile);
            pst.setString(5, dob);
            pst.setInt(6, age);
            pst.setString(7, langString);

            int rows = pst.executeUpdate();
            if(rows > 0) {
                JOptionPane.showMessageDialog(this, "Admission Successful!");
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public void showWindow() {
        setVisible(true);
    }
}