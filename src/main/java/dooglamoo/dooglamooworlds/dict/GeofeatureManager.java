/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.dict;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;

import dooglamoo.dooglamooworlds.DooglamooWorldsMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class GeofeatureManager extends JsonReloadListener
{
	public static final Gson GSON = new GsonBuilder().create();
	
	public GeofeatureManager()
	{
		super(GSON, "geofeatures");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonObject> splashList, IResourceManager resourceManager, IProfiler profiler)
	{
//		System.out.println("GeofeatureManager: apply size " + splashList.size());
		
		DictionaryFactory.clear();
		int count = 0;
		for (Entry<ResourceLocation, JsonObject> entry : splashList.entrySet())
		{
	         ResourceLocation resourcelocation = entry.getKey();
	         if (resourcelocation.getPath().startsWith("_")) continue; //Forge: filter anything beginning with "_" as it's used for metadata.

	         try
	         {
	        	 String name = FilenameUtils.removeExtension(resourcelocation.getPath()).replaceAll("\\\\", "/");
	        	 GeoFeature geo = DictionaryFactory.getGeoFeature(name, entry.getValue());
	        	 DictionaryFactory.register(geo);
	        	 count++;
	         }
	         catch (JsonParseException jsonparseexception)
	         {
	        	 DooglamooWorldsMod.LOGGER.error("Parsing error loading geofeature {}", resourcelocation, jsonparseexception);
	         }
         }

		DooglamooWorldsMod.LOGGER.info("Loaded {} geofeatures", count);
		DooglamooWorldsMod.LOGGER.info("{} biomes cover {}% of geofactors", DictionaryFactory.biomeCount, (int)(DictionaryFactory.biomes.size() / 65536.0 * 100.0));
		DooglamooWorldsMod.LOGGER.info("{} blocks cover {}% of geofactors", DictionaryFactory.blockCount, (int)(DictionaryFactory.blocks.size() / 65536.0 * 100.0));
		DooglamooWorldsMod.LOGGER.info("{} features cover {}% of geofactors", DictionaryFactory.featureCount, (int)(DictionaryFactory.features.size() / 65536.0 * 100.0));
	}
}
