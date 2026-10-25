import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AppGUI extends JFrame {
    private final JTextField studentIdField = new JTextField(12);
    private final JTextField firstNameField = new JTextField(12);
    private final JTextField lastNameField = new JTextField(12);
    private final JTextField majorField = new JTextField(12);
    private final JTextField phoneField = new JTextField(12);
    private final JTextField gpaField = new JTextField(12);
    private final JTextField dobField = new JTextField(12);

    private final JButton addButton = new JButton("Add Student");
    private final JButton displayButton = new JButton("Display All");
    private final JButton searchButton = new JButton("Search");
    private final JButton updateButton = new JButton("Update");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton clearButton = new JButton("Clear");

    private final JTable studentTable = new JTable();
    private final StudentDAO studentDAO = new StudentDAO();
    private final Table tableHelper = new Table();
    private final JLabel statusLabel = new JLabel("Ready");

    public AppGUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(240, 244, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(16, 16, 8, 16));
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Student Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(24, 42, 90));
        headerPanel.add(title, BorderLayout.WEST);

        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(31, 120, 70));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        headerPanel.add(statusLabel, BorderLayout.SOUTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(179, 197, 232), 1),
                "Student Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(48, 76, 126)));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(179, 197, 232), 1),
                        "Student Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14), new Color(48, 76, 126)),
                new EmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel[] labels = {
                new JLabel("Student ID:"),
                new JLabel("First Name:"),
                new JLabel("Last Name:"),
                new JLabel("Major:"),
                new JLabel("Phone:"),
                new JLabel("GPA:"),
                new JLabel("Date of Birth:")
        };

        JTextField[] fields = {
                studentIdField,
                firstNameField,
                lastNameField,
                majorField,
                phoneField,
                gpaField,
                dobField
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            labels[i].setForeground(new Color(34, 46, 72));
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(labels[i], gbc);

            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fields[i].setBackground(Color.WHITE);
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 190, 210)),
                    new EmptyBorder(4, 6, 4, 6)
            ));
            gbc.gridx = 1;
            gbc.weightx = 1;
            formPanel.add(fields[i], gbc);
            gbc.weightx = 0;
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        JButton[] buttons = {addButton, displayButton, searchButton, updateButton, deleteButton, clearButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setFocusPainted(false);
            button.setBackground(new Color(67, 99, 170));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonPanel.add(button);
        }

        addButton.setBackground(new Color(37, 128, 79));
        updateButton.setBackground(new Color(42, 112, 184));
        deleteButton.setBackground(new Color(189, 63, 63));
        clearButton.setBackground(new Color(120, 120, 120));

        studentTable.setFillsViewportHeight(true);
        studentTable.setRowHeight(28);
        studentTable.setAutoCreateRowSorter(true);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setSelectionBackground(new Color(169, 201, 255));
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(179, 197, 232), 1),
                "Student Records",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(48, 76, 126)
        ));

        JPanel contentPanel = new JPanel(new BorderLayout(12, 12));
        contentPanel.setBorder(new EmptyBorder(0, 16, 16, 16));
        contentPanel.setOpaque(false);
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(tableScrollPane, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> addStudent());
        displayButton.addActionListener(e -> loadAllStudents());
        searchButton.addActionListener(e -> searchStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearForm());

        loadAllStudents();
        pack();
    }

    private void addStudent() {
        Student student = readStudentFromForm();
        if (student == null) {
            return;
        }

        try {
            studentDAO.addStudent(student);
            showStatus("Student added successfully.", new Color(31, 120, 70));
            clearForm();
            loadAllStudents();
        } catch (Exception ex) {
            showError("Unable to add student.", ex);
        }
    }

    private void updateStudent() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            showStatus("Please enter a student ID to update.", new Color(189, 63, 63));
            return;
        }

        Student student = readStudentFromForm();
        if (student == null) {
            return;
        }

        try {
            if (!studentDAO.studentExists(studentId)) {
                showStatus("Student not found.", new Color(189, 63, 63));
                return;
            }
            studentDAO.updateStudent(studentId, student);
            showStatus("Student updated successfully.", new Color(31, 120, 70));
            clearForm();
            loadAllStudents();
        } catch (Exception ex) {
            showError("Unable to update student.", ex);
        }
    }

    private void deleteStudent() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            showStatus("Please enter a student ID to delete.", new Color(189, 63, 63));
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student with ID '" + studentId + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (!studentDAO.studentExists(studentId)) {
                showStatus("Student not found.", new Color(189, 63, 63));
                return;
            }
            studentDAO.deleteStudent(studentId);
            showStatus("Student deleted successfully.", new Color(31, 120, 70));
            clearForm();
            loadAllStudents();
        } catch (Exception ex) {
            showError("Unable to delete student.", ex);
        }
    }

    private void loadAllStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            studentTable.setModel(tableHelper.buildTableModel(students));
            showStatus("Showing " + students.size() + " student(s).", new Color(31, 120, 70));
        } catch (Exception ex) {
            showError("Unable to load students.", ex);
        }
    }

    private void searchStudent() {
        String[] searchOptions = {"Student ID", "Last Name", "Major"};
        int choice = JOptionPane.showOptionDialog(this,
                "Search by:",
                "Search Student",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                searchOptions,
                searchOptions[0]);

        if (choice < 0) {
            return;
        }

        String searchTerm = JOptionPane.showInputDialog(this, "Enter search value:");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return;
        }

        try {
            List<Student> students = switch (choice) {
                case 0 -> studentDAO.searchByStudentId(searchTerm.trim());
                case 1 -> studentDAO.searchByLastName(searchTerm.trim());
                case 2 -> studentDAO.searchByMajor(searchTerm.trim());
                default -> List.of();
            };
            studentTable.setModel(tableHelper.buildTableModel(students));
            if (students.isEmpty()) {
                showStatus("No matching students found.", new Color(189, 63, 63));
            } else {
                showStatus("Found " + students.size() + " matching student(s).", new Color(31, 120, 70));
            }
        } catch (Exception ex) {
            showError("Unable to search student.", ex);
        }
    }

    private Student readStudentFromForm() {
        String studentId = studentIdField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String major = majorField.getText().trim();
        String phone = phoneField.getText().trim();
        String gpaText = gpaField.getText().trim();
        String dobText = dobField.getText().trim();

        if (studentId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || major.isEmpty()) {
            showStatus("Student ID, First Name, Last Name, and Major are required.", new Color(189, 63, 63));
            return null;
        }

        if (!phone.isEmpty() && !phone.matches("\\d{10,15}")) {
            showStatus("Phone number must contain only digits and be 10 to 15 characters long.", new Color(189, 63, 63));
            return null;
        }

        double gpa;
        try {
            gpa = Double.parseDouble(gpaText);
            if (gpa < 0 || gpa > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showStatus("GPA must be a number between 0 and 10.", new Color(189, 63, 63));
            return null;
        }

        LocalDate dob;
        try {
            dob = LocalDate.parse(dobText);
        } catch (DateTimeParseException ex) {
            showStatus("Date of birth must be in yyyy-mm-dd format.", new Color(189, 63, 63));
            return null;
        }

        Student student = new Student();
        student.setStudentId(studentId);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setMajor(major);
        student.setPhone(phone);
        student.setGpa(gpa);
        student.setDob(dob);
        return student;
    }

    private void clearForm() {
        studentIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        majorField.setText("");
        phoneField.setText("");
        gpaField.setText("");
        dobField.setText("");
        studentIdField.requestFocus();
        showStatus("Form cleared.", new Color(31, 120, 70));
    }

    private void showError(String message, Exception ex) {
        showStatus(message, new Color(189, 63, 63));
        JOptionPane.showMessageDialog(this, message + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}

