import java.awt.FlowLayout;

import javax.swing.*;

public class UIComboBox extends UIComponent<JPanel> {
    JLabel labelComp;
    JComboBox<String> comboComp;

    public UIComboBox (String name, String label, String[] choices ) {
        super(name);

        root = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.setRoot(root);

        labelComp = new JLabel(label);
        comboComp = new JComboBox<>(choices);

        root.add(labelComp);
        root.add(comboComp);
    }

    public String getValue() {
        return (String) comboComp.getSelectedItem();
    }
}
