public class ImgCompressed {
    private int imgHeight;
    private int imgWidth;
    private byte[] redRLCompressed;
    private byte[] greenRLCompresed;
    private byte[] blueRLCompressed;

    private int totalByteSize = -1;

    public ImgCompressed(int height, int width, byte[] redRL, byte[] greenRL, byte[] blueRL)
    {
        imgHeight = height;
        imgWidth = width;
        redRLCompressed = redRL;
        greenRLCompresed = greenRL;
        blueRLCompressed = blueRL;

        totalByteSize = redRL.length + greenRL.length + blueRL.length;

        System.out.println("Created a compressed image object!");
        System.out.println("Size of image (amount of bytes): " + totalByteSize);
    }

    public int gettotalByteSize()
    {
        return totalByteSize;
    }

    public byte[] getRedCompressed() //TODO: Merge into one function (Red/Green/Blue)
    {
        return redRLCompressed;
    }

    public int getImgWidth()
    {
        return imgWidth;
    }

    public int getImgHeight()
    {
        return imgHeight;
    }

    public void outputToFile(String toPath)
    {
        //TODO
    }



}
