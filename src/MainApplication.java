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
            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();

            String runLengthEncoding = "";
            int rangeLimit = 2;

            System.out.println("width is: " + imgWidth);

            for (int x = 0; x < imgWidth; x++) {
                int tmpRunCounter = 1;
                int curPixelValue = img.getRGB(x, 0); //TODO: Replace with a function
                curPixelValue = (curPixelValue >> 16) & 0x000000FF; //[REF 1]

                for (int ix = x + 1; ix < imgWidth + 1; ix++) {

                    if (ix == imgWidth) {
                        x = imgWidth;
                        break;
                    } else {
                        int thisPixelValue = img.getRGB(ix, 0);
                        thisPixelValue = (thisPixelValue >> 16) & 0x000000FF;

                        if (Math.abs(curPixelValue - thisPixelValue) <= rangeLimit)
                            tmpRunCounter++;
                        else {
                            x = ix - 1;
                            break;
                        }
                    }
                }

                runLengthEncoding += curPixelValue + "|" + tmpRunCounter + "|";
            }

            System.out.println(runLengthEncoding);



        }
        catch (IOException e) {
            System.out.println("An error occurred - Failed to load image!\n" + e);
        }
    }

}
