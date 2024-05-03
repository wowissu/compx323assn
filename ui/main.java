import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

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

    // JPanel layout = UIRender.createYPanel();
    JLabel header = new JLabel("Find student's classes.");

    UITable table = createTable();
    JComponent form = createForm(table);
    
    render.add(header);
    render.add(form);
    render.add(table.root);
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

  static UITable createTable() {
    return new UITable("table");
  }

  static JComponent createForm(UITable uiTable) {
    UIInput studentIDInput = UIRender.createInput("studentID", "Enter student id:");
    UIComboBox eventTypeComboBox = UIRender.createComboBox("eventType", "Choice a event type", new String[]{"All", "Laboratory", "Tutorial", "Lecture"});

    JPanel submitButton = UIRender.createSubmitButton("Submit", new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          List<String> optionalParams = new ArrayList<>();

          // sql
          String sql = "SELECT s.id, s.name, chs.className, e.locationRoom, e.type as eventType, to_char(e.time, 'HH24:MI:SS') as eventTime "
          + "FROM student s "
          + "JOIN class_has_student chs ON s.id = chs.studentID "
          + "JOIN class c ON c.name = chs.className "
          + "JOIN event e ON c.name = e.className";
           
          // student id
          String studentIDSearching = UIRender.get("studentID", UIInput.class).getValue();
          if (studentIDSearching != null && !studentIDSearching.trim().isEmpty()) {
            System.out.println("search student id: " + studentIDSearching);
            sql = sql.concat(" WHERE s.id = ?");
            optionalParams.add(studentIDSearching);
          }

          // event type
          String eventTypeSearching = UIRender.get("eventType", UIComboBox.class).getValue();
          if (eventTypeSearching != null && !eventTypeSearching.trim().isEmpty() && !eventTypeSearching.equals("All")) {
            System.out.println("search eventType: " + eventTypeSearching);
            sql = sql.concat(" AND e.type = ?");
            optionalParams.add(eventTypeSearching);
          }

          PreparedStatement stmt = db.conn.prepareStatement(sql);
          int index = 0;
          for (String params : optionalParams) {
            stmt.setString(++index, params);
          }
          ResultSet rs = stmt.executeQuery();

          DefaultTableModel model = new DefaultTableModel(new String[]{"studentID", "studentName", "className", "locationRoom","eventType", "eventTime"}, 0);

          while (rs.next()) {
            String studentID = rs.getString("id");
            String studentName = rs.getString("name");
            String className = rs.getString("className");
            String locationRoom = rs.getString("locationRoom");c
            String eventType = rs.getString("eventType");
            String eventTime = rs.getString("eventTime");
            
            model.addRow(new Object[]{studentID, studentName, className, locationRoom, eventType, eventTime});
          }

          uiTable.setModel(model);

          rs.close();
          stmt.close();
        } catch (SQLException ex) {
          System.out.print(ex);
        }
      }
    });

    JPanel form = UIRender.createYPanel();
    form.add(studentIDInput.root);
    form.add(eventTypeComboBox.root);
    form.add(submitButton);

    return form;
  }
}
