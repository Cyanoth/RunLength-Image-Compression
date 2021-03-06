import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.media.jai.*;
import javax.swing.*;

import java.io.*;
import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {

        //Ask user to compress an image or load a compressed image.
        System.out.println("Charles Knight - Image Compression & Decompression ");
        System.out.print("\n\t 1. Run Compression & Decompression Process (FROM MEMORY) \n\t 2. Load & Decompress a compressed image (FROM DISK)" + "\nSelect Option: ");

        Scanner scanner = new Scanner(System.in);
        int uChoice = scanner.nextInt();

        switch (uChoice) {
            case 1:
                compressImage();
                break;
            case 2:
                loadImageFromDisk("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\Images\\output\\CompressedOutput.ckcomp");
                break;
            default:
                System.out.println("No option selected, exiting...");
                break;
        }
    }

    /**
     *  Runs the compression, information and decompression algorithms.
     */
    private static void compressImage() {
        ImgCompressed compressedImage;
        File imgPath;
        boolean isPPM = false; //Different way of reading a PPM file. Other files can just use the Java Library.

        FileDialog fd = new FileDialog(new JFrame(), "Choose a file", FileDialog.LOAD); //Windows dialog to select file.
        fd.setDirectory("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\");
        fd.setVisible(true);
        if (fd.getFile() == null) {
            System.out.println("No file was selected, exiting...");
            return;
        } else
            imgPath = new File(fd.getDirectory() + fd.getFile());

        if (fd.getFile().contains("ppm")) //crude and highly unreliable way to check if its PPM (improve in future)
            isPPM = true;

        System.out.println("The File Selected: " + imgPath + " | PPM: " + isPPM);

        BufferedImage img = null;
        try {
            if (isPPM)
            {
                ImageDecoder ppmImgDecoder = ImageCodec.createImageDecoder("PNM", new File(String.valueOf(imgPath)), null);
                img = new RenderedImageAdapter(ppmImgDecoder.decodeAsRenderedImage()).getAsBufferedImage();
            }
            else {
                img = ImageIO.read(imgPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Ask user to choose a compression rate. Where 0 is lossless, beyond is high compression*/
        System.out.print("Please enter a compression rate (0 = Lossless TO 10(+) = High Compression Rate, Lower Quality: ");
        Scanner scanner = new Scanner(System.in);
        int compressRate = scanner.nextInt();
        compressedImage = Compression.runCompressionAlg(img, compressRate); //Run Compression Algorithm
        compareImageSize(imgPath, compressedImage); //Show the compression ration
        compressedImage.outputToFile(fd.getDirectory() + "output\\"); //Output the compressed image to a file (ckcomp file)


        System.out.println("------------------------------------------------------------------------");
        System.out.println("Press any key to run decompression algorithm FROM MEMORY...");

        Scanner pauser = new Scanner(System.in);
        pauser.nextLine();
        pauser.close();
        Decompression.runDecompressAlg(fd.getDirectory() + "\\output\\", compressedImage); //Run Decompression Algorithm
    }


    /**
     * Outputs information regarding size of original and compress image size.
     * @param originalImagePath The original image path
     * @param compressedImage The compressed image in memory
     */
    private static void compareImageSize(File originalImagePath, ImgCompressed compressedImage)
    {
        long originalImgSize = originalImagePath.length();
        long compressedImgSize = compressedImage.getTotalByteSize();
        System.out.println("**********************************************");

        System.out.println("Compression Information:");
        System.out.println("\tCompression Level (0 - Lossless. Higher Number has greater compress rate but less Quality: " + compressedImage.getCompressLevel());
        System.out.println("\tOriginal Image Size (Bytes): " + originalImgSize);
        System.out.println("\tCompressed Image Size (Bytes): " + compressedImgSize);

        double percentageSaved = (double) Math.round(((double) (originalImgSize - compressedImgSize) / originalImgSize) * 100 * 100) / 100;
        System.out.println("\tPercent Saved (2dp): " + percentageSaved + "%");

        System.out.println("**********************************************");
    }

    /**
     * Load a compressed file from the disk
     * @param compressedImgPath Path to compressed Image file
     * @return A Compressed Image Memory Object (not used however)
     */
    private static ImgCompressed loadImageFromDisk(String compressedImgPath) {
        /* IMPORANT TO NOTE: Potential off-by-ones. Make sure when read to increment lengths by 1 to ensure correct byte is read!!!*/
        BufferedReader firstLineReader;
        ImgCompressed compressedImg = null;

        byte[] redArray;
        byte[] greenArray;
        byte[] blueArray;

        try {
            firstLineReader = new BufferedReader(new FileReader(compressedImgPath));
            String firstLineText = firstLineReader.readLine();
            String[] picOptionsArr = firstLineText.split(","); //Gives first line of file in array: [width, height, compressLevel, redLen, greenLen, blueLen]

            int imgWidth = Integer.parseInt(picOptionsArr[0]);
            int imgHeight = Integer.parseInt(picOptionsArr[1]);
            int compressLevel = Integer.parseInt(picOptionsArr[2]);
            redArray = new byte[Integer.parseInt(picOptionsArr[3])];
            greenArray = new byte[Integer.parseInt(picOptionsArr[4])]; //Set length of colour-level byte arrays from the pic options.
            blueArray = new byte[Integer.parseInt(picOptionsArr[5])];

            FileInputStream fis = new FileInputStream(compressedImgPath); //Create Byte Stream Array Reader
            long skip = fis.skip(firstLineText.getBytes().length + 1); //Ignore the first-line (skip the byte length), since we have the picOptions already
            /* Read each colour level byte array */
            fis.read(redArray);
            fis.read(greenArray);
            fis.read(blueArray);
            compressedImg = new ImgCompressed(imgWidth, imgHeight, redArray, greenArray, blueArray, compressLevel);
            Decompression.runDecompressAlg("C:\\Users\\Charlie\\IdeaProjects\\ImageCompression\\res\\Images\\output", compressedImg);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressedImg;
    }

}
