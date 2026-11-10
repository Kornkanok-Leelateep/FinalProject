package Duolearn01;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class UITheme {

    // Modern mint-green palette
    public static final Color GREEN = new Color(145, 211, 145);
    public static final Color DARK_GREEN = new Color(62, 137, 83);
    public static final Color PALE_GREEN = new Color(232, 247, 231);
    public static final Color CREAM = new Color(248, 250, 246);
    public static final Color INK = new Color(48, 65, 58);
    public static final Color MUTED = new Color(119, 137, 127);
    public static final Color BORDER = new Color(225, 235, 226);
    public static final Color BLUE = new Color(151, 202, 230);
    public static final Color ORANGE = new Color(255, 196, 105);
    public static final Color BUTTON_TEXT = new Color(40, 65, 52);

    private UITheme() {}

    public static void styleButton(JButton button, Color color) {
        button.setUI(new BasicButtonUI());
        button.setFont(new Font("Tahoma", Font.BOLD, 14));
        button.setForeground(BUTTON_TEXT);
        button.setBackground(color);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(
                new LineBorder(color.darker(), 1, true),
                new EmptyBorder(10, 18, 10, 18)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
    }

    public static JPanel card() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        panel.setOpaque(false);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));
        return panel;
    }

    public static JLabel heading(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Leelawadee UI", Font.BOLD, size));
        label.setForeground(INK);
        return label;
    }
}