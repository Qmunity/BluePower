package com.bluepowermod.util;

import com.bluepowermod.init.BPItems;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * @author K4Unl (Koen Beckers)
 */
public class Achievements {

    public static Achievement vorpalAchievement   = new Achievement("achievements.vorpal", "vorpal", 0, 0, BPItems.athame, (Achievement)null);
    public static Achievement tungstenAchievement = new Achievement("achievements.tungsten", "tungsten", -1, 1, BPItems.tungsten_ingot, (Achievement)null);
    public static Achievement dopeAchievement     = new Achievement("achievements.dope", "dope", 1, 1, BPItems.blue_doped_wafer, (Achievement)null);

    private static AchievementPage achievementPage = new AchievementPage("bluepowerAchievements", vorpalAchievement, tungstenAchievement, dopeAchievement);

    public static void init() {

        AchievementPage.registerAchievementPage(achievementPage);
        vorpalAchievement.initIndependentStat().registerStat();
        tungstenAchievement.initIndependentStat().registerStat();
        dopeAchievement.initIndependentStat().registerStat();
    }
}
