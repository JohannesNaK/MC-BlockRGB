package me.johannesk.blockrgb.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ImageAnalyzer {

    private static final int RGB_DIFFERENCE = 30;
    private static final List<String> acceptableFormats = Arrays.asList(".jpeg", ".png");
    public static Map<String, Integer> materialToRGB(List<String> materialNames, File  dir) {
        Map<String, Integer> mappedRGB = new HashMap<>();
        for (File imageFile : dir.listFiles()) {
            String blockImage = imageFile.getName().toLowerCase();
            int dotAt = blockImage.indexOf(".");
            if (materialNames.contains(blockImage.substring(0,dotAt))){
                if (!(acceptableFormats.contains(blockImage.substring(dotAt))))
                    continue;
                try {
                    blockImage = blockImage.substring(0,dotAt);
                    BufferedImage image = ImageIO.read(imageFile);
                    int dominanteRGB =  findDominateRGB(image);
                    mappedRGB.put(blockImage, dominanteRGB);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

        }
        return mappedRGB;
    }

    private static int findDominateRGB(BufferedImage buffImage){
        List<Integer> values = new ArrayList<>();

        //Obtain RGB values from buffered image
        for (int x = 0; x < buffImage.getWidth(); x++){
            int rgb;
            for (int y = 0; y < buffImage.getHeight(); y++){
                rgb = buffImage.getRGB(x,y);
                values.add(rgb);
            }
        }
        Map<Integer, Integer> rgbScores = new HashMap<>();
        double diff = 0;
        int r,g,b,sR,sG,sB;
        int score = 0;
        int dominatRGB = 0;
        int maxScore = 0;
        //Find most common RGB values within the RGB_DIFFERENCE threshold.
        for (int rgb :values){
            r = (rgb>>16)&0xFF;
            g = (rgb>>8)&0xFF;
            b = (rgb)&0xFF;
            for (int compareRGB : values){
                sR = (rgb>>16)&0xFF;
                sG = (rgb>>8)&0xFF;
                sB = (rgb)&0xFF;
                if (compareRGB != rgb ) {
                    diff = Math.sqrt(Math.pow(r-sR,2) + Math.pow(g-sG,2) + Math.pow(b-sB,2));
                    if (diff < RGB_DIFFERENCE) {
                        rgbScores.put(rgb,score++);
                    }
                }
            }
            if (score > maxScore) {
                dominatRGB = rgb;
                maxScore = score;
            }
            score = 0;
        }
        return Math.abs(dominatRGB);
    }
}
