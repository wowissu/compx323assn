import javax.swing.*; 
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class UITable extends UIComponent<JScrollPane> {
    public String[] columns;
    public String[][] data;
    public JTable table;

    public UITable (String name) {
        super(name);

        JScrollPane scroll = new JScrollPane();
        setRoot(scroll);
        
        table = new JTable();
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
    
    private void update () {
        DefaultTableModel model = new DefaultTableModel(this.data, this.columns);

        setModel(model);
    }
}
