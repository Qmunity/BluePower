package net.quetzi.bluepower.api.part;

import net.minecraftforge.common.util.ForgeDirection;

public enum FaceDirection {
    
    FRONT("front"), BACK("back"), LEFT("left"), RIGHT("right");
    
    private FaceDirection(String name) {
    
        this.name = name;
    }
    
    private String name; //used in texture paths
                         
    public FaceDirection getOpposite() {
    
        return FaceDirection.getDirection((getDirection() + 2) % 4);
    }
    
    public int getDirection() {
    
        switch (this) {
            case FRONT:
                return 0;
            case LEFT:
                return 1;
            case BACK:
                return 2;
            case RIGHT:
                return 3;
        }
        return -1;
    }
    
    public String getName() {
    
        return name;
    }
    
    public static FaceDirection getDirection(int id) {
    
        for (FaceDirection d : values())
            if (d.getDirection() == id) return d;
        
        return null;
    }
    
    public static FaceDirection getDirection(ForgeDirection face, ForgeDirection direction, int rotation) {
    
        FaceDirection dir = null;
        
        int rot = rotation;
        
        switch (face) {
            case UP:
            case DOWN:
                switch (direction) {
                    case NORTH:
                        dir = FRONT;
                        break;
                    case EAST:
                        dir = RIGHT;
                        break;
                    case SOUTH:
                        dir = BACK;
                        break;
                    case WEST:
                        dir = LEFT;
                        break;
                    default:
                        break;
                }
                
                if (face == ForgeDirection.UP && dir != null) dir = dir.getOpposite();
                break;
            case WEST:
            case EAST:
                rot++;
                switch (direction) {
                    case UP:
                        dir = FRONT;
                        break;
                    case NORTH:
                        dir = RIGHT;
                        break;
                    case DOWN:
                        dir = BACK;
                        break;
                    case SOUTH:
                        dir = LEFT;
                        break;
                    default:
                        break;
                }
                
                if (face == ForgeDirection.WEST && dir != null) dir = dir.getOpposite();
                break;
            case NORTH:
            case SOUTH:
                switch (direction) {
                    case UP:
                        dir = FRONT;
                        break;
                    case EAST:
                        dir = RIGHT;
                        break;
                    case DOWN:
                        dir = BACK;
                        break;
                    case WEST:
                        dir = LEFT;
                        break;
                    default:
                        break;
                }
                
                if (face == ForgeDirection.NORTH && dir != null) dir = dir.getOpposite();
                break;
            default:
                break;
        }
        
        if (dir != null) dir = getDirection((dir.getDirection() + rot) % 4);
        
        return dir;
    }
    
}
