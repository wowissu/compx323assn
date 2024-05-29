import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class main {

  static OracleQueryService service;
  // static MongoQueryService service;
  static UIRender render;

  static UITable table;

  public static void main(String[] args) {
    service = new OracleQueryService();
    // service = new MongoQueryService();
    render = createRender();

    // JPanel layout = UIRender.createYPanel();
    JLabel header = new JLabel("Find student's classes.");

    UITable table = createTable(service);
    JComponent form = createForm(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onSubmit(table);
      }
    });

    render.add(header);
    render.add(form);
    render.add(table.root);
    render.setVisible(true);
  }

  static void onSubmit(UITable uiTable) {
    Integer studentIDInteger = null;
    String studentIDString = UIRender.get("studentID", UIInput.class).getValue();
    String eventTypeString = UIRender.get("eventType", UIComboBox.class).getValue();

    System.out.println(studentIDString);

    try {
      studentIDInteger = studentIDString == null ? null : Integer.parseInt(studentIDString);

    } catch (NumberFormatException ex) {
      studentIDInteger = null;
    }

    Object[][] rows = service.students(
        studentIDInteger,
        eventTypeString);

    uiTable.clear();

    for (int i = 0; i < rows.length; i++) {
      uiTable.addRow(rows[i]);
    }
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

  static UITable createTable(QueryService service) {
    String[] columnNames = new String[] { "studentID", "studentName", "className", "locationRoom", "eventType",
        "eventTime" };

    DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 1;
      }
    };

    UITable uiTable = new UITable("table", model);

    model.addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {

        if (e.getType() == TableModelEvent.UPDATE) {
          int row = e.getFirstRow();
          int column = e.getColumn();
          Object newValue = model.getValueAt(row, column);

          if (column == 1) {
            int studentID = Integer.parseInt(model.getValueAt(row, 0).toString());
            String newName = (String) newValue;

            try {
              service.updateStudentName(studentID, newName);

              System.out.println(studentID + " is updated");

              if (uiTable != null) {
                onSubmit(uiTable);
              }
            } catch (Exception ex) {
              System.out.println("Failed to update data!"); 
            }
          }
        }
      }
    });

    return uiTable;
  }

  static JComponent createForm(ActionListener listener) {
    UIInput studentIDInput = UIRender.createInput("studentID", "Enter student id:");
    UIComboBox eventTypeComboBox = UIRender.createComboBox("eventType", "Choice a event type",
        new String[] { "All", "Laboratory", "Tutorial", "Lecture" });

    JPanel submitButton = UIRender.createSubmitButton("Submit", listener);

    JPanel form = UIRender.createYPanel();
    form.add(studentIDInput.root);
    form.add(eventTypeComboBox.root);
    form.add(submitButton);

    return form;
  }
}
