import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class main {
  public static void main(String[] args) {
    DatabaseConnection db = new DatabaseConnection();
    // connect to db

    UIRender ui = new UIRender("Simple Form", 300, 400);

    ui.onClose(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        // Do your disconnect from the DB here.
        try {
          db.close();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    JPanel yPanel = UIRender.createYPanel();
    JPanel studentNameInput = UIRender.createInput("Enter your name:");
    JPanel classInput = UIRender.createInput("Enter class:");
    JButton button = new JButton("Submit");
    yPanel.add(studentNameInput);
    yPanel.add(classInput);
    yPanel.add(button);

    // button.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // String name = textField.getText();
    // System.out.println("Hello, " + name + "!");
    // }
    // });

    ui.add(yPanel);

    ui.setVisible(true);
  }
}
