package net.quetzi.bluepower.api.part;

public enum FaceDirection {
    
    FRONT, BACK, LEFT, RIGHT;
    
    public static FaceDirection getDirection(int id){
        for(FaceDirection d : values())
            if(d.ordinal() == id)
                return d;
        
        return null;
    }
    
}
