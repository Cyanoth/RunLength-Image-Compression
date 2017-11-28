import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.media.jai.*;

import java.io.File;
import java.io.IOException;

public class MainApplication {

    public static void main(String[] args) {
        ImgCompressed compressedImage;



        try {
            ImageDecoder ppmImgDecoder = ImageCodec.createImageDecoder("PNM", new File("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\Images\\2.ppm"), null);
            BufferedImage img = new RenderedImageAdapter(ppmImgDecoder.decodeAsRenderedImage()).getAsBufferedImage();

            //img = ImageIO.read(new File("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\Lenna.bmp"));
            compressedImage = Compression.runCompressionAlg(img);
            compressedImage.outputToFile("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\Images\\outputs\\", "testing");
            Decompression.runDecompressAlg(compressedImage);

        }
        catch (IOException e) {
            System.out.println("An error occurred - Failed to load image!\n" + e);
        }
    }

    //TODO: Load Compressed Image from File.

}
