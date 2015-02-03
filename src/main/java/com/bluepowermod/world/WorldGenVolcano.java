/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.world;

import static net.minecraftforge.common.ChestGenHooks.DUNGEON_CHEST;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.Config;

/**
 * 
 * @author MineMaarten
 */
public class WorldGenVolcano {

    private static final int MAX_VOLCANO_RADIUS = 200; // absolute max radius a volcano can have, this should be a
    // magnitude bigger than an average volcano radius.
    private final LongHashMap volcanoMap = new LongHashMap();
    private static final Block[] ALTAR_BLOCKS = new Block[] { BPBlocks.amethyst_block, BPBlocks.ruby_block, BPBlocks.sapphire_block,
            BPBlocks.tungsten_block };

    public void generate(World world, Random rand, int middleX, int volcanoHeight, int middleZ) {
        List<Pos>[] distMap = calculateDistMap();
        boolean first = true;
        for (int dist = 0; dist < distMap.length; dist++) {// Loop through every XZ position of the volcano, in order of how close the positions are
            // from the center. The volcano will be generated from the center to the edge.
            List<Pos> distList = distMap[dist];
            boolean isFinished = true;// Will stay true as long as there were still blocks being generated at this distance from the volcano.
            for (Pos p : distList) {
                int worldHeight = world.getHeightValue(p.x + middleX, p.z + middleZ) - 1;
                int posHeight = first ? volcanoHeight : getNewVolcanoHeight(worldHeight, p, rand, dist);
                if (posHeight >= 0 && (posHeight > worldHeight || canReplace(world, p.x + middleX, posHeight, p.z + middleZ))) {// If the calculated
                    // desired volcano
                    // height is higher
                    // than the world
                    // height, generate.
                    volcanoMap.add(ChunkCoordIntPair.chunkXZ2Int(p.x, p.z), posHeight);
                    if (!first) {
                        for (int i = posHeight; i > 0 && (i > worldHeight || canReplace(world, p.x + middleX, i, p.z + middleZ)); i--) {
                            world.setBlock(p.x + middleX, i, p.z + middleZ, BPBlocks.basalt, 0, 2);
                        }
                        for (int i = posHeight + 1; i < volcanoHeight; i++) {
                            if (canReplace(world, p.x + middleX, i, p.z + middleZ)
                                    && world.getBlock(p.x + middleX, i, p.z + middleZ).getMaterial() != Material.water)
                                world.setBlock(p.x + middleX, i, p.z + middleZ, Blocks.air, 0, 2);
                        }
                    }
                    isFinished = false;
                }
                first = false;
            }
            if (isFinished)
                break;
        }
        generateLavaColumn(world, middleX, volcanoHeight, middleZ, rand);
        generateLootChamber(world, middleX, volcanoHeight - 20, middleZ, rand);
    }

    private boolean canReplace(World world, int x, int y, int z) {

        if (world.isAirBlock(x, y, z))
            return true;
        Block block = world.getBlock(x, y, z);
        Material material = block.getMaterial();
        return material == Material.wood || material == Material.cactus || material == Material.leaves || material == Material.plants
                || material == Material.vine || block == Blocks.water || block == Blocks.flowing_water;
    }

    private void generateLavaColumn(World world, int x, int topY, int z, Random rand) {

        // world.setBlock(x, topY, z, Blocks.lava);
        if (rand.nextDouble() < Config.volcanoActiveToInactiveRatio) {
            world.setBlock(x, topY, z, BPBlocks.cracked_basalt_lava);
        } else {
            world.setBlock(x, topY + 1, z, Blocks.lava);
            world.setBlock(x, topY, z, Blocks.lava);// This block set, which does update neighbors, will make the lava above update.
        }
        for (int y = topY - 1; y >= 10; y--) {
            if (world.getBlock(x, y, z) != Blocks.bedrock) {
                world.setBlock(x + 1, y, z, BPBlocks.basalt, 0, 2);
                world.setBlock(x - 1, y, z, BPBlocks.basalt, 0, 2);
                world.setBlock(x, y, z + 1, BPBlocks.basalt, 0, 2);
                world.setBlock(x, y, z - 1, BPBlocks.basalt, 0, 2);
                world.setBlock(x, y, z, Blocks.lava, 0, 2);
            }
        }
    }

    /**
     * Saves an array of relative Positions with distance to origin. The index is the distance, the element the positions with that distance to the
     * origin.
     */
    @SuppressWarnings("unchecked")
    private List<Pos>[] calculateDistMap() {

        List<Pos>[] distMap = new List[MAX_VOLCANO_RADIUS];
        for (int x = -MAX_VOLCANO_RADIUS; x <= MAX_VOLCANO_RADIUS; x++) {
            for (int z = -MAX_VOLCANO_RADIUS; z <= MAX_VOLCANO_RADIUS; z++) {
                int dist = (int) Math.sqrt(x * x + z * z);
                if (dist < MAX_VOLCANO_RADIUS) {
                    List<Pos> distList = distMap[dist];
                    if (distList == null) {
                        distList = new ArrayList<Pos>();
                        distMap[dist] = distList;
                    }
                    distList.add(new Pos(x, z));
                }
            }
        }
        return distMap;
    }

