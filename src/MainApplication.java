import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.media.jai.*;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Arrays;

public class MainApplication {

    public static void main(String[] args) {
        ImgCompressed compressedImage;



        try {
            ImageDecoder ppmImgDecoder = ImageCodec.createImageDecoder("PNM", new File("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\Images\\1.ppm"), null);
            BufferedImage img = new RenderedImageAdapter(ppmImgDecoder.decodeAsRenderedImage()).getAsBufferedImage();

            compressedImage = Compression.runCompressionAlg(img);
            //Decompression.runDecompressAlg(compressedImage);
            
        }
        catch (IOException e) {
            System.out.println("An error occurred - Failed to load image!\n" + e);
        }
    }

}
