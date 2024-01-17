package me.johannesk.blockrgb.utils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockFinder {
    //BlockFinder finds approperiate blocks to use for image analysis..


    private  final List<String> forbiddenBlocks = new ArrayList<>();
    //

    public BlockFinder(){
        this.forbiddenBlocks.add("door");
        this.forbiddenBlocks.add("button");
        this.forbiddenBlocks.add("plate");
        this.forbiddenBlocks.add("sign");
        this.forbiddenBlocks.add("redstone_block");
    }
    public  List<String> findAppropriateBlocks(){
        List<String> approperiateBlocks = new ArrayList<>();
        for (Material material : Material.values()){
            if (material.isBlock() && material.isSolid() && !(material.hasGravity())){
               if (!forbiddenBlocks.contains(material.name()))
                    approperiateBlocks.add(material.name().toLowerCase());
            }
        }
        return approperiateBlocks;
    }
}
