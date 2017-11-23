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

        int[][] redDecompressed = decompressColourLevel(objCompressedImage,'R', imgWidth, imgHeight);
        int[][] greenDecompressed = decompressColourLevel(objCompressedImage, 'G', imgWidth, imgHeight);
        int[][] blueDecompressed = decompressColourLevel(objCompressedImage, 'B', imgWidth, imgHeight);


        int[][] rgbDecompressed = new int[imgWidth][imgHeight];

        for (int cY = 0; cY < imgHeight; cY++)
        {
            for (int cX = 0; cX < imgWidth; cX++)
            {
                int rgb = redDecompressed[cX][cY];
                rgb = (rgb << 8) + greenDecompressed[cX][cY];
                rgb = (rgb << 8) + blueDecompressed[cX][cY];
                rgbDecompressed[cX][cY] = rgb;
            }
        }


        try {
            outputToFile(rgbDecompressed, imgWidth, imgHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Decompression Algorithm Finished");
    }

    private static int[][] decompressColourLevel(ImgCompressed compressedImgObj, char getWhichRGB, int imgWidth, int imgHeight)
    {
        int[][] decompressedColourLevel = new int[imgWidth][imgHeight];
        byte[] compressedColourArray;

        switch (getWhichRGB) {
            case 'R':
                compressedColourArray = compressedImgObj.getRedCompressed();
                break;
            case 'G':
                compressedColourArray = compressedImgObj.getGreenCompressed();
                break;
            case 'B':
                compressedColourArray = compressedImgObj.getBlueCompressed();
                break;
            default: //todo return error
                return null;
        }

        int curSelectedByte = 0;

        for (int ypos = 0; ypos < imgHeight; ypos++) {
            for (int xpos = 0; xpos < imgWidth; ) {
                byte colourValue = compressedColourArray[curSelectedByte++];
                byte runLength = compressedColourArray[curSelectedByte++];
                System.out.println("Colour value: " + colourValue + " (" + (255 + colourValue) + ") | Run Length: " + runLength);

                for (int rx = 0; rx < runLength; rx++) {
                    System.out.println("XPos: " + xpos + " | Rx: " + rx);
                    decompressedColourLevel[xpos++][ypos] = colourValue & 0xFF;

                }

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
