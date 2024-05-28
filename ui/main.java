import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

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

    UITable table = createTable(service);
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

  static UITable createTable(QueryService service) {
    String[] columnNames = new String[] { "studentID", "studentName", "className", "locationRoom", "eventType",
        "eventTime" };

    DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        // 使第二列（Name）可编辑
        return column == 1;
      }
    };

    model.addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        System.out.println("Why it don't trigger this event????");

        if (e.getType() == TableModelEvent.UPDATE) {
          int row = e.getFirstRow();
          int column = e.getColumn();
          Object newValue = model.getValueAt(row, column);

          if (column == 1) {
            int studentID = (int) model.getValueAt(row, 0);
            String newName = (String) newValue;

            service.updateStudentName(studentID, newName);
          }
        }
      }
    });

    UITable uiTable = new UITable("table", model);

    return uiTable;
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

        uiTable.clear();

        for (int i = 0; i < rows.length; i++) {
          uiTable.addRow(rows[i]);
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
