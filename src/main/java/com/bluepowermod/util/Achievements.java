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

    public static Achievement vorpalAchievement         = new Achievement("achievements.vorpal", "vorpal", -2, 0, BPItems.athame, (Achievement) null);
    public static Achievement sortAchievement           = new Achievement("achievements.sort", "sort", 2, 0, BPBlocks.sorting_machine,
            (Achievement) null);
    public static Achievement tungstenAchievement       = new Achievement("achievements.tungsten", "tungsten", -2, 2, BPBlocks.tungsten_ore,
            (Achievement) null);
    public static Achievement dopeAchievement           = new Achievement("achievements.dope", "dope", 2, 2, BPItems.blue_doped_wafer,
            (Achievement) null);
    public static Achievement tubeAchievement           = new Achievement("achievements.tube", "tube", 0, 0,
            PartManager.getPartInfo("pneumaticTube").getItem(), (Achievement) null);
    public static Achievement magTubeAchievement        = new Achievement("achievements.magtube", "magtube", -2, -2,
            PartManager.getPartInfo("magTube").getItem(), (Achievement) null);
    public static Achievement circuitCeptionAchievement = new Achievement("achievements.circuitception", "circuitception", 0, -2,
            PartManager.getPartInfo("integratedCircuit3x3").getItem(), (Achievement) null);

    private static AchievementPage achievementPage = new AchievementPage("BluePower", vorpalAchievement, tungstenAchievement,
            dopeAchievement, sortAchievement, tubeAchievement, magTubeAchievement, circuitCeptionAchievement);

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
