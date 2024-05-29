import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleQueryService implements QueryService {
  String url = "jdbc:oracle:thin:@oracle.cms.waikato.ac.nz:1521:teaching";
  String user = "zs284";
  String pwd = "VXMhcqpzQw";
  private Connection db;

  public OracleQueryService() {
    try {
      Class.forName("oracle.jdbc.OracleDriver");
      db = DriverManager.getConnection(url, user, pwd);

      System.out.println("Connected !");
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void close() {
    try {
      if (db != null) {
        db.close();
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void updateStudentName(Integer studentID, String newName) throws SQLException {
    String sql = "UPDATE student SET name = ? WHERE id = ?";

    PreparedStatement stmt = db.prepareStatement(sql);

    stmt.setString(1, newName);
    stmt.setString(2, studentID.toString());

    int rowsUpdated = stmt.executeUpdate();

    System.out.println("Update number of rows: " + rowsUpdated);
    
    stmt.close();
  }

  public Object[][] students(Integer studentID, String eventType) {
    try {
      List<String> optionalParams = new ArrayList<>();

      // sql
      String sql = "SELECT s.id, s.name, chs.className, e.locationRoom, e.type as eventType, to_char(e.time, 'HH24:MI:SS') as eventTime "
          + "FROM student s "
          + "JOIN class_has_student chs ON s.id = chs.studentID "
          + "JOIN event e ON chs.className = e.className";

      // student id
      if (studentID != null) {
        // System.out.println("search student id: " + studentID);
        sql = sql.concat(" WHERE s.id = ?");
        optionalParams.add(studentID.toString());
      }

      // event type
      if (eventType != null && !eventType.trim().isEmpty() && !eventType.equals("All")) {
        // System.out.println("search eventType: " + eventType);
        sql = sql.concat(" AND e.type = ?");
        optionalParams.add(eventType);
      }

      PreparedStatement stmt = db.prepareStatement(sql);
      int index = 0;
      for (String params : optionalParams) {
        stmt.setString(++index, params);
      }
      ResultSet rs = stmt.executeQuery();
      List<Object[]> rowsList = new ArrayList<>();

      while (rs.next()) {
        Object[] row = new Object[] {
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("className"),
            rs.getString("locationRoom"),
            rs.getString("eventType"),
            rs.getString("eventTime"),
        };
        rowsList.add(row);
      }

      Object[][] rows = new Object[rowsList.size()][];
      for (int i = 0; i < rowsList.size(); i++) {
        rows[i] = rowsList.get(i);
      }

      rs.close();
      stmt.close();

      return rows;
    } catch (SQLException ex) {
      return null;
    }
  }
}