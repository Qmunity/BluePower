package net.quetzi.bluepower.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.IBPRedstonePart;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.references.Refs;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.TMultiPart;

public class MultipartBPPart extends TMultiPart implements IRedstonePart, JNormalOcclusion {
    
    private BPPart part;
    
    public MultipartBPPart(BPPart part) {
    
        setPart(part);
    }
    
    public MultipartBPPart() {
    
    }
    
    protected BPPart getPart() {
    
        return this.part;
    }
    
    protected void setPart(BPPart part) {
    
        this.part = part;
    }
    
    @Override
    public String getType() {
    
        return Refs.MODID + "_" + getPart().getType();
    }
    
    @Override
    public Iterable<IndexedCuboid6> getSubParts() {
    
        List<IndexedCuboid6> cubes = new ArrayList<IndexedCuboid6>();
        List<AxisAlignedBB> aabbs = getPart().getSelectionBoxes();
        if (aabbs == null) return cubes;
        for (AxisAlignedBB aabb : aabbs)
            cubes.add(new IndexedCuboid6(0, new Cuboid6(aabb)));
        return cubes;
    }
    
    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {
    
        List<Cuboid6> cubes = new ArrayList<Cuboid6>();
        List<AxisAlignedBB> aabbs = getPart().getCollisionBoxes();
        if (aabbs == null) return cubes;
        for (AxisAlignedBB aabb : aabbs)
            cubes.add(new Cuboid6(aabb));
        return cubes;
    }
    
    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {
    
        List<Cuboid6> cubes = new ArrayList<Cuboid6>();
        List<AxisAlignedBB> aabbs = getPart().getOcclusionBoxes();
        if (aabbs == null) return cubes;
        for (AxisAlignedBB aabb : aabbs)
            cubes.add(new Cuboid6(aabb));
        return cubes;
    }
    
    @Override
    public void writeDesc(MCDataOutput packet) {
    
        super.writeDesc(packet);
        
        packet.writeString(getPart().getType());
    }
    
    @Override
    public void readDesc(MCDataInput packet) {
    
        super.readDesc(packet);
        
        String type = packet.readString();
        if (getPart() == null) setPart(PartRegistry.createPart(type));
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        String type = tag.getString("id");
        if (getPart() == null) setPart(PartRegistry.createPart(type));
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setString("id", getPart().getType());
    }
    
    @Override
    public int getLightValue() {
    
        return getPart().getLightValue();
    }
    
    @Override
    public Iterable<ItemStack> getDrops() {
    
        return getPart().getDrops();
    }
    
    @Override
    public ItemStack pickItem(MovingObjectPosition hit) {
    
        return getPart().getPickedItem(hit);
    }
    
    @Override
    public float getStrength(MovingObjectPosition hit, EntityPlayer player) {
    
        float s = 50;
        float h = getPart().getHardness(hit, player) * (s / 2F);
        if (h == 0) return s;
        
        return s / h;
    }
    
    // Redstone
    
    @Override
    public int strongPowerLevel(int side) {
    
        if (getPart() instanceof IBPRedstonePart) return ((IBPRedstonePart) getPart()).getStrongOutput(ForgeDirection.getOrientation(side));
        
        return 0;
    }
    
    @Override
    public int weakPowerLevel(int side) {
    
        if (getPart() instanceof IBPRedstonePart) return ((IBPRedstonePart) getPart()).getWeakOutput(ForgeDirection.getOrientation(side));
        
        return 0;
    }
    
    @Override
    public boolean canConnectRedstone(int side) {
    
        if (getPart() instanceof IBPRedstonePart) return ((IBPRedstonePart) getPart()).canConnect(ForgeDirection.getOrientation(side));
        
        return false;
    }
    
    // Events
    
    @Override
    public void onAdded() {
    
        getPart().onAdded();
    }
    
    @Override
    public void onRemoved() {
    
        getPart().onRemoved();
    }
    
    @Override
    public void onPartChanged(TMultiPart part) {
    
        getPart().onPartChanged();
    }
    
    @Override
    public void onEntityCollision(Entity entity) {
    
        getPart().onEntityCollision(entity);
    }
    
    @Override
    public void onNeighborChanged() {
    
        getPart().onNeigbourUpdate();
    }
    
    @Override
    public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack item) {
    
        return getPart().onActivated(player, hit, item);
    }
    
    // Rendering
    
    @Override
    public void renderDynamic(Vector3 pos, float frame, int pass) {
    
        getPart().renderDynamic(new net.quetzi.bluepower.api.vec.Vector3(pos.x, pos.y, pos.z), pass, frame);
    }
    
    @Override
    public boolean renderStatic(Vector3 pos, int pass) {
    
        return getPart().renderStatic(new net.quetzi.bluepower.api.vec.Vector3(pos.x, pos.y, pos.z), pass);
    }
    
    @Override
    public void update() {
    
        getPart().world = world();
        getPart().x = x();
        getPart().y = y();
        getPart().z = z();
        
        getPart().update();
    }
    
}
