package com.bluepowermod.client.render;

public class RenderDebugScreen {

//    private final DecimalFormat df = new DecimalFormat("#####.###");
//
//    @SubscribeEvent
//    public void onRenderDebugScreen(RenderGameOverlayEvent event) {
//
//        if (event.type == RenderGameOverlayEvent.ElementType.DEBUG) {
//            Minecraft mc = Minecraft.getMinecraft();
//            FontRenderer fr = mc.fontRenderer;
//            MinecraftServer sv = MinecraftServer.getServer();
//            boolean isLocal = sv instanceof IntegratedServer;
//
//            List<String> text = new ArrayList<String>();
//
//            // Title
//            {
//                fr.drawStringWithShadow("BluePower info:", event.resolution.getScaledWidth() - 165, 75, 0xFFFFFF44);
//            }
//            // TPS
//            if (sv != null) {
//                double ms = MathHelper.mean(sv.worldTickTimes.get(mc.theWorld.provider.dimensionId)) * 1.0E-6D;
//                double tps = Math.min(1000.0 / ms, 20);
//                text.add("TPS: " + df.format(tps) + " (" + df.format(ms) + "ms)");
//            } else {
//                text.add("Server TPS coming soon!");
//            }
//
//            // Render
//            int i = 0;
//            for (String s : text) {
//                fr.drawStringWithShadow(s, event.resolution.getScaledWidth() - 160, 90 + 10 * i, 0xFFFFFF77);
//                i++;
//            }
//        }
//    }
}
