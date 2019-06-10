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

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.OreFeature;

import java.util.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class WorldGenMarble extends OreFeature {

    LinkedList marbleVein = new LinkedList();
    HashSet veinsList = new HashSet();
    Block block;
    int numberOfBlocks;

    public WorldGenMarble(Block block, int num) {
        super(block.getDefaultState(), num);
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
    public boolean generate(World world, Random rand, BlockPos pos) {

        if (world.isAirBlock(pos)) {
            return false;
        }

        int i = pos.getY();
        while (world.getBlockState(new BlockPos(pos.getX(), i, pos.getZ())) != Blocks.STONE) {
            if (i > 96) return false; // Don't generate marble over y96
            i++;
            addBlock(pos.getX(), i, pos.getZ(), 6);
        }
        while ((this.marbleVein.size() > 0) && (this.numberOfBlocks > 0)) {
            List blocksToGenerate = (List) this.marbleVein.removeFirst();
            Integer[] blockToSet = (Integer[]) blocksToGenerate.toArray();
            if (world.getBlockState(new BlockPos(blockToSet[0], blockToSet[1], blockToSet[2])).getBlock() == Blocks.STONE) {
                world.setBlockState(new BlockPos(blockToSet[0], blockToSet[1], blockToSet[2]), this.block.getDefaultState());
                if (blockToSet[3] > 0) {
                    searchBlock(world, blockToSet[0], blockToSet[1], blockToSet[2], blockToSet[3] - 1);
                }
                this.numberOfBlocks -= 1;
            }
        }
        return true;
    }
}
