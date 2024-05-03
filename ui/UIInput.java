import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UIInput extends UIComponent<JPanel> {    
    JLabel labelComp;
    JTextField textFieldComp;

    public UIInput(String name, String label, int textColumns) {
        super(name);
        this.name = name;

        root = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.setRoot(root);

        labelComp = new JLabel(label);
        textFieldComp = new JTextField(textColumns);

        root.add(labelComp);
        root.add(textFieldComp);
    }

    public String getValue() {
        return textFieldComp.getText();
    }
}
