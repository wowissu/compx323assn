import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class main {
  public static void main(String[] args) {
    DatabaseConnection db = new DatabaseConnection();

    UIRender render = new UIRender("Simple Form", 300, 400);

    render.onClose(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        // Do your disconnect from the DB here.
        try {
          db.close();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    InputList inputList = new InputList();
    JPanel yPanel = UIRender.createYPanel();
    JPanel studentNameInput = UIRender.createInput("Enter student name:", inputList);
    JPanel classInput = UIRender.createInput("Enter class:", inputList);
    JButton submitButton = UIRender.createSubmitButton("Submit", new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (JTextComponent inputComp : inputList) {
          String text = inputComp.getText();
          System.out.println("Hello, " + text + "!");
        }

        try {
          Statement stmt = db.conn.createStatement();
          ResultSet rs = stmt.executeQuery("SELECT s.* FROM student s WHERE s.name LIKE '%John%'");

          while (rs.next()) {
            System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " +
            rs.getString("name"));
          }

          // //   step3 create the statement object
          // // PreparedStatement stmt = db.conn.prepareStatement(
          // //   "SELECT chs.className, e.locationRoom, e.type as eventType, to_char(e.time, 'HH24:MI:SS') as eventTime "
          // //   + "FROM student s "
          // //   + "JOIN class_has_student chs ON s.id = chs.studentID "
          // //   + "JOIN class c ON c.name = chs.className "
          // //   + "JOIN event e ON c.name = e.className "
          // //   + "WHERE s.name LIKE ?");
          // // stmt.setString(1, "%John%");

          // List<Event> evtList = new ArrayList<Event>();
          // ResultSet rs = stmt.executeQuery("SELECT s.* FROM student s WHERE s.name LIKE '%John%'");

          // // PreparedStatement stmt = db.conn.prepareStatement("SELECT s.* FROM student s WHERE s.name LIKE '%John%'");
          // // ResultSet rs = stmt.executeQuery();

          // System.out.println("before while");

          // while (rs.next()) {
          //   System.out.print("loop rs");

          //   Event evt = new Event();

          //   evt.setClassName(rs.getString("className"));
          //   evt.setLocationRoom(rs.getString("locationRoom"));
          //   evt.setEventType(rs.getString("eventType"));
          //   evt.setEventTime(rs.getString("eventTime"));

          //   System.out.print(evt);

          //   evtList.add(evt);
          // }

          System.out.println("end while");

          rs.close();
          stmt.close();

        } catch (SQLException ex) {
          System.out.print(ex);
        }
      }
    });

    yPanel.add(studentNameInput);
    yPanel.add(classInput);
    yPanel.add(submitButton);

    

    render.add(yPanel);
    render.setVisible(true);
  }
}
