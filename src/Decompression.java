import java.awt.image.BufferedImage;
import java.io.File;

public class Decompression {
//TODO: When Decompressing, make sure it is as an UNSIGNED byte (current stored as signed)


    public static void runDecompressAlg(ImgCompressed objCompressedImage)
    {
        System.out.println("Starting Decompression Algorithm");
        int imgWidth = objCompressedImage.getImgWidth();
        int imgHeight = objCompressedImage.getImgHeight();

        int[][] decImgArr = new int[imgWidth][imgHeight];

        byte[] curColourArr = objCompressedImage.getRedCompressed();
        int curSelectedByte = 0;

        for (int xpos = 0; xpos < imgWidth -1;)
        {
            byte colourValue = curColourArr[curSelectedByte++];
            byte runLength = curColourArr[curSelectedByte++];
            System.out.println("Colour value: " + colourValue + " | Run Length: " + runLength);

            for (int rx = 0; rx < runLength; rx++)
            {
                System.out.println("XPos: " + xpos + " | Rx: " + rx);
                decImgArr[xpos++][0] = colourValue & 0xFF;

            }

        }

        System.out.println("Decompression Algorithm Finished");
    }
}
