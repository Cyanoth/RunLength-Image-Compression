import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class Compression {
     private static int rangeLimit = 5;

    /**
     * Constructor for the compression functions
     * @param img - A Java BufferedImage already loaded from disk
     * @param rangelimit Maximum colour difference allowed between next pixel (default 5)
     * @return A compressed image object in memory
     */
    public static ImgCompressed runCompressionAlg(BufferedImage img, int rangelimit) {
        System.out.println("Running Compression Algorithm...");

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        rangeLimit = rangelimit;

        /* Compress each colour stream (Red, Green, Blue) */
        System.out.println("--------------------------------------");
        ByteArrayOutputStream redArr =  compressToByteArr(img, 'R', imgWidth, imgHeight);
        ByteArrayOutputStream greenArr = compressToByteArr(img, 'G', imgWidth, imgHeight);
        ByteArrayOutputStream blueArr = compressToByteArr(img, 'B', imgWidth, imgHeight);
        System.out.println("--------------------------------------");

        //Create compress image in memory with the compress colour streams
        ImgCompressed resImg = new ImgCompressed(imgWidth, imgHeight, redArr.toByteArray(), greenArr.toByteArray(), blueArr.toByteArray(), rangelimit);
        System.out.println("Compression Algorithm Completed!");

        return resImg;
    }

    private static ByteArrayOutputStream compressToByteArr(BufferedImage img, char colourCode, int imgWidth, int imgHeight) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (int y = 0; y < imgHeight; y++) { //Iterate over each Y column in the image.
            for (int x = 0; x < imgWidth; x++) {
                int runCounter = 1; //Run Length Counter
                int pixelColourCounter = getColourLevel(img, colourCode, x, y); //Keep count of pixel values (for mean value)
                byte curPixelValue = getColourLevel(img, colourCode, x, y); //Used to compare the next pixel colour value with this.

                for (int ix = x + 1; ix < imgWidth + 1; ix++) { //For each next pixel from x -> imgwidth or to runlength.
                    if (ix == imgWidth) {
                        x = imgWidth; //Break row if its reached the end of image width.
                        break;
                    } else {
                        byte nextPixelValue = getColourLevel(img, colourCode, ix, y);

                        /* Compare next pixel colour value, if its within range limit then include it.
                         * BUGFIX: Limit runCounter to maximum 200 (prevent exceeding image boundaries)
                         */
                        if ((Math.abs(curPixelValue - nextPixelValue) <= rangeLimit) && runCounter < 200) {
                            runCounter++; //Increment the run length counter
                            pixelColourCounter += nextPixelValue; //Add this pixel to colour counter
                        }
                        else { //Else Next Pixel is not within range limit, so start next break and start new..
                            x = ix - 1;
                            break;
                        }
                    }
                }

                //Write to 'ByteStream Array': [meanColourValue, runLength, meanColourValue, runLength...]
                output.write((int) Math.floor(pixelColourCounter / runCounter)); //Calculate and Write Mean Value then RunLength
                output.write((byte) runCounter);
            }
        }

        System.out.println("Completed Compression on colour: " + colourCode + " | Size: " + output.size());
        return output;
    }

    /**
     * Gets the value of a colour level (0 - 255)
     * @param img A Java BufferedImage loaded already from disk
     * @param getWhichRGB Specify character: R = Red, B = Blue, G = Green
     * @param x X Corrd
     * @param y Y Corrd
     * @return Byte range 0-255 for colour level at x,y
     */
    private static byte getColourLevel(BufferedImage img, char getWhichRGB, int x, int y) {
        Color getColour = new Color(img.getRGB(x, y));

          switch (getWhichRGB) {
              case 'R':
                  return (byte) getColour.getRed();
              case 'G':
                  return (byte) getColour.getGreen();
              case 'B':
                  return (byte) getColour.getBlue();
              default:
                  return (byte) 0;
          }
    }

}
