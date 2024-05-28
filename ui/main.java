import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class main {

  // static OracleQueryService service;
  static MongoQueryService service;
  static UIRender render;

  public static void main(String[] args) {
    // service = new OracleQueryService();
    service = new MongoQueryService();
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
    UIRender render = new UIRender("Simple Form", 500, 500);

    render.onClose(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        // Do your disconnect from the DB here.
        service.close();
      }
    });

    return render;
  }

  static UITable createTable() {
    return new UITable("table");
  }

  static JComponent createForm(UITable uiTable) {
    UIInput studentIDInput = UIRender.createInput("studentID", "Enter student id:");
    UIComboBox eventTypeComboBox = UIRender.createComboBox("eventType", "Choice a event type",
        new String[] { "All", "Laboratory", "Tutorial", "Lecture" });

    JPanel submitButton = UIRender.createSubmitButton("Submit", new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Integer studentIDInteger = null;
        String studentIDString = UIRender.get("studentID", UIInput.class).getValue();
        String eventTypeString = UIRender.get("eventType", UIComboBox.class).getValue();
        DefaultTableModel model = new DefaultTableModel(
            new String[] { "studentID", "studentName", "className", "locationRoom", "eventType", "eventTime" }, 0);

        System.out.println(studentIDString);

        try {
          studentIDInteger = Integer.parseInt(studentIDString);

        } catch (NumberFormatException ex) {
          System.out.println("Invalid integer input");
          studentIDInteger = null;
        }

        Object[][] rows = service.students(
            studentIDInteger,
            eventTypeString);

        for (int i = 0; i < rows.length; i++) {
          model.addRow(rows[i]);
        }

        uiTable.setModel(model);
      }
    });

    JPanel form = UIRender.createYPanel();
    form.add(studentIDInput.root);
    form.add(eventTypeComboBox.root);
    form.add(submitButton);

    return form;
  }
}
