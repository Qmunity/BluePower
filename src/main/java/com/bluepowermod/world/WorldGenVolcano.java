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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.Config;

/**
 * 
 * @author MineMaarten
 */
public class WorldGenVolcano {

    private static final int MAX_VOLCANO_RADIUS = 200; // absolute max radius a volcano can have, this should be a
    // magnitude bigger than an average volcano radius.
    private final Map<Pos, Integer> volcanoMap = new HashMap<Pos, Integer>();

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
                    volcanoMap.put(p, posHeight);
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
                int neighborHeight = getNeighborHeight(x, z);
                if (neighborHeight != -1) {
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
                blocksDown = (int) (Math.pow(avgHeight - worldHeight + 1, 1.2) * 0.02D + (rand.nextDouble() - 0.5) * 3 + 0.4D);
            }
            if (blocksDown < 0)
                blocksDown = 0;
            int newHeight = (int) avgHeight - blocksDown;
            return newHeight;
        } else {
            return -1;
        }
    }

    /**
     * This helper method is created so we don't have to create an object just to do a volcanoMap.get(new Pos(x,z)).
     * 
     * @param x
     * @param z
     * @return
     */
    private int getNeighborHeight(int x, int z) {

        for (Map.Entry<Pos, Integer> entry : volcanoMap.entrySet()) {
            if (entry.getKey().x == x && entry.getKey().z == z)
                return entry.getValue();
        }
        return -1;
    }

    public class Pos {

        public final int x, z;

        public Pos(int x, int z) {

            this.x = x;
            this.z = z;
        }
    }
}
