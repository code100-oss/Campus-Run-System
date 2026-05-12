package fosu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleButtonComponent {
    public static void main(String[] args) {
        JFrame frame = new JFrame("简单按钮示例");
        JButton button = new JButton("点击我");
        JLabel label = new JLabel("按钮还未被点击");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("按钮已被点击！");
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(null);
        button.setBounds(100, 30, 100, 30);
        label.setBounds(90, 70, 150, 30);
        frame.add(button);
        frame.add(label);
        frame.setVisible(true);
    }
}

