package MediaPlayer.Slider;

import javax.swing.*;
import java.awt.*;

public class JSliderCustom extends JSlider {
    public JSliderCustom(int x, int y, int width, int height, int minValue) {
        super(0, 100,minValue);
        setMajorTickSpacing(1);
        setBounds(x, y, width, height);
        setOpaque(true);
        setFocusable(false);
        setBackground(new Color(47, 73, 113));
        setForeground(new Color(149, 165, 24));
        setUI(new JSliderUI(this));
    }
}