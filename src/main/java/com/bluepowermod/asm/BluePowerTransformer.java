package com.bluepowermod.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BluePowerTransformer implements IClassTransformer
{
    private enum ClassName
    {
        ACHIEVEMENTS("com.bluepowermod.util.Achievements"),
        ENCHANTMENT_HELPER("net.minecraft.enchantment.EnchantmentHelper","afv"),
        ENCHANTMENT_CONTAINER("net.minecraft.inventory.ContainerEnchantment","aag",Method.ENCHANT_ITEM);

        private String name;
        private String obfName;
        private Method[] methods;

        ClassName(String name, Method... methods)
        {
            this(name,name,methods);
        }

        ClassName(String name, String obfName, Method... methods)
        {
            this.name = name;
            this.obfName = obfName;
            this.methods = methods;
        }
        public String getName()
        {
            return FMLForgePlugin.RUNTIME_DEOBF?obfName:name;
        }

        public String getASMName()
        {
            return name.replace(".","/");
        }

        public boolean hasMethods()
        {
            return methods!=null && methods.length>0;
        }
    }

    private enum Method
    {
        ENCHANT_ITEM("enchantItem","func_75140_a","(Lnet/minecraft/entity/player/EntityPlayer;I)Z"){
            public InsnList getInstructions()
            {
                InsnList instructions = new InsnList();
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
                instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.ACHIEVEMENTS.getASMName(), CHECK.getName(), CHECK.args,false));
                return instructions;
            }},
        CHECK("checkForAchievement","(Lnet/minecraft/entity/player/EntityPlayer;Ljava/util/List;)V"),
        BUILD_LIST("buildEnchantmentList","func_77513_b","(Ljava/util/Random;Lnet/minecraft/item/ItemStack;I)Ljava/util/List");

        public String name;
        public String obfName;
        public String args;

        Method(String name, String args)
        {
            this(name,name,args);
        }

        Method(String name, String obfName, String args)
        {
            this.name = name;
            this.obfName = obfName;
            this.args = args;
        }


        public String getName()
        {
            return FMLForgePlugin.RUNTIME_DEOBF?obfName:name;
        }

        public InsnList getInstructions()
        {
            return null;
        }
    }

    private static Map<String,ClassName> classMap = new HashMap<String, ClassName>();

    static
    {
        for (ClassName clazz : ClassName.values())
            if (clazz.hasMethods()) classMap.put(clazz.getName(),clazz);
    }

    @Override
    public byte[] transform(String className, String className2, byte[] bytes)
    {
        ClassName clazz = classMap.get(className);
        if (clazz!=null)
        {
            switch (clazz)
            {
                case ENCHANTMENT_CONTAINER:
                    bytes = insertAchievementCheck(Method.ENCHANT_ITEM,bytes);
                    break;
            }
            classMap.remove(className);
        }
        return bytes;
    }

    private byte[] insertAchievementCheck(Method method, byte[] bytes)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        MethodNode methodNode = getMethodByName(classNode,method.getName(),method.args);

        if (methodNode!=null)
        {
            AbstractInsnNode pos = findAchievementNode(methodNode.instructions);
            if (pos!=null)
                methodNode.instructions.insertBefore(pos,method.getInstructions());
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }


    public static MethodNode getMethodByName(ClassNode classNode, String name, String args) {
        List<MethodNode> methods = classNode.methods;
        for (int k = 0; k < methods.size(); k++) {
            MethodNode method = methods.get(k);
            if (method.name.equals(name) && method.desc.equals(args)) {
                return method;
            }
        }
        return null;
    }

    public static AbstractInsnNode findAchievementNode(InsnList instructions)
    {
        for(Iterator<AbstractInsnNode> itr = instructions.iterator(); itr.hasNext();)
        {
            AbstractInsnNode node = itr.next();
            if (node instanceof VarInsnNode)
            {
                if (node.getOpcode()==Opcodes.ALOAD && ((VarInsnNode) node).var==4) return node;
            }
        }
        return null;
    }

}