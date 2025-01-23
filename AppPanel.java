package MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppPanel extends JPanel {
    public static JFrame frame;
    public static Timer timer;
    public static String startTime = "00:00";
    public static JPanel panel;
    public static JLabel label;
    static ControlPanel cp;
    public String endTime = "00:00";
    boolean isCLicked = false;

    public AppPanel() {
        cp = new ControlPanel();
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                music(g2d);
                timer(g2d, startTime);
                repaint();
                nameSongArtist(g2d, ControlPanel.audio.songName, ControlPanel.audio.artistName);
            }
        };

        label = new JLabel();
        label.setVisible(false);
        label.setForeground(new Color(189, 189, 239));
        label.setFont(new Font(null, Font.PLAIN, 35));
        label.setBounds(185, 250, 150, 50);
        panel.add(label);
        frame = new JFrame("Media Player");
        cp.action();
        cp.volume();
        panel.add(ControlPanel.sliderPlay);
        panel.add(ControlPanel.sliderVolume);
        panel.add(cp.closeButton);
        panel.add(cp.playPauseButton);
        panel.add(cp.previousButton);
        panel.add(cp.nextButton);
        panel.add(cp.volumeDownButton);
        panel.add(cp.volumeUpButton);
        panel.add(cp.repeatButton);
        panel.add(cp.arrowButton);
        panel.add(cp.shuffleButton);
        panel.add(cp.playBackSpeed);
        panel.add(cp.playBackSpeed);
        panel.add(cp.plusTenSecondsButton);
        panel.add(cp.minusTenSecondsButton);
        cp.playBackSpeed.setVisible(false);
        cp.plusTenSecondsButton.setVisible(false);
        cp.minusTenSecondsButton.setVisible(false);
        cp.shuffleButton.setVisible(false);
        cp.repeatButton.setVisible(false);
        panel.setBackground(new Color(19, 40, 54));
        panel.setLayout(null);
        frame.add(panel);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setSize(400, 610);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        timer = new Timer(1000, e -> {
            startTime = ControlPanel.audio.updateTimeForward();
            endTime = ControlPanel.audio.lengthOfClip();
            panel.repaint();
        });
        timer.stop();
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                lengthOfClip(x, y);
                tenSecondsArrowForward(x, y, e);
                tenSecondsArrowRewind(x, y, e);
            }
        });
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    if (isCLicked) {
                        isCLicked = false;
                        SwingUtilities.invokeLater(this::repaint);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void lengthOfClip(int x, int y) {
        var region = new Rectangle(300, 400, 200, 200);
        if (region.contains(x, y)) {
            if (!isCLicked) {
                isCLicked = true;
                repaint();
            }
        }
    }

    private void tenSecondsArrow(int x, int y, MouseEvent e, String arrow) {
        Rectangle region;
        switch (arrow) {
            case "forward":
                region = new Rectangle(200, 100, 350, 300);
                break;
            case "rewind":
                region = new Rectangle(45, 100, 200, 300);
                break;
            default:
                return;
        }

        if (region.contains(x, y) && e.getClickCount() == 2) {
            if (arrow.equals("forward")) {
                label.setText("⏩");
                ControlPanel.audio.forwardTenSeconds();
            } else {
                label.setText("⏪");
                ControlPanel.audio.backwardTenSeconds();
            }
            label.setVisible(true);
            new Timer(1500, a -> {
                label.setVisible(false);
                repaint();
            }).start();
        }
    }

    public void tenSecondsArrowForward(int x, int y, MouseEvent e) {
        tenSecondsArrow(x, y, e, "forward");
    }

    public void tenSecondsArrowRewind(int x, int y, MouseEvent e) {
        tenSecondsArrow(x, y, e, "rewind");
    }

    private void nameSongArtist(Graphics2D g2d, String songName, String artistName) {
        g2d.drawString(songName, 150, 465);
        g2d.setFont(new Font(null, Font.ITALIC, 15));
        g2d.drawString(artistName, 160, 480);
    }

    public void timer(Graphics2D g2d, String timeStart) {
        g2d.setFont(new Font(null, Font.PLAIN, 14));
        g2d.setColor(Color.white);
        g2d.drawString(timeStart, 25, 480);
        if (isCLicked) {
            g2d.drawString(" - " + ControlPanel.audio.updateTimeBackward(), 335, 480);
        } else {
            g2d.drawString(ControlPanel.audio.lengthOfClip(), 335, 480);
        }
        if (!ControlPanel.audio.repeat) {
            if (timeStart.equals(ControlPanel.audio.lengthOfClip())) {
                ControlPanel.audio.nextTrack();
            }
        }
    }

    private void music(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 200));
        g2d.setColor(new Color(13, 79, 128));
        g2d.drawString("♪", 145, 335);
        g2d.setColor(new Color(62, 84, 75, 20));
        g2d.drawString("♪", 130, 360);
        g2d.setColor(new Color(40, 117, 162));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(45, 100, 315, 335, 20, 20);
    }
}