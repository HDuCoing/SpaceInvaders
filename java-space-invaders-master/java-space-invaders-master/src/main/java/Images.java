import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
/*
@author Nilu; Massey via GameEngine
 */
public class Images {
    public Image loadImage(String filename) {
        try {
            // Load Image
            Image image = ImageIO.read(new File(filename));

            // Return Image
            return image;
        } catch (IOException e) {
            // Show Error Message
            System.out.println("Error: could not load image " + filename);
            System.exit(1);
        }

        // Return null
        return null;
    }
}
