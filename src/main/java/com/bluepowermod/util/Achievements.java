package com.bluepowermod.util;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.PartManager;

/**
 * @author K4Unl (Koen Beckers)
 */
public class Achievements {

    public static Achievement vorpalAchievement = new Achievement("achievement.bluepower:vorpal", "bluepower:vorpal", -2, 0, BPItems.athame,
            (Achievement) null);
    public static Achievement sortAchievement = new Achievement("achievement.bluepower:sort", "bluepower:sort", 2, 0, BPBlocks.sorting_machine,
            (Achievement) null);
    public static Achievement tungstenAchievement = new Achievement("achievement.bluepower:tungsten", "bluepower:tungsten", -2, 2,
            BPBlocks.tungsten_ore, (Achievement) null);
    public static Achievement dopeAchievement = new Achievement("achievement.bluepower:dope", "bluepower:dope", 2, 2, BPItems.blue_doped_wafer,
            (Achievement) null);
    public static Achievement tubeAchievement = new Achievement("achievement.bluepower:tube", "bluepower:tube", 0, 0, PartManager.getPartInfo(
            "pneumaticTube"), (Achievement) null);
    public static Achievement magTubeAchievement = new Achievement("achievement.bluepower:magtube", "bluepower:magtube", -2, -2, PartManager
            .getPartInfo("magTube").getItem(), (Achievement) null);
    public static Achievement circuitCeptionAchievement = new Achievement("achievement.bluepower:circuitception", "bluepower:circuitception", 0, -2,
            PartManager.getPartInfo("integratedCircuit3x3").getItem(), (Achievement) null);

    private static AchievementPage achievementPage = new AchievementPage("BluePower", vorpalAchievement, tungstenAchievement, dopeAchievement,
            sortAchievement, tubeAchievement, magTubeAchievement, circuitCeptionAchievement);

    public static void init() {

        AchievementPage.registerAchievementPage(achievementPage);
        vorpalAchievement.initIndependentStat().registerStat();
        tungstenAchievement.initIndependentStat().registerStat();
        dopeAchievement.initIndependentStat().registerStat();
        sortAchievement.initIndependentStat().registerStat();
        tubeAchievement.initIndependentStat().registerStat();
        magTubeAchievement.initIndependentStat().registerStat();
        circuitCeptionAchievement.initIndependentStat().registerStat();
    }
}
