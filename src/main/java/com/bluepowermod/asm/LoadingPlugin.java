package com.bluepowermod.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.classloading.FMLForgePlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"com.bluepowermod.asm."})
public class LoadingPlugin implements IFMLLoadingPlugin
{
    public static boolean runtimeDeobfEnabled = FMLForgePlugin.RUNTIME_DEOBF;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{getAccessTransformerClass()};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
    }

    @Override
    public String getAccessTransformerClass()
    {
        return "com.bluepowermod.asm.BluePowerTransformer";
    }
}
