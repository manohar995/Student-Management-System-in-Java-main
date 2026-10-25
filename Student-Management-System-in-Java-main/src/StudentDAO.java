import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private static final String SELECT_SQL =
            "SELECT student_id, first_name, last_name, major, phone, gpa, dob FROM sdata";

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO sdata (student_id, first_name, last_name, major, phone, gpa, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());
            ps.setString(4, student.getMajor());
            ps.setString(5, student.getPhone());
            ps.setDouble(6, student.getGpa());
            ps.setDate(7, Date.valueOf(student.getDob()));
            ps.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        try (Connection conn = dbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SQL + " ORDER BY student_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                students.add(Student.fromResultSet(rs));
            }
        }
        return students;
    }

    public List<Student> searchByStudentId(String studentId) throws SQLException {
        return searchStudents("student_id", studentId);
    }

    public List<Student> searchByLastName(String lastName) throws SQLException {
        return searchStudents("last_name", lastName);
    }

    public List<Student> searchByMajor(String major) throws SQLException {
        return searchStudents("major", major);
    }

    public boolean studentExists(String studentId) throws SQLException {
        String sql = "SELECT 1 FROM sdata WHERE student_id = ?";
        try (Connection conn = dbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void updateStudent(String studentId, Student student) throws SQLException {
        String sql = "UPDATE sdata SET first_name = ?, last_name = ?, major = ?, phone = ?, gpa = ?, dob = ? WHERE student_id = ?";
        try (Connection conn = dbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getMajor());
            ps.setString(4, student.getPhone());
            ps.setDouble(5, student.getGpa());
            ps.setDate(6, Date.valueOf(student.getDob()));
            ps.setString(7, studentId);
            ps.executeUpdate();
        }
    }

    public void deleteStudent(String studentId) throws SQLException {
        String sql = "DELETE FROM sdata WHERE student_id = ?";
        try (Connection conn = dbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.executeUpdate();
        }
    }

    private List<Student> searchStudents(String column, String value) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = SELECT_SQL + " WHERE " + column + " LIKE ? ORDER BY student_id";
        try (Connection conn = dbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + value + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(Student.fromResultSet(rs));
                }
            }
            return students;
        }
    }
}
