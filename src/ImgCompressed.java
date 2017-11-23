import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ImgCompressed {
    private int imgHeight;
    private int imgWidth;
    private byte[] redRLCompressed;
    private byte[] greenRLCompresed;
    private byte[] blueRLCompressed;

    private int totalByteSize = -1;

    public ImgCompressed(int width, int height, byte[] redRL, byte[] greenRL, byte[] blueRL)
    {
        imgHeight = height;
        imgWidth = width;
        redRLCompressed = redRL;
        greenRLCompresed = greenRL;
        blueRLCompressed = blueRL;

        totalByteSize = redRL.length + greenRL.length + blueRL.length;

        System.out.println("Created a compressed image object!");
        System.out.println("Size of image (amount of bytes): " + totalByteSize);
    }

    public int gettotalByteSize()
    {
        return totalByteSize;
    }

    public byte[] getRedCompressed() //TODO: Merge into one function (Red/Green/Blue)
    {
        return redRLCompressed;
    }

    public int getImgWidth()
    {
        return imgWidth;
    }

    public int getImgHeight()
    {
        return imgHeight;
    }

    public void outputToFile(String toPath, String fileName)
    {
        try {
            FileOutputStream fos = new FileOutputStream(toPath + fileName + ".ckcomp");
            fos.write(Integer.toString(imgWidth).getBytes());
            fos.write(",".getBytes());
            fos.write(Integer.toString(imgHeight).getBytes());
            fos.write("\n".getBytes());
            fos.write(redRLCompressed);
            fos.write("\n".getBytes());
            fos.write(greenRLCompresed);
            fos.write("\n".getBytes());
            fos.write(blueRLCompressed);

            fos.close();


            System.out.println("The compressed image file was written successfully!!");

        }
        catch (IOException e) {
            System.out.println("An error occurred during picture export... " + e);
        }
    }

    public byte[] getGreenCompressed() {
        return greenRLCompresed;
    }

    public byte[] getBlueCompressed() {
        return blueRLCompressed;
    }
}
