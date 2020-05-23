/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world;

import dooglamoo.dooglamooworlds.client.gui.screen.CreateDooglamooWorldScreen;
import dooglamoo.dooglamooworlds.world.biome.provider.DooglamooBiomeProvider;
import dooglamoo.dooglamooworlds.world.biome.provider.DooglamooBiomeProviderSettings;
import dooglamoo.dooglamooworlds.world.gen.DooglamooChunkGenerator;
import dooglamoo.dooglamooworlds.world.gen.DooglamooGenSettings;
import com.mojang.datafixers.Dynamic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DooglamooWorldType extends WorldType
{
	public DooglamooWorldType(String name)
	{
		super(name);
		this.setCustomOptions(true);
	}
	
	@Override
    public ChunkGenerator<?> createChunkGenerator(World world)
    {
		if (world.dimension.isSurfaceWorld())
		{
			CompoundNBT nbt = world.getWorldInfo().getGeneratorOptions();
			DooglamooGenSettings gensettings = DooglamooGenSettings.createDooglamooGenerator(new Dynamic<>(NBTDynamicOps.INSTANCE, nbt));
			DooglamooBiomeProviderSettings biomesettings = new DooglamooBiomeProviderSettings().setWorldInfo(world.getWorldInfo()).setGeneratorSettings(gensettings);
			DooglamooBiomeProvider biomeprovider = new DooglamooBiomeProvider(biomesettings);
			return new DooglamooChunkGenerator(world, biomeprovider, gensettings);
		}
		else
		{
			return super.createChunkGenerator(world);
		}
    }
	
	@Override
	public float getCloudHeight()
    {
		return 192.0f;
    }
	
	@Override
	@OnlyIn(Dist.CLIENT)
    public void onCustomizeButton(Minecraft mc, CreateWorldScreen gui)
    {
		mc.displayGuiScreen(new CreateDooglamooWorldScreen(gui, gui.chunkProviderSettingsJson));
    }
}
