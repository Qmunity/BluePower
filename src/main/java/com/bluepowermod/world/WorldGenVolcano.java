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

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.BPConfig;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.*;

/**
 * 
 * @author MineMaarten
 */
public class WorldGenVolcano extends Feature<NoFeatureConfig> {

    private static final int MAX_VOLCANO_RADIUS = 200; // absolute max radius a volcano can have, this should be a
    // magnitude bigger than an average volcano radius.
    private HashMap<Pos, Integer> volcanoMap;
    private final List<Block> alterBlocks = new ArrayList<>();

    public WorldGenVolcano(Codec<NoFeatureConfig> codec) {
        super(codec);
        alterBlocks.add( BPBlocks.amethyst_block);
        alterBlocks.add(BPBlocks.ruby_block);
        alterBlocks.add(BPBlocks.sapphire_block);
        if(BPConfig.CONFIG.generateTungstenInVolcano.get()) {
            alterBlocks.add(BPBlocks.tungsten_block);
        }
    }

    @Override
    public boolean func_230362_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, BlockPos blockPos, NoFeatureConfig noFeatureConfig) {
        int startChunkX = blockPos.getX() >> 8;
        int startChunkZ = blockPos.getZ() >> 8;
        volcanoMap = new HashMap<>();
        ((SharedSeedRandom)rand).setLargeFeatureSeed(world.func_234938_ad_(), startChunkX, startChunkZ);
        int volcanoHeight = 100 + rand.nextInt(40);

        List<Pos>[] distMap = calculateDistMap();
        boolean first = true;
        int middleX = (startChunkX << 8) + 128;
        int middleZ = (startChunkZ << 8) + 128;
        for (int dist = 0; dist < distMap.length; dist++) {// Loop through every XZ position of the volcano, in order of how close the positions are
            // from the center. The volcano will be generated from the center to the edge.
            List<Pos> distList = distMap[dist];
            boolean isFinished = true;// Will stay true as long as there were still blocks being generated at this distance from the volcano.
            for (Pos p : distList) {
                int posHeight = first ? volcanoHeight : getNewVolcanoHeight(p, rand, dist);
                if (posHeight > 0) {
                    volcanoMap.put(new Pos(p.x, p.z), posHeight);
                    if (!first && middleX + p.x >> 4 == blockPos.getX() >> 4 && middleZ + p.z >> 4 == blockPos.getZ() >> 4) {
                        int worldHeight = world.getHeight(Heightmap.Type.WORLD_SURFACE, p.x + middleX, p.z + middleZ);
                        for (int i = posHeight; i > 0 && (i > worldHeight || canReplace(world, p.x + middleX, i, p.z + middleZ)); i--) {
                            func_230367_a_(world, new BlockPos(p.x + middleX, i, p.z + middleZ), BPBlocks.basalt.getDefaultState());
                        }
                        for (int i = posHeight + 1; i < volcanoHeight; i++) {
                            if (canReplace(world, p.x + middleX, i, p.z + middleZ)
                                    && world.getBlockState(new BlockPos(p.x + middleX, i, p.z + middleZ)).getMaterial() != Material.WATER)
                                func_230367_a_(world, new BlockPos(p.x + middleX, i, p.z + middleZ), Blocks.AIR.getDefaultState());
                        }
                    }
                    isFinished = false;
                }
                first = false;
            }
            if (isFinished)
                break;
        }
        if(middleX >> 4 == blockPos.getX() >> 4 && middleZ >> 4 == blockPos.getZ() >> 4) {
            generateLavaColumn(world, middleX, volcanoHeight, middleZ, rand);
            generateLootChamber(world, middleX, rand.nextInt(volcanoHeight - 80) + 60, middleZ, rand);
        }
        return true;

    }

    private boolean canReplace(IWorld world, int x, int y, int z) {

        if (world.isAirBlock(new BlockPos(x, y, z)))
            return true;
        Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        Material material = world.getBlockState(new BlockPos(x, y, z)).getMaterial();
        return material == Material.WOOD || material == Material.CACTUS || material == Material.LEAVES || material == Material.PLANTS
                || material == Material.TALL_PLANTS || block == Blocks.WATER;
    }

    private void generateLavaColumn(IWorld world, int x, int topY, int z, Random rand) {
        // world.setBlock(x, topY, z, Blocks.lava);
        if (rand.nextDouble() < BPConfig.CONFIG.volcanoActiveToInactiveRatio.get()) {
            func_230367_a_(world, new BlockPos(x, topY, z), BPBlocks.cracked_basalt_lava.getDefaultState());
        } else {
            func_230367_a_(world, new BlockPos(x, topY, z), Blocks.LAVA.getDefaultState());
            world.getPendingFluidTicks().scheduleTick(new BlockPos(x, topY, z), Blocks.LAVA.getDefaultState().getFluidState().getFluid(), 10);
        }
        for (int y = topY - 1; y >= 10; y--) {
            if (world.getBlockState(new BlockPos(x, y, z)) != Blocks.BEDROCK.getDefaultState()) {
                func_230367_a_(world, new BlockPos(x + 1, y, z), BPBlocks.basalt.getDefaultState());
                func_230367_a_(world, new BlockPos(x - 1, y, z), BPBlocks.basalt.getDefaultState());
                func_230367_a_(world, new BlockPos(x, y, z + 1), BPBlocks.basalt.getDefaultState());
                func_230367_a_(world, new BlockPos(x, y, z - 1), BPBlocks.basalt.getDefaultState());
                func_230367_a_(world, new BlockPos(x, y, z), Blocks.LAVA.getDefaultState());
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
     * @param requestedPos New volcano position
     * @param rand
     * @param distFromCenter
     * @return
     */
    private int getNewVolcanoHeight(Pos requestedPos, Random rand, int distFromCenter) {

        int neighborCount = 0;
        int totalHeight = 0;
        for (int x = requestedPos.x - 1; x <= requestedPos.x + 1; x++) {
            for (int z = requestedPos.z - 1; z <= requestedPos.z + 1; z++) {
                Integer neighborHeight = volcanoMap.get(new Pos(x, z));
                if (neighborHeight != null) {
                    neighborCount++;
                    totalHeight += neighborHeight;
                }
            }
        }
        if (neighborCount != 0) {
            double avgHeight = (double) totalHeight / neighborCount;
            if (rand.nextInt(5) != 0)
                return (int) avgHeight - 2;
            // Formula that defines how fast the volcano descends. Using a square function to make it steeper at the top, and added randomness.
            int blocksDown;
            if (distFromCenter < 2) {
                blocksDown = 0;
            } else if (distFromCenter == 2) {
                blocksDown = rand.nextInt(2);
            } else {
                blocksDown = (int) (Math.pow(avgHeight - 60 + 1, 1.2) * 0.005D + (rand.nextDouble() - 0.5) * 3 + 0.4D);
            }
            if (blocksDown < 0)
                blocksDown = 0;
            return (int) avgHeight - blocksDown;
        } else {
            return -1;
        }
    }

    private void generateLootChamber(IWorld world, int middleX, int startY, int middleZ, Random rand) {
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
                        func_230367_a_(world, new BlockPos(x, y, z), spawnGlass ? BPBlocks.reinforced_sapphire_glass.getDefaultState() : Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        for (Direction d : Direction.values()) {
            if (d != Direction.UP && d != Direction.DOWN) {
                if (rand.nextInt(2) == 0) {
                    generateAltar(world, middleX + d.getXOffset() * roomSize / 2, startY - 1, middleZ + d.getZOffset() * roomSize / 2, rand, d);
                }
            }
        }
    }

    private void generateAltar(IWorld world, int startX, int startY, int startZ, Random rand, Direction dir) {
        generateLootChest(world, new BlockPos(startX, startY + 1, startZ), rand, dir);
        Direction opDir = dir.getOpposite();
        Block altarBlock = alterBlocks.get(new Random().nextInt(alterBlocks.size()));
        setAltarBlockAndPossiblyTrap(world, startX, startY, startZ, rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + opDir.getXOffset(), startY, startZ + opDir.getZOffset(), rand, altarBlock);
        Direction sideDir = Direction.DOWN;
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.getXOffset(), startY, startZ + sideDir.getZOffset(), rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.getXOffset() + opDir.getXOffset(), startY, startZ + sideDir.getZOffset() + opDir.getZOffset(), rand,
                altarBlock);
        sideDir = sideDir.getOpposite();
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.getXOffset(), startY, startZ + sideDir.getZOffset(), rand, altarBlock);
        setAltarBlockAndPossiblyTrap(world, startX + sideDir.getXOffset() + opDir.getXOffset(), startY, startZ + sideDir.getZOffset() + opDir.getZOffset(), rand,
                altarBlock);

    }

    private void setAltarBlockAndPossiblyTrap(IWorld world, int x, int y, int z, Random rand, Block altarBlock) {
        func_230367_a_(world, new BlockPos(x, y, z), altarBlock.getDefaultState());
        if (rand.nextInt(6) == 0) {
            func_230367_a_(world, new BlockPos(x, y - 1, z), Blocks.TNT.getDefaultState());
            func_230367_a_(world, new BlockPos(x, y - 2, z), Blocks.REDSTONE_BLOCK.getDefaultState());
        }
    }

    private void generateLootChest(IWorld world, BlockPos pos, Random rand, Direction dir) {
        func_230367_a_(world, pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, dir.getOpposite()));
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof ChestTileEntity){
            if (rand.nextInt(5) == 0 && BPConfig.CONFIG.generateTungstenInVolcano.get()) {
                ((ChestTileEntity)te).setInventorySlotContents(13, new ItemStack(BPItems.tungsten_ingot,
                        5 + rand.nextInt(10)));
            } else {
                ((ChestTileEntity)te).setLootTable(LootTables.CHESTS_SIMPLE_DUNGEON, rand.nextInt());
            }
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
