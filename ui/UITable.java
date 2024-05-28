import java.awt.Component;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class UITable extends UIComponent<JScrollPane> {
    public String[] columns;
    public String[][] data;
    public JTable table;

    public UITable(String name) {
        super(name);

        JScrollPane scroll = new JScrollPane();
        setRoot(scroll);

        table = new JTable();
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setCellEditor(new CellEditor());
        scroll.setViewportView(table);
    }

    public UITable setColumns(String[] columns) {
        this.columns = columns;
        this.update();

        return this;
    }

    public UITable setData(String[][] data) {
        this.data = data;
        this.update();

        return this;
    }

    public void setModel(TableModel model) {
        this.table.setModel(model);
    }

    private void update() {
        DefaultTableModel model = new DefaultTableModel(this.data, this.columns);

        setModel(model);
    }

    private class CellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
        private static final long serialVersionUID = 1L;
        private JFormattedTextField renderer;
        private JFormattedTextField editor;
        private NumberFormat format = DecimalFormat.getInstance();

        public CellEditor() {
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(4);
            format.setRoundingMode(RoundingMode.HALF_UP);
            renderer = new JFormattedTextField(format);
            renderer.setBorder(null);
            editor = new JFormattedTextField(format);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            renderer.setValue(value);
            return renderer;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            editor.setValue(value);
            return editor;
        }

        @Override
        public boolean stopCellEditing() {
            try {
                editor.commitEdit();
            } catch (ParseException e) {
                return false;
            }
            return super.stopCellEditing();
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getValue();
        }
    }
}
