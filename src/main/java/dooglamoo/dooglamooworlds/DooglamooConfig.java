/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds;

import net.minecraftforge.common.ForgeConfigSpec;

public class DooglamooConfig
{
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    public static class General
    {
        public final ForgeConfigSpec.ConfigValue<Boolean> customGenOres;
        public final ForgeConfigSpec.ConfigValue<Boolean> standardGenOres;
        public final ForgeConfigSpec.ConfigValue<Boolean> customGenSlimeOre;
        public final ForgeConfigSpec.ConfigValue<Boolean> customGenGlowstoneOre;
        public final ForgeConfigSpec.ConfigValue<Boolean> customGenQuartzOre;
        public final ForgeConfigSpec.ConfigValue<Boolean> customGenHellBiome;
        public final ForgeConfigSpec.ConfigValue<Boolean> swapNorthSouthTemperature;
        public final ForgeConfigSpec.ConfigValue<Boolean> swapEastWestPrecipitation;

        public General(ForgeConfigSpec.Builder builder)
        {
            builder.push("General");
            
            customGenOres = builder
                    .comment("If true, custom generates localized small to large ore clusters.")
                    .translation("config.property.custom_generate_ores")
                    .define("customGenOres", true);
            standardGenOres = builder
                    .comment("If true, generates standard vanilla Minecraft ore distribution.")
                    .translation("config.property.standard_generate_ores")
                    .define("standardGenOres", false);
            customGenSlimeOre = builder
                    .comment("If false, no vanilla slime blocks will be generated.")
                    .translation("config.property.custom_generate_slime_ore")
                    .define("customGenSlimeOre", false);
            customGenGlowstoneOre = builder
                    .comment("If false, no vanilla glowstone blocks will be generated in overworld.")
                    .translation("config.property.custom_generate_glowstone_ore")
                    .define("customGenGlowstoneOre", false);
            customGenQuartzOre = builder
                    .comment("If false, no vanilla nether quartz blocks will be generated in overworld.")
                    .translation("config.property.custom_generate_quartz_ore")
                    .define("customGenQuartzOre", false);
            customGenHellBiome = builder
                    .comment("If true, Nether mobs will generate around volcanic areas in the overworld.")
                    .translation("config.property.custom_generate_hell_biome")
                    .define("customGenHellBiome", false);
            swapNorthSouthTemperature = builder
                    .comment("If true, it gradually gets colder going south and hotter going north.")
                    .translation("config.property.swap_north_south_temperature")
                    .define("swapNorthSouthTemperature", false);
            swapEastWestPrecipitation = builder
                    .comment("If true, it gradually gets dryer going east and wetter going west.")
                    .translation("config.property.swap_east_west_precipitation")
                    .define("swapEastWestPrecipitation", false);
            
            builder.pop();
        }
    }
}
