import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Decompression {

    public static void runDecompressAlg(ImgCompressed objCompressedImage)
    {
        System.out.println("Starting Decompression Algorithm");
        int imgWidth = objCompressedImage.getImgWidth();
        int imgHeight = objCompressedImage.getImgHeight();

        int[][] redDecompressed = decompressColourLevel(objCompressedImage.getRedCompressed(), imgWidth, imgHeight);
        int[][] greenDecompressed = decompressColourLevel(objCompressedImage.getGreenCompressed(), imgWidth, imgHeight);
        int[][] blueDecompressed = decompressColourLevel(objCompressedImage.getBlueCompressed(), imgWidth, imgHeight);


        int[][] rgbDecompressed = new int[imgWidth][imgHeight];

        for (int cY = 0; cY < imgHeight; cY++)
        {
            for (int cX = 0; cX < imgWidth; cX++)
            {
                Color test = new Color(redDecompressed[cX][cY],  greenDecompressed[cX][cY], blueDecompressed[cX][cY]);
                rgbDecompressed[cX][cY] = test.getRGB();
            }
        }


        try {
            outputToFile(rgbDecompressed, imgWidth, imgHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Decompression Algorithm Finished");
    }

    private static int[][] decompressColourLevel(byte[] compressedColourArray, int imgWidth, int imgHeight)
    {
        int[][] decompressedColourLevel = new int[imgWidth][imgHeight];

        int curSelectedByte = 0;

        for (int ypos = 0; ypos < imgHeight; ypos++) {


            for (int xpos = 0; xpos < imgWidth; ) {



                int colourValue = compressedColourArray[curSelectedByte++];
                int runLength = Byte.toUnsignedInt(compressedColourArray[curSelectedByte++]);


                for (int rx = 0; rx < runLength; rx++) {
                   // System.out.println("XPos: " + xpos + " | Rx: " + rx  +  " RunLength: " + runLength +  " YPos: " + ypos);
                    decompressedColourLevel[xpos++][ypos] = colourValue & 0xFF;
                }
                //xpos += runLength;
            }

        }

        return decompressedColourLevel;
    }



    public static void outputToFile(int[][] rgbDecompressed, int imgWidth, int imgHeight) throws IOException {
        File outputFile = new File("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\Images\\outputs\\decompressedout.bmp");
        BufferedImage imgOutput = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);


        for (int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                imgOutput.setRGB(x, y, rgbDecompressed[x][y]);

            }
        }
        ImageIO.write(imgOutput, "bmp", outputFile);

        Desktop dt = Desktop.getDesktop();
        dt.open(outputFile);
        System.out.println("The image file was written successfully!!");

    }
}
