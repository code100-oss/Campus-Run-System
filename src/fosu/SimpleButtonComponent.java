package fosu;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

public class SimpleButtonComponent {
    public static void main(String[] args) {
        // Create GUI on EDT and use a simple layout instead of absolute positioning.
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("简单按钮示例");
            JButton button = new JButton("点击我");
            JLabel label = new JLabel("按钮还未被点击");

            button.addActionListener(e -> label.setText("按钮已被点击！"));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
            frame.add(button);
            frame.add(label);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

