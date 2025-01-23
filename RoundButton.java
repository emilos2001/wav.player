package MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundButton extends JButton {
    private final int arcWidth;
    private final int arcHeight;

    public RoundButton(String text, int width, int height, int arcWidth, int arcHeight) {
        super(text);
        setPreferredSize(new Dimension(width, height));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
        g2d.fill(roundedRectangle);
        g2d.setColor(getForeground());
        super.paintComponent(g);
    }
}
