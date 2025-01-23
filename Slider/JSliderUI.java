package MediaPlayer.Slider;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class JSliderUI extends BasicSliderUI {
    public int thumbX;
    public int thumbY;

    public JSliderUI(JSlider slider) {
        super(slider);
    }

    @Override
    public void paintThumb(Graphics g) {
        thumbX = thumbRect.x;
        thumbY = thumbRect.y;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(216, 102, 0));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillOval(thumbX, thumbY, thumbRect.width, thumbRect.height);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = trackRect.x;
        int y = trackRect.y;
        int w = trackRect.width;
        int h = trackRect.height;
        int fillUp = thumbRect.x + x;
        g2d.setColor(slider.getForeground());
        g2d.fillRect(x, y, w, h);
        g2d.setColor(slider.getBackground());
        g2d.fillRect(x + fillUp, y, w - fillUp, h - 5);
    }
}