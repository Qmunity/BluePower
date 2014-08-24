package com.bluepowermod.tileentities;

/**
 * Used in tube routing when a stack is retrieved. It will apply the filter of the target TE for the retrieved stack (implementer of this interface)
 * 
 * @author MineMaarten
 */
public interface IFuzzyRetrieving {
    public int getFuzzySetting();
}
