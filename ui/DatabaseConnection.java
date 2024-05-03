import java.sql.*;

public class DatabaseConnection {

  String url = "jdbc:oracle:thin:@oracle.cms.waikato.ac.nz:1521:teaching";
  String user = "zs284";
  String pwd = "VXMhcqpzQw";
  Connection conn;

  public DatabaseConnection() {
    try {
      Class.forName("oracle.jdbc.OracleDriver");
      conn = DriverManager.getConnection(url, user, pwd);

      System.out.println("Connected !");
      
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void close() throws SQLException {
    if (conn != null) {
      conn.close();
    }
  }
}