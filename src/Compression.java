import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class Compression {
    private static final int RANGE_LIMIT = 4; //TODO: Move this so its user selectable option (compression quality)


    public static ImgCompressed runCompressionAlg(BufferedImage img) {
        System.out.println("Running Compression...");

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        ByteArrayOutputStream redArr = new ByteArrayOutputStream();
        ByteArrayOutputStream greenArr = new ByteArrayOutputStream();
        ByteArrayOutputStream blueArr = new ByteArrayOutputStream();

        compressToByteArr(img, redArr, 'R', imgWidth, imgHeight);
        compressToByteArr(img, greenArr, 'G', imgWidth, imgHeight);
        compressToByteArr(img, blueArr, 'B', imgWidth, imgHeight);

        ImgCompressed resImg = new ImgCompressed(imgWidth, imgHeight, redArr.toByteArray(), greenArr.toByteArray(), blueArr.toByteArray());
        System.out.println("Compression Algorithm Completed!");

        return resImg;

    }

    private static void compressToByteArr(BufferedImage img, ByteArrayOutputStream useStream, char colourCode, int imgWidth, int imgHeight) {
        for (int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                int tmpRunCounter = 1;
                byte curPixelValue = getColourLevel(img, colourCode, x, y);

                for (int ix = x + 1; ix < imgWidth + 1; ix++) {
                    if (ix == imgWidth) {
                        x = imgWidth;
                        break;
                    } else {
                        int nextPixelValue = getColourLevel(img, colourCode, ix, y);

                        if (Math.abs(curPixelValue - nextPixelValue) <= RANGE_LIMIT)
                            tmpRunCounter++; //TODO: Ensure tmpRunCounter doesn't exceed 255 (byte limit!)
                        else {
                            x = ix - 1;
                            break;
                        }
                    }
                }
                useStream.write(curPixelValue);
                useStream.write(tmpRunCounter);
            }
        }

        System.out.println("Completed Compression on colour: " + colourCode + " | Size: " + useStream.size());
        //TODO: Show Compression Ratio (original file to compressed file)
    }


    private static byte getColourLevel(BufferedImage img, char getWhichRGB, int x, int y) {
        int pixVal = img.getRGB(x, y);
        int resVal = 0;

        switch (getWhichRGB) {
            case 'R':
                resVal = (pixVal >> 16) & 0xFF; //[REF 1]
                break;
            case 'G':
                resVal = (pixVal >> 8) & 0xFF; //[REF 1]
                break;
            case 'B':
                resVal = pixVal & 0xFF; //[REF 1]
                break;
            default:
                System.out.println("WARNING! Invalid RGB Selector!!!");
                break;

        }
        return (byte) resVal;
    }

}