    /**
     * Calculates a height for the requested position. It looks at the adjacent (already generated) volcano heights, takes the average, and blends in
     * a bit of randomness. If there are no neighbors this is the first volcano block generated, meaning it's the center, meaning it should get the
     * max height.
     *
     * @param worldHeight Terrain height
     * @param requestedPos New volcano position
     * @param rand
     * @param distFromCenter
     * @return
     */
    private int getNewVolcanoHeight(int worldHeight, Pos requestedPos, Random rand, int distFromCenter) {

        int neighborCount = 0;
        int totalHeight = 0;
        for (int x = requestedPos.x - 1; x <= requestedPos.x + 1; x++) {
            for (int z = requestedPos.z - 1; z <= requestedPos.z + 1; z++) {
                Integer neighborHeight = (Integer) volcanoMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
                if (neighborHeight != null) {
                    neighborCount++;
                    totalHeight += neighborHeight;
                }
            }
        }
        if (neighborCount != 0) {
            double avgHeight = (double) totalHeight / neighborCount;
            if ((int) avgHeight < worldHeight + 2 && rand.nextInt(5) != 0)
                return (int) avgHeight - 2;
            // Formula that defines how fast the volcano descends. Using a square function to make it steeper at the top, and added randomness.
            int blocksDown;
            if (distFromCenter < 2) {
                blocksDown = 0;
            } else if (distFromCenter == 2) {
                blocksDown = rand.nextInt(2);
            } else {
                blocksDown = (int) (Math.pow(avgHeight - worldHeight + 1, 1.2) * 0.005D + (rand.nextDouble() - 0.5) * 3 + 0.4D);
            }
            if (blocksDown < 0)
                blocksDown = 0;
            int newHeight = (int) avgHeight - blocksDown;
            return newHeight;
        } else {
            return -1;
        }
    }

    private void generateLootChamber(World world, int middleX, int startY, int middleZ, Random rand) {
        int roomSize = 9;
        int roomHeight = 5;
        int startX = middleX - roomSize / 2;
        int startZ = middleZ - roomSize / 2;

        for (int x = startX; x < startX + roomSize; x++) {
            for (int y = startY; y < startY + roomHeight; y++) {
                for (int z = startZ; z < startZ + roomSize; z++) {
                    int xOffset = Math.abs(x - middleX);
                    int zOffset = Math.abs(z - middleZ);
                    if (xOffset != 0 || zOffset != 0) {
                        boolean spawnGlass = xOffset <= 1 && zOffset <= 1;
                        world.setBlock(x, y, z, spawnGlass ? BPBlocks.reinforced_sapphire_glass : Blocks.air, 0, 2);
                    }
                }
            }
        }

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (d != ForgeDirection.UP && d != ForgeDirection.DOWN) {
                if (rand.nextInt(2) == 0) {
                    generateAltar(world, middleX + d.offsetX * roomSize / 2, startY - 1, middleZ + d.offsetZ * roomSize / 2, rand, d);
                }
            }
        }
    }

    private void generateAltar(World world, int startX, int startY, int startZ, Random rand, ForgeDirection dir) {
        generateLootChest(world, startX, startY + 1, startZ, rand, dir);
        ForgeDirection opDir = dir.getOpposite();
        Block altarBlock = ALTAR_BLOCKS[rand.nextInt(ALTAR_BLOCKS.length)];
        setAltarBlockAndPossiblyTrap(world, startX, startY, startZ, rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + opDir.offsetX, startY, startZ + opDir.offsetZ, rand, altarBlock);
        ForgeDirection sideDir = dir.getRotation(ForgeDirection.DOWN);
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.offsetX, startY, startZ + sideDir.offsetZ, rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.offsetX + opDir.offsetX, startY, startZ + sideDir.offsetZ + opDir.offsetZ, rand,
                altarBlock);
        sideDir = sideDir.getOpposite();
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.offsetX, startY, startZ + sideDir.offsetZ, rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.offsetX + opDir.offsetX, startY, startZ + sideDir.offsetZ + opDir.offsetZ, rand,
                altarBlock);

    }

    private void setAltarBlockAndPossiblyTrap(World world, int x, int y, int z, Random rand, Block altarBlock) {
        world.setBlock(x, y, z, altarBlock, 0, 2);
        if (rand.nextInt(6) == 0) {
            world.setBlock(x, y - 1, z, Blocks.tnt, 0, 2);
            world.setBlock(x, y - 2, z, Blocks.redstone_block, 0, 2);
        }
    }

    private void generateLootChest(World world, int x, int y, int z, Random rand, ForgeDirection dir) {
        world.setBlock(x, y, z, Blocks.chest, dir.getOpposite().ordinal(), 3);
        if (rand.nextInt(5) == 0) {
            ((TileEntityChest) world.getTileEntity(x, y, z)).setInventorySlotContents(13,
                    new ItemStack(BPBlocks.tungsten_block, 5 + rand.nextInt(10)));
        } else {
            WeightedRandomChestContent.generateChestContents(rand, ChestGenHooks.getItems(DUNGEON_CHEST, rand),
                    (TileEntityChest) world.getTileEntity(x, y, z), ChestGenHooks.getCount(DUNGEON_CHEST, rand));//Possibly to be added with IC designs from the community.
        }
    }

    private static class Pos {

        public final int x, z;

        public Pos(int x, int z) {

            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pos) {
                Pos pos = (Pos) object;
                return pos.x == x && pos.z == z;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return (x << 13) + z;
        }
    }
}
