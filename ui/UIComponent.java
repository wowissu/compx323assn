import javax.swing.JComponent;

public class UIComponent<E extends JComponent> {
    String name; 
    E root;

    public UIComponent(String name) {
        this.name = name;
    }

    public void setRoot(E comp) {
        root = comp;
    }
}
