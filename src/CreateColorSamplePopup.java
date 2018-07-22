/*
Definitive Guide to Swing for Java 2, Second Edition
By John Zukowski     
ISBN: 1-893115-78-X
Publisher: APress
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class CreateColorSamplePopup {

  public static void main(String args[]) {
    JFrame frame = new JFrame("JColorChooser Create Popup Sample");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container contentPane = frame.getContentPane();

    final JButton button = new JButton("Pick to Change Background");

    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        Color initialBackground = button.getBackground();

        final JColorChooser colorChooser = new JColorChooser(
            initialBackground);
        //        colorChooser.setPreviewPanel(new JPanel());
        final JLabel previewLabel = new JLabel("I Love Swing",
            JLabel.CENTER);
        previewLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC,
            48));
        colorChooser.setPreviewPanel(previewLabel);
        // Bug workaround
        colorChooser.updateUI();

        // For okay button selection, change button background to
        // selected color
        ActionListener okActionListener = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            Color newColor = colorChooser.getColor();
            if (newColor.equals(button.getForeground())) {
              System.out.println("Color change rejected");
            } else {
              button.setBackground(colorChooser.getColor());
            }
          }
        };

        // For cancel button selection, change button background to red
        ActionListener cancelActionListener = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            button.setBackground(Color.red);
          }
        };

        final JDialog dialog = JColorChooser.createDialog(null,
            "Change Button Background", true, colorChooser,
            okActionListener, cancelActionListener);

        // Wait until current event dispatching completes before showing
        // dialog
        Runnable showDialog = new Runnable() {
          public void run() {
            dialog.show();
          }
        };
        SwingUtilities.invokeLater(showDialog);
      }
    };
    button.addActionListener(actionListener);
    contentPane.add(button, BorderLayout.CENTER);

    frame.setSize(300, 100);
    frame.setVisible(true);
  }
}
