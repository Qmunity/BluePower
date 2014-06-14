package net.quetzi.bluepower.part;

public class PartCageLamp extends PartLamp{

	public PartCageLamp(String colorName, Integer colorVal) {
		
		super(colorName, colorVal);
	}
	
	
	@Override
    public void renderBase(int pass){
    	
    }
    
	@Override
    public void renderLamp(int pass){
    	
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
