import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class Compression {
    private static final int RANGE_LIMIT = 5; //TODO: Move this so its user selectable option (compression quality)


    public static ImgCompressed runCompressionAlg(BufferedImage img) {
        System.out.println("Running Compression...");

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        ByteArrayOutputStream redArr =  compressToByteArr(img, 'R', imgWidth, imgHeight);
        ByteArrayOutputStream greenArr = compressToByteArr(img, 'G', imgWidth, imgHeight);
        ByteArrayOutputStream blueArr = compressToByteArr(img, 'B', imgWidth, imgHeight);

        ImgCompressed resImg = new ImgCompressed(imgWidth, imgHeight, redArr.toByteArray(), greenArr.toByteArray(), blueArr.toByteArray());
        System.out.println("Compression Algorithm Completed!");

        return resImg;
    }

    private static ByteArrayOutputStream compressToByteArr(BufferedImage img, char colourCode, int imgWidth, int imgHeight) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (int y = 0; y < imgHeight; y++) { //Iterate over each column
            for (int x = 0; x < imgWidth; x++) {
                int runCounter = 1; //Run Length
                int pixelColourCounter = getColourLevel(img, colourCode, x, y); //Keep count of pixel values (for mean value)

                byte curPixelValue = getColourLevel(img, colourCode, x, y); //Compare next pixel value to this.

                for (int ix = x + 1; ix < imgWidth + 1; ix++) {
                    if (ix == imgWidth) {
                        x = imgWidth; //Stop this row if its reached the end of image (x)
                        break;
                    } else {
                        byte nextPixelValue = getColourLevel(img, colourCode, ix, y);

                        if ((Math.abs(curPixelValue - nextPixelValue) <= RANGE_LIMIT) && runCounter < 200) { //Compare next pixel value, if its within range limit use it.
                            runCounter++; //Increment run length
                            pixelColourCounter += nextPixelValue; //Add to pixel colour counter
                        }
                        else { //Next Pixel not within range, so start next X.
                            x = ix - 1;
                            break;
                        }
                    }
                }
                output.write((int) Math.floor(pixelColourCounter / runCounter)); //Write to byte stream the mean value & RunLength
                output.write((byte) runCounter);
            }
        }

        System.out.println("Completed Compression on colour: " + colourCode + " | Size: " + output.size());
        return output;
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
