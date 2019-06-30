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

import com.bluepowermod.init.Config;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class WorldGenMarble extends Feature<NoFeatureConfig> {

    private LinkedList marbleVein = new LinkedList();
    private HashSet veinsList = new HashSet();
    private int numberOfBlocks;
    private Block block;

    public WorldGenMarble(Function<Dynamic<?>, ? extends NoFeatureConfig> deserializer, Block block, int veinSize) {
        super(deserializer);
        this.block = block;
        this.numberOfBlocks = veinSize;
    }

    private void addBlock(int x, int y, int z, int num) {

        List marbleCandidate = Arrays.asList(x, y, z);
        if (this.veinsList.contains(marbleCandidate)) return;
        this.marbleVein.addLast(Arrays.asList(x, y, z, num));
        this.veinsList.add(marbleCandidate);
    }

    private void searchBlock(World world, int x, int y, int z, int num) {

        if (world.isAirBlock(new BlockPos(x - 1, y, z)) || world.isAirBlock(new BlockPos(x + 1, y, z)) || world.isAirBlock(new BlockPos(x, y - 1, z)) || world.isAirBlock(new BlockPos(x, y + 1, z))
                || world.isAirBlock(new BlockPos(x, y, z - 1)) || world.isAirBlock(new BlockPos(x, y, z + 1))) {
            num = 6;
        }
        addBlock(x - 1, y, z, num);
        addBlock(x + 1, y, z, num);
        addBlock(x, y - 1, z, num);
        addBlock(x, y + 1, z, num);
        addBlock(x, y, z - 1, num);
        addBlock(x, y, z + 1, num);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGenerator, Random random, BlockPos pos, NoFeatureConfig noFeatureConfig) {
        if (world.isAirBlock(pos)) {
            return false;
        }

        if (Config.veinSizeMarble > 0) {
            for (int i = 0; i < 4; i++) {
                int y = pos.getY();
                while (world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock() != Blocks.STONE) {
                    if (y > 96) return false; // Don't generate marble over y96
                    y++;
                    addBlock(pos.getX(), y, pos.getZ(), 6);
                }
                while ((this.marbleVein.size() > 0) && (this.numberOfBlocks > 0)) {
                    List blocksToGenerate = (List) this.marbleVein.removeFirst();
                    Integer[] blockToSet = (Integer[]) blocksToGenerate.toArray();
                    if (world.getBlockState(new BlockPos(blockToSet[0], blockToSet[1], blockToSet[2])).getBlock() == Blocks.STONE) {
                        world.setBlockState(new BlockPos(blockToSet[0], blockToSet[1], blockToSet[2]), this.block.getDefaultState(), 0);
                        if (blockToSet[3] > 0) {
                            searchBlock(world.getWorld(), blockToSet[0], blockToSet[1], blockToSet[2], blockToSet[3] - 1);
                        }
                        this.numberOfBlocks -= 1;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
