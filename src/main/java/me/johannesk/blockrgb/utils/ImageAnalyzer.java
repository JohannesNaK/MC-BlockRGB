package me.johannesk.blockrgb.utils;

import org.bukkit.Bukkit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ImageAnalyzer {

    private static final int RGB_DIFFERENCE = 50;
    public static Map<String, Integer> materialToRGB(List<String> materialNames, String dirPath) {
        File[] dir = new File(dirPath).listFiles();
        Map<String, Integer> mappedRGB = new HashMap<>();
        int i = 0;
        for (File imageFile : dir) {
            String blockImage = imageFile.getName().toLowerCase();
            if (!blockImage.contains("."))
                continue;
            blockImage = blockImage.substring(0,blockImage.indexOf("."));
            if (materialNames.contains(blockImage)){
                try {
                    BufferedImage image = ImageIO.read(imageFile);
                    if (image == null)
                        continue;
                    int dominanteRGB =  findDominateRGB(image);
                    mappedRGB.put(blockImage, dominanteRGB);
                } catch (IOException e){
                   Bukkit.getLogger().warning("Skipping " + blockImage + ", could not read image");
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
        int score;
        //Find most common RGB values within the RGB_DIFFERENCE value.
        for (int rgb :values){
            r = (rgb>>16)&0xFF;
            g = (rgb>>8)&0xFF;
            b = (rgb)&0xFF;
            score = 0;
            for (int compareRGB : values){
                sR = (rgb>>16)&0xFF;
                sG = (rgb>>8)&0xFF;
                sB = (rgb)&0xFF;
                if (compareRGB != rgb ) {
                    diff = Math.sqrt(Math.pow(r-sR,2) + Math.pow(g-sG,2) + Math.pow(b-sB,2));
                    if (diff < RGB_DIFFERENCE)
                        rgbScores.put(rgb,score++);
                }
            }
        }
        int dominatRGB = 0;
        int maxScore = 0;
        //Find the dominant rgb
        for (int rgb : rgbScores.keySet()){
             if (rgbScores.get(rgb) > maxScore){
                 dominatRGB = rgb;
                 maxScore = rgbScores.get(rgb);
             }
        }
        return dominatRGB;
    }
}
