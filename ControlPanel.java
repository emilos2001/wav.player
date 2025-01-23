package MediaPlayer;

import MediaPlayer.Slider.JSliderCustom;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;


public class ControlPanel {
    public static PlayAudio audio = new PlayAudio();
    public static int vol = (int) (audio.globalVolume * 100);
    public static JSliderCustom sliderVolume;
    public static JSliderCustom sliderPlay;
    public static Timer playTime;
    String close = "×";
    String play = "⏸";
    String arrowUp = "↑";
    String arrowDown = "↓";
    String pause = "▶";
    String previous = "⏪";
    String next = "⏩";
    String volumeUp = "\uD83D\uDD0A";
    String volumeDown = "\uD83D\uDD09";
    String shuffle = "\uD83D\uDD00";
    String repeat = " \uD83D\uDD01";
    String defaultSpeed = "1.0X";
    Color buttonsColor = new Color(19, 40, 54);
    Color forengoundButtonsColor = new Color(169, 164, 164);
    public JButton closeButton = buttons(close, 340, 15, 50, 50, buttonsColor, forengoundButtonsColor, 90, 50);
    public JButton volumeUpButton = buttons(volumeUp, 320, 510, 70, 50, buttonsColor, forengoundButtonsColor, 130, 50);
    public JButton volumeDownButton = buttons(volumeDown, 15, 510, 70, 50, buttonsColor, forengoundButtonsColor, 135, 60);
    public JButton playPauseButton = buttons(pause, 160, 500, 80, 75, buttonsColor, forengoundButtonsColor, 130, 75);
    public JButton previousButton = buttons(previous, 85, 510, 55, 55, buttonsColor, forengoundButtonsColor, 130, 75);
    public JButton repeatButton = buttons(repeat, 15, 510, 65, 55, buttonsColor, forengoundButtonsColor, 140, 55);
    public JButton nextButton = buttons(next, 250, 510, 55, 55, buttonsColor, forengoundButtonsColor, 120, 75);
    public JButton shuffleButton = buttons(shuffle, 320, 510, 55, 55, buttonsColor, forengoundButtonsColor, 120, 75);
    private final Set<JButton> buttons2 = Set.of(repeatButton, shuffleButton);
    public JButton arrowButton = buttons(arrowUp, 170, 578, 50, 30, buttonsColor, forengoundButtonsColor, 120, 50);
    public JButton playBackSpeed = buttons(defaultSpeed, 160, 500, 80, 75, buttonsColor, forengoundButtonsColor, 130, 75);
    public JButton plusTenSecondsButton = buttons("↻", 250, 510, 55, 55, buttonsColor, forengoundButtonsColor, 130, 75);
    public JButton minusTenSecondsButton = buttons("↺", 85, 510, 55, 55, buttonsColor, forengoundButtonsColor, 120, 75);
    private final Set<JButton> buttons1 = Set.of(closeButton, playPauseButton, previousButton, nextButton, volumeUpButton, volumeDownButton, arrowButton, playBackSpeed, plusTenSecondsButton, minusTenSecondsButton);

