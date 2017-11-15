import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class Compression {
    private static final int RANGE_LIMIT = 4;


    public static void runCompressImage(BufferedImage img)
    {
        System.out.println("Running Compression...");

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        ByteArrayOutputStream redArr = new ByteArrayOutputStream();


        for (int y = 0; y <imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                int tmpRunCounter = 1;
                byte curPixelValue = getColourLevel(img, 'R', x, y);

                for (int ix = x + 1; ix < imgWidth + 1; ix++) {
                    if (ix == imgWidth) {
                        x = imgWidth;
                        break;
                    } else {
                        int nextPixelValue = getColourLevel(img, 'R', ix, y);

                        if (Math.abs(curPixelValue - nextPixelValue) <= RANGE_LIMIT)
                            tmpRunCounter++;
                        else {
                            x = ix - 1;
                            break;
                        }
                    }
                }
                redArr.write(curPixelValue);
                redArr.write(tmpRunCounter);
            }
        }

        System.out.println("Compression Finished! Size Red Array: " + redArr.size());
    }

    private static byte getColourLevel(BufferedImage img, char getWhichRGB, int x, int y)
    {
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
