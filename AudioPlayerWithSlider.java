package MediaPlayer;
import MediaPlayer.Slider.JSliderCustom;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class AudioPlayerWithSlider {
    private Clip audioClip;
    private JSliderCustom sliderPlay;
    private Timer playTime;

    public AudioPlayerWithSlider(String audioFilePath) {
        try {
            // Load the audio clip
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(audioFilePath));
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);

            // Create the slider
            // Create the slider
            sliderPlay = new JSliderCustom(25, 490, 365, 7, 0); // Length in milliseconds
            sliderPlay.setValue(0); // Start at the beginning

// Add change listener to update audio position when slider is adjusted
            sliderPlay.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (sliderPlay.getValueIsAdjusting()) {
                        int newPosition = sliderPlay.getValue();
                        audioClip.setMicrosecondPosition(newPosition * 1_000L); // Set position in microseconds
                    }
                }
            });

// Add mouse listener to seek audio clip position on click
            sliderPlay.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int mouseX = e.getX();
                    // Calculate the new position based on mouse click relative to slider width
                    int value = (int) ((double) mouseX / sliderPlay.getWidth() * sliderPlay.getMaximum());
                    sliderPlay.setValue(value); // Set the new value on the slider
                    audioClip.setMicrosecondPosition(value * 1_000L); // Set the new position of the audio clip in microseconds

                    // Start the audio clip if it's not already playing
                    if (!audioClip.isRunning()) {
                        audioClip.start();
                    }
                }
            });

// Timer to update the slider as the audio clip plays
            playTime = new Timer(100, e -> {
                long currentPosition = audioClip.getMicrosecondPosition();
                int currentValue = (int) ((currentPosition * (sliderPlay.getMaximum() - sliderPlay.getMinimum())) / audioClip.getMicrosecondLength());
                sliderPlay.setValue(currentValue); // Update the slider value based on current position

                // Stop the timer when the audio reaches the end
                if (currentPosition >= audioClip.getMicrosecondLength()) {
                    ((Timer) e.getSource()).stop();
                }
            });
            playTime.start();


            // Create a simple UI
            JFrame frame = new JFrame("Audio Player");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(sliderPlay);
            frame.setSize(400, 100);
            frame.setVisible(true);

            // Start playing the audio
            audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Provide the path to an audio file
        String audioFilePath = "src\\mp3MediaPlayer\\musi.wav or mp3\\MATEMATIC - Seara [ ezmp3.cc ].wav"; // Ensure this file exists
        new AudioPlayerWithSlider(audioFilePath);
    }
}
