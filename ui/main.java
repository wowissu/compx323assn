import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class main {

  static DatabaseConnection db;
  static UIRender render;

  public static void main(String[] args) {
    db = new DatabaseConnection();
    render = createRender();
    JComponent form = createForm();
    
    render.add(form);
    render.setVisible(true);
  }


  static UIRender createRender() {
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

    return render;
  }

  static JComponent createForm() {
    UIInput studentNameInput = UIRender.createInput("studentName", "Enter student name:");
    JButton submitButton = UIRender.createSubmitButton("Submit", new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          List<String> optionalParams = new ArrayList<>();
          String sql = "SELECT chs.className, e.locationRoom, e.type as eventType, to_char(e.time, 'HH24:MI:SS') as eventTime "
          + "FROM student s "
          + "JOIN class_has_student chs ON s.id = chs.studentID "
          + "JOIN class c ON c.name = chs.className "
          + "JOIN event e ON c.name = e.className";
          
          String studentName = UIRender.get("studentName", UIInput.class).getValue();
          System.out.println("student name: " + studentName);
          if (studentName != null && !studentName.trim().isEmpty()) {
            sql = sql.concat(" WHERE s.name LIKE ?");
            optionalParams.add("%" + studentName + "%");
          }

          PreparedStatement stmt = db.conn.prepareStatement(sql);
          int index = 0;
          for (String params : optionalParams) {
            stmt.setString(++index, params);
          }
          ResultSet rs = stmt.executeQuery();

          List<Event> evtList = new ArrayList<Event>();

          DefaultTableModel model = new DefaultTableModel();

          while (rs.next()) {
            Event evt = new Event();
            evt.setClassName(rs.getString("className"));
            evt.setLocationRoom(rs.getString("locationRoom"));
            evt.setEventType(rs.getString("eventType"));
            evt.setEventTime(rs.getString("eventTime"));
            evtList.add(evt);

            String className = rs.getString("className");
            String locationRoom = rs.getString("locationRoom");
            String eventType = rs.getString("eventType");
            String eventTime = rs.getString("eventTime");

            
            model.addRow(new Object[]{className, locationRoom, eventType, eventTime});
          }

          System.out.println(evtList.size());

          rs.close();
          stmt.close();
        } catch (SQLException ex) {
          System.out.print(ex);
        }
      }
    });

    JPanel form = UIRender.createYPanel();
    form.add(studentNameInput.root);
    form.add(submitButton);

    return form;
  }
}
