import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class UITable extends UIComponent<JTable> {
    public String[] columns;
    public String[][] data;

    public UITable (String name) {
        super(name);


        JTable table = new JTable();
        setRoot(table);
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
    
    private void update () {
        String[] columnNames = this.columns;
        DefaultTableModel model = new DefaultTableModel();

        this.root.setModel(model);
    }
}
