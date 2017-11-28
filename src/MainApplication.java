import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.media.jai.*;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {

        //Ask user to compress an image or load a compressed image.
        System.out.println("Charles Knight - Image Compression & Decompression");
        System.out.print("\n\t 1. Run Compression & Decompression Process \n\t 2. Load & Decompress a compressed image " + "\nSelect Option: ");
        Scanner scanner = new Scanner(System.in);
        int uChoice = scanner.nextInt();

        switch (uChoice) {
            case 1:
                compressImage();
                break;
            case 2:
                loadImageFromDisk();
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
        compressedImage.outputToFile(fd.getDirectory() + "\\output\\"); //Output the compressed image to a file (ckcomp file)

        System.out.println("------------------------------------------------------------------------");
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

    private static ImgCompressed loadImageFromDisk()
    {
        //TODO
        return null;
    }

}
