package me.johannesk.blockrgb;

import me.johannesk.blockrgb.commands.AnalyzeCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BlockRGB extends JavaPlugin {


    public static final String ROOT_DIR = "plugins" +File.separator;
    private static BlockRGB instance;

    @Override
    public void onEnable() {
        this.getCommand("analyze").setExecutor(new AnalyzeCommand("block"));
        instance = this;
    }

    public static BlockRGB getInstance(){
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
