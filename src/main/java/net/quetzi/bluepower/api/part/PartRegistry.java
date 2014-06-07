package net.quetzi.bluepower.api.part;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.compat.fmp.MultipartBPPart;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.part.gate.GateBase;
import net.quetzi.bluepower.references.Refs;

import java.util.*;

public class PartRegistry
{

    private static Map<String, Class<? extends BPPart>> parts = new HashMap<String, Class<? extends BPPart>>();

    private PartRegistry()
    {

    }

    public static void registerPart(String id, Class<? extends BPPart> part)
    {

        if (id == null || id.trim().isEmpty()) return;
        if (part == null) return;
        if (parts.containsKey(part)) return;

        parts.put(id, part);
    }

    public static BPPart createPart(String id, boolean isMultipart)
    {

        try {
            for (String s : parts.keySet()) {
                if (((isMultipart ? Refs.MODID + "_" + s : s)).equals(id)) {
                    try {
                        BPPart p = parts.get(s).newInstance();
                        System.out.println(p + " <-> " + p.getType());
                        MultipartBPPart p2 = new MultipartBPPart(p);
                        System.out.println(p2 + " --> " + p2.getType());
                        return p;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BPPart createPart(String id)
    {

        return createPart(id, false);
    }

    public static Map<String, Class<? extends BPPart>> getMappings()
    {

        return Collections.unmodifiableMap(parts);
    }

    public static List<String> getRegisteredParts()
    {

        return Collections.unmodifiableList(new ArrayList<String>(parts.keySet()));
    }

    public static ItemStack getItemForPart(String id)
    {

        if (parts.containsKey(id)) {
            ItemStack is = new ItemStack(BPItems.multipart);

            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("id", id);

            is.setTagCompound(tag);

            return is;
        }

        return null;
    }

    public static BPPart createPartFromItem(ItemStack is)
    {

        NBTTagCompound tag = is.getTagCompound();
        String id = tag.getString("id");
        return createPart(id);
    }

    public static void init()
    {

        registerPart("gatebase", GateBase.class);
    }

}
