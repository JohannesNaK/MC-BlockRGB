package me.johannesk.blockrgb;

import me.johannesk.blockrgb.utils.ImageAnalyzer;
import me.johannesk.blockrgb.utils.BlockFinder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class BlockRGB extends JavaPlugin {

    private static final  String BLOCK_DIR_PATH = "plugins" + File.separator + "blocks";
    private static final  String SAVE_FILE_PATH = "plugins" + File.separator + "rgb_mapped_values.txt";

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!(new File(BLOCK_DIR_PATH).exists()))
            Bukkit.getLogger().warning("COULD NOT FIND BLOCK RESOURCE FOLDER!");
        else {
            BlockFinder blockFinder = new BlockFinder();
            List<String> toSearch = blockFinder.findAppropriateBlocks();
            Map<String, Integer> mappedValues = ImageAnalyzer.materialToRGB(toSearch,BLOCK_DIR_PATH);
            saveMappedValues(mappedValues);
        }

    }

    private void saveMappedValues(Map<String, Integer> mappedValues){
        File saveMappings = new File(SAVE_FILE_PATH);
        try {
            System.out.println("Saving values..");
            saveMappings.createNewFile();
            FileWriter writer = new FileWriter(saveMappings);
            for (String material : mappedValues.keySet()){
                writer.write(material + "," + mappedValues.get(material) + "\n");
            }

            writer.close();
            writer.close();
        } catch (IOException e){
            System.out.println("failed to save file..");
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
