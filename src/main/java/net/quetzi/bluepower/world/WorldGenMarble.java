package net.quetzi.bluepower.world;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class WorldGenMarble extends WorldGenMinable {
    
    LinkedList marbleVein = new LinkedList();
    HashSet    veinsList  = new HashSet();
    Block      block;
    int        numberOfBlocks;
    
    public WorldGenMarble(Block block, int num) {
    
        super(block, num);
        this.block = block;
        this.numberOfBlocks = num;
    }
    
    private void addBlock(int x, int y, int z, int num) {
    
        List marbleCandidate = Arrays.asList(x, y, z);
        if (this.veinsList.contains(marbleCandidate)) return;
        this.marbleVein.addLast(Arrays.asList(x, y, z, num));
        this.veinsList.add(marbleCandidate);
    }
    
    private void searchBlock(World world, int x, int y, int z, int num) {
    
        if (world.isAirBlock(x - 1, y, z) || world.isAirBlock(x + 1, y, z) || world.isAirBlock(x, y - 1, z) || world.isAirBlock(x, y + 1, z)
                || world.isAirBlock(x, y, z - 1) || world.isAirBlock(x, y, z + 1)) {
            num = 6;
        }
        addBlock(x - 1, y, z, num);
        addBlock(x + 1, y, z, num);
        addBlock(x, y - 1, z, num);
        addBlock(x, y + 1, z, num);
        addBlock(x, y, z - 1, num);
        addBlock(x, y, z + 1, num);
    }
    
    public boolean generate(World world, Random random, int x, int y, int z) {
    
        if (!world.blockExists(x, y, z)) { return false; }
        
        int i = y;
        while (world.getBlock(x, i, z) != Blocks.stone) {
            if (i > 96) return false; // Don't generate marble over y96
            i++;
            addBlock(x, i, z, 6);
        }
        while ((this.marbleVein.size() > 0) && (this.numberOfBlocks > 0)) {
            List blocksToGenerate = (List) this.marbleVein.removeFirst();
            Integer[] blockToSet = (Integer[]) blocksToGenerate.toArray();
            if (world.getBlock(blockToSet[0], blockToSet[1], blockToSet[2]) == Blocks.stone) {
                world.setBlock(blockToSet[0], blockToSet[1], blockToSet[2], this.block);
                if (blockToSet[3] > 0) {
                    searchBlock(world, blockToSet[0], blockToSet[1], blockToSet[2], blockToSet[3] - 1);
                }
                this.numberOfBlocks -= 1;
            }
        }
        return true;
    }
}
