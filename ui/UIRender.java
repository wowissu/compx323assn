import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.HashMap;

import javax.swing.*;

public class UIRender {

  private JFrame frame;
  private JPanel mainPane;

  public UIRender(String title, int width, int height) {
    mainPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

    frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(width, height);
    frame.setContentPane(mainPane);
  }

  public void add(Component panel) {
    // System.out.println(mainPane);
    mainPane.add(panel);
  }

  public void setVisible(Boolean b) {
    frame.setVisible(b);
  }

  public void onClose(WindowListener l) {
    frame.addWindowListener(l);
  }

  static HashMap<String, UIComponent> map = new HashMap<String, UIComponent>();

  static UIInput createInput(String name, String label, int textColumns) {
    UIInput comp = new UIInput(name, label, textColumns);
    map.put(name, comp);

    return comp;
  }

  static UIInput createInput(String name, String label) {
    return createInput(name, label, 20);
  }

  static UIComboBox createComboBox(String name, String label, String[] choices) {
    UIComboBox comp = new UIComboBox(name, label, choices);
    map.put(name, comp);

    return comp;
  }

  static JPanel createYPanel(LayoutManager layout, boolean isDoubleBuffered) {
    JPanel panel = new JPanel(layout, isDoubleBuffered);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    return panel;
  }

  static JPanel createYPanel() {
    return createYPanel(new FlowLayout(FlowLayout.LEFT), true);
  }

  static JPanel createSubmitButton(String text, ActionListener listener) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton button = new JButton(text);

    button.addActionListener(listener);
    panel.add(button);

    return panel;
  }

  static <E extends UIComponent> E get(String name, Class<E> type) {
    UIComponent component = map.get(name);

    if (type.isInstance(component)) {
      return type.cast(component);
    } else {
      return null;
    }
  }
}
