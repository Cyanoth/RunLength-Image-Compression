import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgCompressed {
    private int imgHeight;
    private int imgWidth;
    private byte[] redRLCompressed; //ByteArray for compressed Red Level [colourValue, runLength, ..]
    private byte[] greenRLCompressed; //ByteArray for compressed Green Level [colourValue, runLength, ..]
    private byte[] blueRLCompressed; //ByteArray for compressed Blue Level [colourValue, runLength, ..]

    private int totalByteSize = -1; //Total Size of Red+Green+Blue in Bytes
    private int compressLevel = -1; //Level of compression

    /**
     * Constructor for a compressed image that is stored in memory.
     * Created by the Compression Algorithm
     * @param width Image Width
     * @param height Image Height
     * @param redRL Red Compressed ByteArray
     * @param greenRL Green Compress ByteArray
     * @param blueRL Blue Compress Byte Array
     * @param compresslevel What the range limit for the compression was.
     */
    public ImgCompressed(int width, int height, byte[] redRL, byte[] greenRL, byte[] blueRL, int compresslevel)
    {
        imgHeight = height;
        imgWidth = width;
        redRLCompressed = redRL;
        greenRLCompressed = greenRL;
        blueRLCompressed = blueRL;
        compressLevel = compresslevel;
        totalByteSize = redRL.length + greenRL.length + blueRL.length;

        System.out.println("A compressed image object was created in memory. Total Size of Image (Bytes): " + totalByteSize);
    }

    public byte[] getRedCompressed() { return redRLCompressed; }

    public byte[] getGreenCompressed() { return greenRLCompressed; }

    public byte[] getBlueCompressed() { return blueRLCompressed; }

    public int getImgWidth()
    {
        return imgWidth;
    }

    public int getImgHeight()
    {
        return imgHeight;
    }

    public int getTotalByteSize()
    {
        return totalByteSize;
    }

    public int getCompressLevel() { return compressLevel; }


    /**
     * Output the compressed image as a 'ckcomp' file.
     * @param toPath Where the compressed image should be outputted.
     */
    public void outputToFile(String toPath)
    {
        try {
            FileOutputStream fos = new FileOutputStream(toPath + "CompressedOutput.ckcomp");
            fos.write(Integer.toString(imgWidth).getBytes());
            fos.write(",".getBytes());
            fos.write(Integer.toString(imgHeight).getBytes());
            fos.write(",".getBytes());
            fos.write(Integer.toString(compressLevel).getBytes());
            fos.write(",".getBytes());
            fos.write(Integer.toString(redRLCompressed.length + 1).getBytes());
            fos.write(",".getBytes());
            fos.write(Integer.toString(greenRLCompressed.length + 1).getBytes());
            fos.write(",".getBytes());
            fos.write(Integer.toString(blueRLCompressed.length +1).getBytes());
            fos.write("\n".getBytes());
            fos.write(redRLCompressed);
            System.out.println(redRLCompressed.length);
            fos.write("\n".getBytes());
            fos.write(greenRLCompressed);
            System.out.println(greenRLCompressed.length);
            fos.write("\n".getBytes());
            fos.write(blueRLCompressed);
            System.out.println(blueRLCompressed.length);

            fos.close();
            System.out.println("The compressed image file was written to the disk successfully (" + toPath + "CompressedOutput.ckcomp" + ")");

        }
        catch (IOException e) {
            System.out.println("An error occurred during picture export... " + e);
        }
    }


}
