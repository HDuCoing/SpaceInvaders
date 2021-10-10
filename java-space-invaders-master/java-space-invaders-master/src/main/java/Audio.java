import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/*
@author Nilu; Massey via GameEngine
 */
public class Audio {
    // Class used to store an audio clip
    public class AudioClip {
        // Format
        AudioFormat mFormat;

        // Audio Data
        byte[] mData;

        // Buffer Length
        long mLength;

        // Loop Clip
        Clip mLoopClip;

        public Clip getLoopClip() {
            // return mLoopClip
            return mLoopClip;
        }

        public void setLoopClip(Clip clip) {
            // Set mLoopClip to clip
            mLoopClip = clip;
        }

        public AudioFormat getAudioFormat() {
            // Return mFormat
            return mFormat;
        }

        public byte[] getData() {
            // Return mData
            return mData;
        }

        public long getBufferSize() {
            // Return mLength
            return mLength;
        }

        public AudioClip(AudioInputStream stream) {
            // Get Format
            mFormat = stream.getFormat();

            // Get length (in Frames)
            mLength = stream.getFrameLength() * mFormat.getFrameSize();

            // Allocate Buffer Data
            mData = new byte[(int)mLength];

            try {
                // Read data
                stream.read(mData);
            } catch(Exception exception) {
                // Print Error
                System.out.println("Error reading Audio File\n");

                // Exit
                System.exit(1);
            }

            // Set LoopClip to null
            mLoopClip = null;
        }
    }

    // Loads the AudioClip stored in the file specified by filename
    public Audio.AudioClip loadAudio(String filename) {
        try {
            // Open File
            File file = new File(filename);

            // Open Audio Input Stream
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);

            // Create Audio Clip
            Audio.AudioClip clip = new Audio.AudioClip(audio);

            // Return Audio Clip
            return clip;
        } catch(Exception e) {
            // Catch Exception
            System.out.println("Error: cannot open Audio File " + filename + "\n");
        }

        // Return Null
        return null;
    }

    // Plays an AudioClip
    public void playAudio(Audio.AudioClip audioClip) {
        // Check audioClip for null
        if(audioClip == null) {
            // Print error message
            System.out.println("Error: audioClip is null\n");

            // Return
            return;
        }

        try {
            // Create a Clip
            Clip clip = AudioSystem.getClip();

            // Load data
            clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int)audioClip.getBufferSize());

            // Play Clip
            clip.start();
        } catch(Exception exception) {
            // Display Error Message
            System.out.println("Error playing Audio Clip\n");
        }
    }

}
