package com.bluepowermod.util;

public class DebugHelper {

    public static boolean isDebugModeEnabled() {

        return Boolean.parseBoolean(System.getProperty("bluepower.debug", "false"));
    }

}