    public ControlPanel() {
        sliderVolume = new JSliderCustom(50, 420, 300, 5, vol);
        sliderPlay = new JSliderCustom(25, 490, 365, 7, 0);
        sliderPlay.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (sliderPlay.getValueIsAdjusting()){
                    int newPosition = sliderPlay.getValue();
                    audio.audioClip.setMicrosecondPosition(newPosition * 1_000L);
                }
            }
        });
        playTime = new Timer(100, e -> {
            long currentPosition = audio.audioClip.getMicrosecondPosition();
            int currentValue = (int) ((currentPosition * (sliderPlay.getMaximum() - sliderPlay.getMinimum())) / audio.audioClip.getMicrosecondLength());
            sliderPlay.setValue(currentValue);
            if (currentPosition >= audio.audioClip.getMicrosecondLength()) {
                ((Timer) e.getSource()).stop();
            }
        });
        playTime.start();
    }

    private static JButton buttons(String text, int x, int y, int width, int height, Color backGround, Color foreGround, int arcWidth, int arcHeight) {
        RoundButton button = new RoundButton(text, width, height, arcWidth, arcHeight);
        button.setBounds(x, y, width, height);
        button.setBackground(backGround);
        button.setForeground(foreGround);
        button.setFocusable(false);
        button.setFont(new Font(null, Font.BOLD, 17));
        return button;
    }

    public void volume() {
        sliderVolume.addChangeListener(e -> {
            int sliderValue = sliderVolume.getValue();
            float normalizedVolume = sliderValue / 100.0f;
            audio.setVolumeControl(normalizedVolume);
            String percentage = sliderValue + "%";
            AppPanel.label.setVisible(true);
            AppPanel.label.setText(percentage);
            new Timer(2500, actionEvent -> {
                AppPanel.label.setVisible(false);
            }).start();
        });
    }

    public void playState() {
        audio.play();
        audio.isPlaying = true;
        playPauseButton.setText(play);
        playTime.start();
        AppPanel.timer.start();
    }

    public void pauseState() {
        audio.pause();
        audio.isPlaying = false;
        playPauseButton.setText(pause);
        playTime.stop();
        AppPanel.timer.stop();

    }

    private void playPause() {
        if (audio.isPlaying) {
            pauseState();
        } else {
            playState();
        }
    }

    private void hoverButton(JButton button) {
        button.setForeground(new Color(134, 134, 16));
        button.setBackground(new Color(0, 0, 0, 100));
    }

    private void hoverBackButton(JButton button) {
        button.setForeground(forengoundButtonsColor);
        button.setBackground(buttonsColor);
    }


    private void repeatAndShuffleButtonsHover(JButton button) {
        button.setBackground(new Color(0, 0, 0, 100));
    }

    private void repeatAndShuffleButtonsHoverBack(JButton button) {
        button.setBackground((buttonsColor));
    }

    public void action() {
        setAction(closeButton);
        setAction(playPauseButton);
        setAction(previousButton);
        setAction(nextButton);
        setAction(repeatButton);
        setAction(shuffleButton);
        setAction(volumeUpButton);
        setAction(volumeDownButton);
        setAction(arrowButton);
        setAction(playBackSpeed);
        setAction(minusTenSecondsButton);
        setAction(plusTenSecondsButton);
    }


    private void playBackSpeed() {
        String[] speeds = {"1.0X", "1.25X", "1.5X", "1.75X", "2.0X"};
        float[] speedFactors = {1.0f, 1.25f, 1.5f, 1.75f, 2.0f};
        for (int i = 0; i < speeds.length; i++) {
            if (speeds[i].equals(defaultSpeed)) {
                defaultSpeed = speeds[(i + 1) % speeds.length];
                playBackSpeed.setText(defaultSpeed);
                onOff(defaultSpeed);
                float newSpeed = speedFactors[(i + 1) % speeds.length];
                break;
            }
        }
    }

    private void onOff(String text) {
        AppPanel.label.setVisible(true);
        AppPanel.label.setText(text);
        Timer timer = new Timer(2500, e -> {
            AppPanel.label.setVisible(false);
            AppPanel.label.repaint();
            AppPanel.label.revalidate();
        });
        timer.setRepeats(false);
        timer.start();

    }


    private void additionalMenu() {
        if (arrowButton.getText().equals(arrowUp)) {
            arrowButton.setText(arrowDown);
            previousButton.setVisible(false);
            playPauseButton.setVisible(false);
            nextButton.setVisible(false);
            shuffleButton.setVisible(true);
            repeatButton.setVisible(true);
            volumeDownButton.setVisible(false);
            volumeUpButton.setVisible(false);
            minusTenSecondsButton.setVisible(true);
            playBackSpeed.setVisible(true);
            plusTenSecondsButton.setVisible(true);
        } else {
            arrowButton.setText(arrowUp);
            minusTenSecondsButton.setVisible(false);
            playBackSpeed.setVisible(false);
            plusTenSecondsButton.setVisible(false);
            volumeDownButton.setVisible(true);
            volumeUpButton.setVisible(true);
            shuffleButton.setVisible(false);
            repeatButton.setVisible(false);
            previousButton.setVisible(true);
            playPauseButton.setVisible(true);
            nextButton.setVisible(true);
        }
    }

    private void setAction(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object object = e.getSource();
                if (object == closeButton) {
                    System.exit(0);
                } else if (object == arrowButton) {
                    additionalMenu();
                } else if (object == playPauseButton) {
                    playPause();
                } else if (object == nextButton) {
                    audio.nextTrack();
                    playPauseButton.setText(play);
                } else if (object == previousButton) {
                    audio.previousTrack();
                    playPauseButton.setText(play);
                } else if (object == playBackSpeed) {
                    playBackSpeed();
                } else if (object == plusTenSecondsButton) {
                    audio.forwardTenSeconds();
                    int splayVal = sliderPlay.getValue();
                    sliderPlay.setValue(splayVal + 10);
                    onOff("⏩");
                } else if (object == minusTenSecondsButton) {
                    audio.backwardTenSeconds();
                    int splayVal = sliderPlay.getValue();
                    sliderPlay.setValue(splayVal - 10);
                    onOff("⏪");
                } else if (object == repeatButton) {
                    if (repeatButton.getForeground().equals(forengoundButtonsColor)) {
                        repeatButton.setForeground(new Color(134, 134, 16));
                        onOff("\uD83D\uDD01");
                        audio.repeatTrackOn();
                    } else {
                        repeatButton.setForeground(forengoundButtonsColor);
                        onOff("❌");
                        audio.repeatTrackOff();
                    }
                } else if (object == shuffleButton) {
                    if (shuffleButton.getForeground().equals(forengoundButtonsColor)) {
                        audio.randomPlay();
                        playPauseButton.setText(play);
                        shuffleButton.setForeground(new Color(134, 134, 16));
                        onOff("\uD83D\uDD00");
                    } else {
                        shuffleButton.setForeground(forengoundButtonsColor);
                        onOff("❌");
                    }
                } else if (object == volumeUpButton) {
                    int currentValue = sliderVolume.getValue();
                    if (currentValue < sliderVolume.getMaximum()) {
                        sliderVolume.setValue(currentValue + 5);
                    }
                } else if (object == volumeDownButton) {
                    int currentValue = sliderVolume.getValue();
                    if (currentValue > sliderVolume.getMinimum()) {
                        sliderVolume.setValue(currentValue - 5);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (buttons1.contains(e.getSource())) {
                    hoverButton((JButton) e.getSource());
                }
                if (buttons2.contains(e.getSource())) {
                    repeatAndShuffleButtonsHover((JButton) e.getSource());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (buttons1.contains(e.getSource())) {
                    hoverBackButton((JButton) e.getSource());
                }
                if (buttons2.contains(e.getSource())) {
                    repeatAndShuffleButtonsHoverBack((JButton) e.getSource());
                }
            }
        });
    }
}
