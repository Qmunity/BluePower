package net.quetzi.bluepower.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.*;

public class WorldGenVolcano extends WorldGenMinable {
    LinkedList layerFill = new LinkedList();
    HashMap currentLayerFill = new HashMap();
    private Block block;
    private int numberOfBlocks;

    public WorldGenVolcano(Block block, int num) {
        super(block, num);
        this.block = block;
        this.numberOfBlocks = num;
    }

    private void addBlock(int x, int y, int z, int sides) {
        if (sides <= 0)
            return;
        List searchBlock = Arrays.asList(x, z);
        Integer i = (Integer) this.currentLayerFill.get(searchBlock);

        if ((i != null) && (sides <= i))
            return;
        this.layerFill.addLast(Arrays.asList(x, y, z));

        this.currentLayerFill.put(searchBlock, sides);
    }

    private void searchBlock(int x, int y, int z, int sides, Random random) {
        int rp = random.nextInt(16);
        addBlock(x - 1, y, z, (rp & 0x1) > 0 ? sides - 1 : sides);
        addBlock(x + 1, y, z, (rp & 0x2) > 0 ? sides - 1 : sides);
        addBlock(x, y, z - 1, (rp & 0x4) > 0 ? sides - 1 : sides);
        addBlock(x, y, z + 1, (rp & 0x8) > 0 ? sides - 1 : sides);
    }

    public boolean canReplaceBlock(Block block) {
        if ((block == Blocks.air) || (block == Blocks.water) || (block == Blocks.flowing_water) || (block instanceof BlockFlower) || (block == Blocks.grass) || (block == Blocks.dirt) || (block == Blocks.log) || (block == Blocks.log2) || (block == Blocks.leaves) || (block == Blocks.leaves2)) {
            return true;
        }
        return false;
    }

    public void clearArea(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block == Blocks.air) {
            return;
        }
        if ((block != Blocks.log) && (block != Blocks.log2) && (block != Blocks.leaves)
                && (block != Blocks.leaves2) && (block != Blocks.snow_layer) && (block != Blocks.vine)) {
            return;
        }
        world.setBlock(x, y, z, Blocks.air);
        clearArea(world, x, y + 1, z);
    }

    private void genLavaColumn(World world, int x, int y, int z) {
        int height = world.getHeightValue(x, z);
        // Create column of lava with basalt on 4 sides down to bedrock
        while (canReplaceBlock(world.getBlock(x, height - 1, z)))
            height--;
        for (int i = y; i < height; i++) {
            world.setBlock(x, i, z, Blocks.lava);
            world.setBlock(x - 1, i, z, this.block);
            world.setBlock(x + 1, i, z, this.block);
            world.setBlock(x, i, z - 1, this.block);
            world.setBlock(x, i, z + 1, this.block);
        }
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        if (world.getBlock(x, y, z) != Blocks.lava) {
            return false;
        }
        genLavaColumn(world, x, y, z);
        int head = 3 + random.nextInt(4);
        int spread = random.nextInt(3);
        int surfaceHeight = world.getHeightValue(x, z);
        int currentY = surfaceHeight;

        // While there are blocks left to add
        while (this.numberOfBlocks > 0) {
            boolean atTop = false;
            // if the layer is empty we need to fill it
            while (this.layerFill.size() == 0) {
                world.setBlock(x, currentY, z, Blocks.lava); // set the centre block to lava
                this.currentLayerFill.clear(); //clear out the temporary layer data
                searchBlock(x, currentY, z, head, random);
                currentY++;
                if (currentY > 200) {
                    world.setBlock(x, currentY, z, Blocks.flowing_lava);
                    while ((currentY > surfaceHeight) && (world.getBlock(x, currentY, z) == Blocks.lava)) {
                        world.markBlockForUpdate(x, currentY, z);
                        world.notifyBlocksOfNeighborChange(x, currentY, z, Blocks.lava);
                        world.scheduledUpdatesAreImmediate = true;
                        Blocks.lava.updateTick(world, x, currentY, z, random);

                        world.scheduledUpdatesAreImmediate = false;
                        currentY--;
                    }
                    atTop = true;
                    break;
                }
            }
            if (atTop) {
                break;
            }

            Integer[] blockCoord = (Integer[]) ((List) this.layerFill.removeFirst()).toArray();

            if (world.blockExists(blockCoord[0], 64, blockCoord[2])) {
                int pow = (Integer) this.currentLayerFill.get(Arrays.asList(blockCoord[0], blockCoord[2]));
                int currentYIndex = world.getHeightValue(blockCoord[0], blockCoord[2]);

                while ((currentYIndex <= blockCoord[1]) && (canReplaceBlock(world.getBlock(blockCoord[0], currentYIndex - 1, blockCoord[2]))))
                    currentYIndex--;
                if (currentYIndex <= blockCoord[1]) {
                    Block block = world.getBlock(blockCoord[0], currentYIndex, blockCoord[2]);
                    if (canReplaceBlock(block)) {
                        clearArea(world, blockCoord[0], currentYIndex, blockCoord[2]);
                        world.setBlock(blockCoord[0], currentYIndex, blockCoord[2], this.block);

                        if (blockCoord[1] > currentYIndex) {
                            pow = Math.max(pow, spread);
                        }
                        searchBlock(blockCoord[0], currentYIndex, blockCoord[2], pow, random);
                        this.numberOfBlocks -= 1;
                    }
                }
            }
        }
        return true;
    }
}
