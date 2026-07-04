/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.Vec3;

public class DirectionHelper {

    /**
     * Returns an Array of Directions for Rotation on a given facing direction.
     * @author MoreThanHidden
     */
    public static Direction[] ArrayFromDirection(Direction direction){
        Direction[] dirs = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        switch (direction){
            case DOWN:
                dirs = new Direction[]{Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};
                break;
            case UP:
                dirs = new Direction[]{Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST};
                break;
            case NORTH:
                dirs = new Direction[]{Direction.UP, Direction.WEST, Direction.DOWN, Direction.EAST};
                break;
            case SOUTH:
                dirs = new Direction[]{Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST};
                break;
            case WEST:
                dirs = new Direction[]{Direction.UP, Direction.SOUTH , Direction.DOWN, Direction.NORTH};
                break;
            case EAST:
                dirs = new Direction[]{Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH};
                break;
        }
        return dirs;
    }

    public static final Direction[][] OUTPUT_TABLE = {
            {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST},
            {Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST},
            {Direction.DOWN, Direction.WEST, Direction.UP, Direction.EAST},
            {Direction.DOWN, Direction.EAST, Direction.UP, Direction.WEST},
            {Direction.DOWN, Direction.SOUTH, Direction.UP, Direction.NORTH},
            {Direction.DOWN, Direction.NORTH, Direction.UP, Direction.SOUTH}
    };

    public static int getRotationFromContext(BlockPlaceContext context) {
        // how do we want to orient the block when we place it?
        // attachment face should be the face that was clicked
        // what about the rotation?

        // option A: output faces away from player, inverted when sneaking
        // empirical observations: this is super annoying to get the player in the right standing position,
        // especially when placing the thing on a wall

        // option B: the orientation depends on which part of the face was clicked, not the player's facing
        // this gives more control to the player but might be confusing
        // we may want to render a preview of the placement somehow

        BlockPos placePos = context.getClickedPos();
        Direction faceOfAdjacentBlock = context.getClickedFace();
        Direction directionTowardAdjacentBlock = faceOfAdjacentBlock.getOpposite();
        Vec3 relativeHitVec = context.getClickLocation().subtract(Vec3.atLowerCornerOf(placePos));
        return getRotationFromContext(placePos, directionTowardAdjacentBlock, relativeHitVec);
    }

    public static int getRotationFromContext(BlockPos placePos, Direction directionTowardAdjacentBlock, Vec3 relativeHitVec)
    {
        Direction outputDirection = getOutputDirectionFromRelativeHitVec(relativeHitVec, directionTowardAdjacentBlock);
        int rotationIndex = getRotationIndexForDirection(directionTowardAdjacentBlock, outputDirection);
        return rotationIndex;
    }

    public static int getRotationIndexForDirection(Direction attachmentFace, Direction outputDirection)
    {
        // now using the lookup table above, find the index matching our directions
        Direction[] rotatedOutputs = OUTPUT_TABLE[attachmentFace.ordinal()];
        int size = rotatedOutputs.length;
        for (int i=0; i<size; i++)
        {
            if (rotatedOutputs[i] == outputDirection)
            {
                return i;
            }
        }

        return 0;
    }

    public static Direction getOutputDirectionFromRelativeHitVec(Vec3 hitVec, Direction directionTowardBlockAttachedTo)
    {
        // we have the relative hit vector, where 0,0,0 is the bottom-left corner of the cube we are placing into
        // and 1,1,1 is the top-right
        // how do we convert this into a direction?
        // we want to ignore the attachment direction and its opposite
        // Direction has a method that converts a vector to a direction
        // we could "flatten" the hit vec's value on the axis of attachment
        // Direction::getFacingFromVector uses 0,0,0 as the center and 1,1,1 and -1,-1,-1 as corners,
        // so we'll want to map our hitvec accordingly

        Axis axis = directionTowardBlockAttachedTo.getAxis();
        float x = (float) (axis == Axis.X ? 0F : hitVec.x*2 - 1);
        float y = (float) (axis == Axis.Y ? 0F : hitVec.y*2 - 1);
        float z = (float) (axis == Axis.Z ? 0F : hitVec.z*2 - 1);

        return Direction.getNearest(x, y, z);
    }
}
