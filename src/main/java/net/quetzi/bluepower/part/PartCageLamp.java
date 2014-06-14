package net.quetzi.bluepower.part;

public class PartCageLamp extends PartLamp{

	public PartCageLamp(String colorName, Integer colorVal) {
		
		super(colorName, colorVal);
	}
	
	
	@Override
    public void renderBottom(int pass){
    	
    }
    
	@Override
    public void renderGlass(int pass){
    	
    }
	
	@Override
    public String getType() {
    
        return "cagelamp" + colorName;
    }
	
	@Override
    public String getUnlocalizedName() {
    
        return "cagelamp." + colorName;
    }

}
