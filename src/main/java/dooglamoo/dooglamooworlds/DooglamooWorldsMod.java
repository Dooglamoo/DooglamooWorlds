/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dooglamoo.dooglamooworlds.command.DooglamooCommands;
import dooglamoo.dooglamooworlds.dict.DictionaryFactory;
import dooglamoo.dooglamooworlds.dict.GeofeatureManager;
import dooglamoo.dooglamooworlds.world.DooglamooWorldType;
import dooglamoo.dooglamooworlds.world.gen.feature.DooglamooFeatures;

@Mod("dooglamooworlds")
public class DooglamooWorldsMod
{
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static final WorldType DOOGLAMOO = new DooglamooWorldType("dooglamoo");

    public DooglamooWorldsMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);

        MinecraftForge.EVENT_BUS.register(this);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DooglamooConfig.spec);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        DictionaryFactory.init();
    }
    
    private void loadComplete(final FMLLoadCompleteEvent event)
    {
    	DictionaryFactory.loadComplete();
    }
    
    @SubscribeEvent
    public void tagsUpdated(final TagsUpdatedEvent event)
    {
    	DictionaryFactory.tagsUpdated(event);
    }
    
    @SubscribeEvent
    public void onServerAboutToStart(final FMLServerAboutToStartEvent event)
    {
        event.getServer().getResourceManager().addReloadListener(new GeofeatureManager());
    }
    
    @SubscribeEvent
    public void onServerStarting(final FMLServerStartingEvent event)
    {
        event.getCommandDispatcher().register(DooglamooCommands.GEOFACTORS);
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onFeatureRegistry(final RegistryEvent.Register<Feature<?>> featureRegistryEvent)
        {
        	DooglamooFeatures.register(featureRegistryEvent.getRegistry());
        }
    }
}
