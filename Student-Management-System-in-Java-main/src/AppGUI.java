package src;

import javax.swing.*;
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

    public AppGUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(studentIdField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Major:"));
        formPanel.add(majorField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("GPA:"));
        formPanel.add(gpaField);
        formPanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"));
        formPanel.add(dobField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        studentTable.setFillsViewportHeight(true);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Student Records"));

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addStudent());
        displayButton.addActionListener(e -> loadAllStudents());
        searchButton.addActionListener(e -> searchStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearForm());

        loadAllStudents();
    }

    private void addStudent() {
        Student student = readStudentFromForm();
        if (student == null) {
            return;
        }

        try {
            studentDAO.addStudent(student);
            JOptionPane.showMessageDialog(this, "Student added successfully.");
            clearForm();
            loadAllStudents();
        } catch (Exception ex) {
            showError("Unable to add student.", ex);
        }
    }

    private void updateStudent() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student ID to update.");
            return;
        }

        Student student = readStudentFromForm();
        if (student == null) {
            return;
        }

        try {
            if (!studentDAO.studentExists(studentId)) {
                JOptionPane.showMessageDialog(this, "Student not found.");
                return;
            }
            studentDAO.updateStudent(studentId, student);
            JOptionPane.showMessageDialog(this, "Student updated successfully.");
            clearForm();
            loadAllStudents();
        } catch (Exception ex) {
            showError("Unable to update student.", ex);
        }
    }

    private void deleteStudent() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student ID to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student with ID '" + studentId + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (!studentDAO.studentExists(studentId)) {
                JOptionPane.showMessageDialog(this, "Student not found.");
                return;
            }
            studentDAO.deleteStudent(studentId);
            JOptionPane.showMessageDialog(this, "Student deleted successfully.");
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
                JOptionPane.showMessageDialog(this, "No matching student found.");
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
            JOptionPane.showMessageDialog(this, "Student ID, First Name, Last Name, and Major are required.");
            return null;
        }

        if (!phone.isEmpty() && !phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must contain only digits and be 10 to 15 characters long.");
            return null;
        }

        double gpa;
        try {
            gpa = Double.parseDouble(gpaText);
            if (gpa < 0 || gpa > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "GPA must be a number between 0 and 10.");
            return null;
        }

        LocalDate dob;
        try {
            dob = LocalDate.parse(dobText);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Date of birth must be in yyyy-mm-dd format.");
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
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(this, message + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

