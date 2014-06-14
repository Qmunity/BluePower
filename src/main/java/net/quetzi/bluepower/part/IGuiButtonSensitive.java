package net.quetzi.bluepower.part;

/**
 * Implemented by BPParts, this interface can be used to sync client side gui input with server.
 */
public interface IGuiButtonSensitive {
    
    public void onButtonPress(int messageId, int value);
}
