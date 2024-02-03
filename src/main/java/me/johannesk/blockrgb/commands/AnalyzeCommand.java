package me.johannesk.blockrgb.commands;

import me.johannesk.blockrgb.BlockRGB;
import me.johannesk.blockrgb.image.BlockFinder;
import me.johannesk.blockrgb.image.ImageAnalyzer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AnalyzeCommand implements CommandExecutor {

    private static final String PERMISSION = "blockrgb.start";
    private static boolean isInUse;
    private static final  List<String> WHITELISTED_BLOCKS = new BlockFinder().findAppropriateBlocks();
    private File blockDir;
    private  Logger logger = Bukkit.getLogger();

    public AnalyzeCommand(String blockDirPath){
        blockDir = new File(BlockRGB.ROOT_DIR  + blockDirPath);
        if (!(blockDir.exists()))
            Bukkit.getLogger().warning("Could not find block dir at path " + BlockRGB.ROOT_DIR + blockDirPath);
      //  BlockFinder blockFinder = new BlockFinder();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("analyze")){
            if (sender.hasPermission(PERMISSION)) {
                if (isInUse)
                    Bukkit.getLogger().warning("Another analyzation is already being done. Please wait untill it is done before running a new one");
                if (args.length == 0) {
                    if (!(blockDir.exists())) {
                        logger.warning("Could not find default block directory at path " + blockDir);
                        logger.info("Run /analyze <directory path>  if you want to analyze another directory. The path is relative from plugins/");
                    } else {
                        startAnalyzing(blockDir);
                    }
                } else if (args.length == 1) {
                    File dir = new File(BlockRGB.ROOT_DIR + args[0]);
                    if (dir.exists()) {
                        startAnalyzing(dir);
                    } else
                        logger.warning("Could not find directory! Check if provided path is correct!");
                }
            }
        }
        return true;
    }

    private void startAnalyzing(File dir){
        logger.info("Found  directory to analyze...");

        //Running task async  to not deadlock server resources
        //following methods do not make use of the Spigot API therefore is safe to run async.
        new BukkitRunnable() {
            @Override
            public void run() {
                Map<String, Integer> mappedValues = ImageAnalyzer.materialToRGB(WHITELISTED_BLOCKS,dir);
                saveMappedValues(mappedValues, BlockRGB.ROOT_DIR + "rgb_mappings.txt");
                isInUse = false;
            }
        }.runTaskAsynchronously(BlockRGB.getInstance());
    }
    private void saveMappedValues(Map<String, Integer> mappedValues, String savePath){
        File saveMappings = new File(savePath );
        Bukkit.getLogger();
        try {
            logger.info("Saving mappings...");
            saveMappings.createNewFile();
            FileWriter writer = new FileWriter(saveMappings);
            for (String material : mappedValues.keySet()){
                writer.write(material + "," + mappedValues.get(material) + "\n");
            }
            logger.info("Done!");
            writer.close();
            writer.close();
        } catch (IOException e){
            logger.warning("failed to save file..");
        }
    }
}
