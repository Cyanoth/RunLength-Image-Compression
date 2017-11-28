import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Decompression {

    /**
     * Converts compressed Red, Green, Blue Levels into RGB value for each pixel.
     * @param outputPath Path where decompressed image should be outputted.
     * @param objCompressedImage A compressed image memory object.
     */
    public static void runDecompressAlg(String outputPath, ImgCompressed objCompressedImage)
    {
        System.out.println("Starting Decompression Algorithm");
        int imgWidth = objCompressedImage.getImgWidth();
        int imgHeight = objCompressedImage.getImgHeight();

        /* Decompress each colour level (from colour -> runlength */
        int[][] redDecompressed = decompressColourLevel(objCompressedImage.getRedCompressed(), imgWidth, imgHeight);
        int[][] greenDecompressed = decompressColourLevel(objCompressedImage.getGreenCompressed(), imgWidth, imgHeight);
        int[][] blueDecompressed = decompressColourLevel(objCompressedImage.getBlueCompressed(), imgWidth, imgHeight);

        int[][] rgbDecompressed = new int[imgWidth][imgHeight]; //Create a new image with RGB Values.

        for (int cY = 0; cY < imgHeight; cY++) //For each pixel, combine the red, green and blue.
        {
            for (int cX = 0; cX < imgWidth; cX++)
            {
                Color test = new Color(redDecompressed[cX][cY],  greenDecompressed[cX][cY], blueDecompressed[cX][cY]);
                rgbDecompressed[cX][cY] = test.getRGB();
            }
        }

        outputToFile(outputPath, rgbDecompressed, imgWidth, imgHeight);
        System.out.println("Decompression Algorithm Finished");
    }

    /**
     * Decompressed a compressed colour level byte array.
     * @param compressedColourArray A red/green/blue compressed array [colourValue, runLength, ..]
     * @param imgWidth The Image Width
     * @param imgHeight The Image Height
     * @return
     */
    private static int[][] decompressColourLevel(byte[] compressedColourArray, int imgWidth, int imgHeight)
    {
        int[][] decompressedColourLevel = new int[imgWidth][imgHeight];
        int curSelectedByte = 0;

        for (int ypos = 0; ypos < imgHeight; ypos++) {
            for (int xpos = 0; xpos < imgWidth; ) { //For each pixel in the image
                int colourValue = compressedColourArray[curSelectedByte++]; //Read the colour level value
                int runLength = Byte.toUnsignedInt(compressedColourArray[curSelectedByte++]); //Read the run length

                for (int rx = 0; rx < runLength; rx++) { //Up to the run length, set the colour level
                    decompressedColourLevel[xpos++][ypos] = colourValue & 0xFF;
                }
            }
        }

        return decompressedColourLevel;
    }

    /**
     * Outputs the decompressed image to a file on the disk
     * @param outputPath Where the file should be outputted to
     * @param rgbDecompressed An array with RGB values (decompressed from each colour level)
     * @param imgWidth The Image Width
     * @param imgHeight The Image Height
     */
    private static void outputToFile(String outputPath, int[][] rgbDecompressed, int imgWidth, int imgHeight) {
        File outputFile = new File(outputPath + "DecompressedOut.bmp");
        BufferedImage imgOutput = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) { //For each pixel x,y write the mean RGB value
                imgOutput.setRGB(x, y, rgbDecompressed[x][y]);

            }
        }

        try {
            ImageIO.write(imgOutput, "bmp", outputFile); //Write file and open it.
            Desktop dt = Desktop.getDesktop();
            dt.open(outputFile);
            System.out.println("The decompressed image file was written successfully! (" + outputFile.getPath() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
