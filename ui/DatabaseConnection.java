import java.sql.*;

public class DatabaseConnection {

  String url = "jdbc:oracle:thin:@oracle.cms.waikato.ac.nz:1521:teaching";
  String user = "zs284";
  String pwd = "VXMhcqpzQw";
  Connection conn;

  public void DatabaseConnection() {
    try {
      conn = DriverManager.getConnection(url, user, pwd);

      // step3 create the statement object
      // Statement stmt = conn.createStatement();

      // step4 execute query
      // ResultSet rs = stmt.executeQuery("select * from movie");

      // while (rs.next()) {
      // System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " +
      // rs.getString(3));
      // }

    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void close() throws SQLException {
    conn.close();
  }
}