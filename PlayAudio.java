package MediaPlayer;

import javax.sound.sampled.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PlayAudio {
    private final List<Path> listOfMusic = new LinkedList<>();
    public int currentTrack;
    public boolean isPlaying = true;
    public Clip audioClip;
    AudioInputStream audioStream;
    public FloatControl volumeControl;
    public String songName = "";
    public String artistName = "";
    public Path filePath;
    public boolean repeat = false;
    public String fileDirectory;
    public float globalVolume = 1.0f;
    long lengthOfTheClipForward;
    long backwardTime = 0;
    long forwardTime = 0;
    int minutesForward = 0;
    int secondsForward = 0;
    int remainingSecondsForward = 0;
    int minutesBackward = 0;
    int secondsBackward = 0;
    int remainingSecondsBackward = 0;

    public PlayAudio() {
        fileDirectory = "src\\MediaPlayer\\musi.wav or mp3";
        try {
            Files.walk(Paths.get(fileDirectory)).filter(path -> {
                String fileName = path.toString().toLowerCase();
                return fileName.endsWith(".wav");
            }).forEach(listOfMusic::add);
            filePath = listOfMusic.getFirst();
            currentFile(filePath);
            if (!listOfMusic.isEmpty()) {
                filePath = listOfMusic.getFirst();
                currentFile(filePath);
            }
        } catch (Exception e) {
            System.out.println("Error loading audio file." + e);
        }
    }

    public void currentFile(Path filePath) {
        try {
            audioStream = AudioSystem.getAudioInputStream(filePath.toFile());
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            volumeControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolumeControl(globalVolume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolumeControl(float volume) {
        if (volume < 0.0f || volume > 1.0f) {
            throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
        }
        if (volumeControl != null) {
            globalVolume = volume;
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float decibels = min + (max - min) * volume;
            volumeControl.setValue(decibels);
        }
    }

    public void play() {
        if (audioClip != null && !audioClip.isRunning()) {
            nameOfTrack(String.valueOf(filePath));
            audioClip.start();
            System.out.println("Music playing.");
        }
    }


    public void pause() {
        if (audioClip.isRunning() && audioClip != null) {
            audioClip.stop();
            System.out.println("Music paused.");
        }
    }

//    private static AudioFormat getAudioFormat(float speed, AudioInputStream audioStream){
//        AudioFormat audioFormat = audioStream.getFormat();
//        float playBackSpeed = audioFormat.getFrameRate() * speed;
//        return new AudioFormat(
//                audioFormat.getEncoding(),
//                playBackSpeed,
//                audioFormat.getSampleSizeInBits(),
//                audioFormat.getChannels(),
//                audioFormat.getFrameSize(),
//                playBackSpeed,
//                audioFormat.isBigEndian()
//        );
//    }


    public void randomPlay() {
        if (audioClip.isOpen()) {
            audioClip.close();
        }
        Random random = new Random();
        changeTrack();
        int index = random.nextInt(listOfMusic.size());
        filePath = listOfMusic.get(index);
        currentFile(filePath);
        nameOfTrack(String.valueOf(filePath));
        play();
    }

    public void repeatTrackOn() {
        if (audioClip.isRunning()) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            repeat = true;
        }
    }


    public void repeatTrackOff() {
        if (repeat && audioClip != null && audioClip.isOpen()) {
            audioClip.loop(0);
            repeat = false;
        }
    }

    public void seekBySeconds(int seconds) {
        long currentPosition = audioClip.getMicrosecondPosition();
        long newPosition = currentPosition + (seconds * 1_000_000L);
        newPosition = Math.max(newPosition, 0);
        newPosition = Math.min(newPosition, audioClip.getMicrosecondLength());
        audioClip.setMicrosecondPosition(newPosition);
        if (seconds < 0) {
            backwardTime = newPosition;
        } else if (seconds > 0) {
            forwardTime = newPosition;
        }
    }

    public void backwardTenSeconds() {
        seekBySeconds(-10);
    }

    public void forwardTenSeconds() {
        seekBySeconds(10);
    }


    private void nameOfTrack(String fileName) {
        fileName = filePath.getFileName().toString();
        String[] parts = fileName.split("-");
        songName = parts[1];
        artistName = parts[0];
    }

    public void nextTrack() {
        pause();
        currentTrack = listOfMusic.indexOf(filePath);
        if (currentTrack < listOfMusic.size() - 1) {
            filePath = listOfMusic.get(currentTrack + 1);
            currentFile(filePath);
            play();
        }
        changeTrack();
    }

    public void previousTrack() {
        pause();
        currentTrack = listOfMusic.indexOf(filePath);
        if (currentTrack > 0) {
            filePath = listOfMusic.get(currentTrack - 1);
            currentFile(filePath);
            play();
        }
        changeTrack();
    }

    private void changeTrack() {
        ControlPanel.sliderPlay.setValue(0);
        ControlPanel.playTime.start();
        AppPanel.timer.stop();
        AppPanel.timer.start();
    }


    public String updateTimeForward() {
        long clipLength = audioClip.getMicrosecondLength();
        if (clipLength != lengthOfTheClipForward) {
            forwardTime = 0;
            lengthOfTheClipForward = clipLength;
        }
        forwardTime += 1_000_000;
        if (forwardTime > clipLength) {
            forwardTime = clipLength;
        }
        secondsForward = (int) (forwardTime / 1_000_000);
        minutesForward = secondsForward / 60;
        remainingSecondsForward = secondsForward % 60;
        if (repeat) {
            if (forwardTime == clipLength) {
                forwardTime = 0;
                audioClip.setMicrosecondPosition(0);
                audioClip.start();
            }
        }
        return String.format("%02d : %02d", minutesForward, remainingSecondsForward);
    }

    public String updateTimeBackward() {
        long clipLength = audioClip.getMicrosecondLength();
        backwardTime = clipLength - forwardTime;
        if (backwardTime < 0) {
            backwardTime = 0;
        }
        secondsBackward = (int) (backwardTime / 1_000_000);
        minutesBackward = secondsBackward / 60;
        remainingSecondsBackward = secondsBackward % 60;
        if (minutesBackward == 0 && remainingSecondsBackward == 0) {
            nextTrack();
        }
        return String.format("%02d:%02d", minutesBackward, remainingSecondsBackward);
    }


    public String lengthOfClip() {
        int seconds = (int) (audioClip.getMicrosecondLength() / 1_000_000);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d : %02d", minutes, remainingSeconds);
    }
}
