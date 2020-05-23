/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import dooglamoo.dooglamooworlds.world.biome.provider.DooglamooBiomeProvider;
import dooglamoo.dooglamooworlds.world.gen.DooglamooChunkGenerator;
import com.mojang.brigadier.builder.*;

public class DooglamooCommands
{
	public static final LiteralArgumentBuilder<CommandSource> GEOFACTORS = Commands.literal("geofactors").requires(source -> source.hasPermissionLevel(2))
			.executes(context -> {
				Vec3d pos = context.getSource().getPos();
				World world = context.getSource().getWorld();
				if (world.getChunkProvider() instanceof ServerChunkProvider && ((ServerChunkProvider)world.getChunkProvider()).getChunkGenerator() instanceof DooglamooChunkGenerator)
				{
					DooglamooChunkGenerator chunkGenerator = (DooglamooChunkGenerator)((ServerChunkProvider)world.getChunkProvider()).getChunkGenerator();
					DooglamooBiomeProvider biomeProvider = chunkGenerator.biomeProvider;
					int x1 = (int)pos.getX();
					int z1 = (int)pos.getZ();
					double elevation = biomeProvider.elevationGen.noise(x1, z1) * biomeProvider.elevationAmplitude + biomeProvider.elevationWeight;
					double density = biomeProvider.densityGen.noise(x1, z1) * biomeProvider.densityAmplitude + biomeProvider.densityWeight;
					double uplift = biomeProvider.upliftGen.noise(x1, z1) * biomeProvider.upliftAmplitude + biomeProvider.upliftWeight;
					double volcanism = biomeProvider.volcanismGen.noise(x1, z1) * biomeProvider.volcanismAmplitude + biomeProvider.volcanismWeight;
					double era = biomeProvider.eraGen.noise(x1, z1) * biomeProvider.eraAmplitude + biomeProvider.eraWeight;
					double erosion = biomeProvider.erosionGen.noise(x1, z1) * biomeProvider.erosionAmplitude + biomeProvider.erosionWeight;
					double temperature = biomeProvider.temperatureGen.noise(x1, z1) * biomeProvider.temperatureAmplitude + MathHelper.clamp(biomeProvider.temperatureWeight + z1 * biomeProvider.zClimateFactor, -0.6, 0.6);
					double precipitation = biomeProvider.precipitationGen.noise(x1, z1) * biomeProvider.precipitationAmplitude + MathHelper.clamp(biomeProvider.precipitationWeight + x1 * biomeProvider.xClimateFactor, -0.6, 0.6);
					String geofactorsStr =
							"Density: " + density + "\n"
							+ "Elevation: " + elevation + "\n"
							+ "Era: " + era + "\n"
							+ "Volcanism: " + volcanism + "\n"
							+ "Erosion: " + erosion + "\n"
							+ "Uplift: " + uplift + "\n"
							+ "Temperature: " + temperature + "\n"
							+ "Precipitation: " + precipitation;
					context.getSource().sendFeedback(new StringTextComponent(geofactorsStr), true);
					return 1;
				}
				return 0;
			});
}
