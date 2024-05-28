import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UITable extends UIComponent<JScrollPane> {
    public String[] columns;
    public String[][] data;
    public JTable table;
    public DefaultTableModel model;

    public UITable(String name, DefaultTableModel model) {
        super(name);
        this.model = model;

        JScrollPane scroll = new JScrollPane();
        setRoot(scroll);

        table = new JTable(model);
        scroll.setViewportView(table);
    }

    public void clear() {
        model.setRowCount(0);
    }

    public void addRow(Object[] row) {
        model.addRow(row);
    }
}
