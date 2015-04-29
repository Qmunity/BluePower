package com.bluepowermod.client.render;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;

public class TessellatorHelper {

    private static Field vertexCount, rawBuffer, hasTexture, hasBrightness, hasColor, hasNormals;
    static {
        try {
            Class<Tessellator> c = Tessellator.class;

            vertexCount = c.getDeclaredField("vertexCount");
            vertexCount.setAccessible(true);

            rawBuffer = c.getDeclaredField("rawBuffer");
            rawBuffer.setAccessible(true);

            hasTexture = c.getDeclaredField("hasTexture");
            hasTexture.setAccessible(true);

            hasBrightness = c.getDeclaredField("hasBrightness");
            hasBrightness.setAccessible(true);

            hasColor = c.getDeclaredField("hasColor");
            hasColor.setAccessible(true);

            hasNormals = c.getDeclaredField("hasNormals");
            hasNormals.setAccessible(true);
        } catch (Exception ex) {
        }
    }

    private static int oldCount = -1;

    public static void saveCurrent() {

        try {
            sc();
        } catch (Exception ex) {
        }
    }

    public static List<TessellatorVertex> findDifferences() {

        try {
            return fd();
        } catch (Exception ex) {
        }

        return null;
    }

    private static void sc() throws Exception {

        oldCount = vertexCount.getInt(Tessellator.instance);
    }

    private static List<TessellatorVertex> fd() throws Exception {

        if (oldCount == -1)
            return null;

        List<TessellatorVertex> l = new ArrayList<TessellatorVertex>();

        int now = vertexCount.getInt(Tessellator.instance);

        int[] buf = (int[]) rawBuffer.get(Tessellator.instance);
        int bufIndex = oldCount * 8;
        for (int i = oldCount; i < now; i++) {
            l.add(new TessellatorVertex(buf, bufIndex));
            bufIndex += 8;
        }

        oldCount = 0;

        return l;
    }

    @SuppressWarnings("unused")
    private static class TessellatorVertex {

        private int[] buf;
        private int index;

        public TessellatorVertex(int[] buf, int index) {

            this.buf = buf;
            this.index = index;
        }

        public double x() {

            return Float.intBitsToFloat(buf[index + 0]);
        }

        public double y() {

            return Float.intBitsToFloat(buf[index + 1]);
        }

        public double z() {

            return Float.intBitsToFloat(buf[index + 2]);
        }

        public int rgb() {

            return buf[index + 5];
        }

        public double r() {

            return 0;
        }

        public double g() {

            return 0;
        }

        public double b() {

            return 0;
        }

        public double a() {

            return 0;
        }

        public double u() {

            return Float.intBitsToFloat(buf[index + 3]);
        }

        public double v() {

            return Float.intBitsToFloat(buf[index + 4]);
        }

        public int brightness() {

            return buf[index + 7];
        }

        public void setX(double x) {

            buf[index + 0] = Float.floatToRawIntBits((float) x);
        }

        public void setY(double y) {

            buf[index + 1] = Float.floatToRawIntBits((float) y);
        }

        public void setZ(double z) {

            buf[index + 2] = Float.floatToRawIntBits((float) z);
        }

        public void setR(double r) {

        }

        public void setG(double g) {

        }

        public void setB(double b) {

        }

        public void setA(double a) {

        }

        public void setU(double u) {

            buf[index + 3] = Float.floatToRawIntBits((float) u);
        }

        public void setV(double v) {

            buf[index + 4] = Float.floatToRawIntBits((float) v);
        }

        public void setBrightness(int brightness) {

            buf[index + 7] = brightness;
        }

    }

}
