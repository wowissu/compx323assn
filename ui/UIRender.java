import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Collection;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class UIRender {

  private JFrame frame;
  private JPanel mainPane;

  public UIRender(String title, int width, int height) {
    mainPane = new JPanel(new FlowLayout(FlowLayout.LEFT));

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

  static JPanel createInput(String label, int textColumns, InputList collector) {
    JPanel panelComp = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel labelComp = new JLabel(label);
    JTextField textFieldComp = new JTextField(textColumns);

    if (collector != null) {
      collector.add(textFieldComp);
    }

    panelComp.add(labelComp);
    panelComp.add(textFieldComp);

    return panelComp;
  }

  static JPanel createInput(String label, InputList collector) {
    return createInput(label, 20, collector);
  }

  static JPanel createYPanel(LayoutManager layout, boolean isDoubleBuffered) {
    JPanel panel = new JPanel(layout, isDoubleBuffered);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    return panel;
  }

  static JPanel createYPanel() {
    return createYPanel(new FlowLayout(FlowLayout.LEFT), true);
  }

  static JButton createSubmitButton(String text, ActionListener listener) {
    JButton button = new JButton(text);

    button.addActionListener(listener);

    return button;
  }
}
