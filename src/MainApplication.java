import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MainApplication {

    public static void main(String[] args){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\LineTest.bmp"));
            Compression.runCompressImage(img);





        }
        catch (IOException e) {
            System.out.println("An error occurred - Failed to load image!\n" + e);
        }
    }

}
