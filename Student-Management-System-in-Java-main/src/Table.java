import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class Table {
    public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        Vector<Vector<Object>> rowData = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            rowData.add(row);
        }

        return new DefaultTableModel(rowData, columnNames);
    }

    public DefaultTableModel buildTableModel(List<Student> students) {
        String[] columnNames = {"Student ID", "First Name", "Last Name", "Major", "Phone", "GPA", "DOB"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Student student : students) {
            model.addRow(new Object[] {
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getMajor(),
                    student.getPhone(),
                    student.getGpa(),
                    student.getDob() != null ? student.getDob().toString() : ""
            });
        }

        return model;
    }
}
