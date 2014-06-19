package net.quetzi.bluepower.tileentities.tier3;

public class InvalidOPCodeException extends RuntimeException {
	public InvalidOPCodeException(String op){
		super("invalid OP code "+op);
	}
}
