/*
 * The MIT License
 *
 * Copyright (c) 2017 Elucent, William Thompson (unascribed), and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package elucent.albedo.lighting;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Light {
    public float x;
    public float y;
    public float z;
    public float r;
    public float g;
    public float b;
    public float a;
    public float radius;

    public Light(float x, float y, float z, float r, float g, float b, float a, float radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.radius = radius;
    }

    public static Light.Builder builder() {
        return new Light.Builder();
    }

    public static final class Builder {
        private float x = 0.0F / 0.0F;
        private float y = 0.0F / 0.0F;
        private float z = 0.0F / 0.0F;
        private float r = 0.0F / 0.0F;
        private float g = 0.0F / 0.0F;
        private float b = 0.0F / 0.0F;
        private float a = 0.0F / 0.0F;
        private float radius = 0.0F / 0.0F;

        public Builder() {
        }

        public Light.Builder pos(BlockPos pos) {
            return this.pos((float)pos.getX() + 0.5F, (float)pos.getY() + 0.5F, (float)pos.getZ() + 0.5F);
        }

        public Light.Builder pos(Vec3d pos) {
            return this.pos(pos.x, pos.y, pos.z);
        }

        public Light.Builder pos(Entity e) {
            return this.pos(e.posX, e.posY, e.posZ);
        }

        public Light.Builder pos(double x, double y, double z) {
            return this.pos((float)x, (float)y, (float)z);
        }

        public Light.Builder pos(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public Light.Builder color(int c, boolean hasAlpha) {
            return this.color(this.extract(c, 2), this.extract(c, 1), this.extract(c, 0), hasAlpha ? this.extract(c, 3) : 1.0F);
        }

        private float extract(int i, int idx) {
            return (float)(i >> idx * 8 & 255) / 255.0F;
        }

        public Light.Builder color(float r, float g, float b) {
            return this.color(r, g, b, 1.0F);
        }

        public Light.Builder color(float r, float g, float b, float a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            return this;
        }

        public Light.Builder radius(float radius) {
            this.radius = radius;
            return this;
        }

        public Light build() {
            if (Float.isFinite(this.x) && Float.isFinite(this.y) && Float.isFinite(this.z) && Float.isFinite(this.r) && Float.isFinite(this.g) && Float.isFinite(this.b) && Float.isFinite(this.a) && Float.isFinite(this.radius)) {
                return new Light(this.x, this.y, this.z, this.r, this.g, this.b, this.a, this.radius);
            } else {
                throw new IllegalArgumentException("Position, color, and radius must be set, and cannot be infinite");
            }
        }
    }
}