package com.bluepowermod.compat.cc;

public class CCUtils {

    public static int packDigital(byte[] signal) {

        if (signal == null)
            return 0;
        int packed = 0;
        for (int i = 0; i < 16; i++)
            if (signal[i] != 0)
                packed |= 1 << i;
        return packed;
    }

    public static byte[] unpackDigital(int packed) {

        if (packed == 0)
            return new byte[16];
        byte[] sig = new byte[16];
        for (int i = 0; i < 16; i++)
            sig[i] = (byte) (((packed & 1 << i) == 0) ? 0 : 255);
        return sig;
    }
}
