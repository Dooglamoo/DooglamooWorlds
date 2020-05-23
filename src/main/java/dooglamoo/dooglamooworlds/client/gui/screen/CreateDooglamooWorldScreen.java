/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.client.gui.screen;

import dooglamoo.dooglamooworlds.world.gen.DooglamooGenSettings;
import com.mojang.datafixers.Dynamic;

import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.AbstractOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreateDooglamooWorldScreen extends Screen
{
	private final CreateWorldScreen parent;
	private DooglamooGenSettings generatorInfo = DooglamooGenSettings.getDefaultDooglamooGenerator();
	private OptionsRowList optionsRowList;

	public CreateDooglamooWorldScreen(CreateWorldScreen parent, CompoundNBT generatorOptions)
	{
		super(new TranslationTextComponent("createWorld.customize.dooglamoo.title"));
		this.parent = parent;
	    this.setGeneratorOptions(generatorOptions);
	}
	
	public CompoundNBT getGeneratorOptions()
	{
		return (CompoundNBT) this.generatorInfo.buildSettings(NBTDynamicOps.INSTANCE).getValue();
	}

	public void setGeneratorOptions(CompoundNBT nbt)
	{
		this.generatorInfo = DooglamooGenSettings.createDooglamooGenerator(new Dynamic<>(NBTDynamicOps.INSTANCE, nbt));
	}
	
	@Override
	protected void init()
	{
		SliderPercentageOption elevationScaleOption = new SliderPercentageOption(
				"config.property.elevation_scale", 1.0, 3.0, 0.01f, (gamesettings) -> {
					return generatorInfo.elevationScale;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.elevationScale = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption densityScaleOption = new SliderPercentageOption(
				"config.property.density_scale", 1.0, 3.0, 0.01f, (gamesettings) -> {
					return generatorInfo.densityScale;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.densityScale = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption upliftScaleOption = new SliderPercentageOption(
				"config.property.uplift_scale", 1.0, 3.0, 0.01f, (gamesettings) -> {
					return generatorInfo.upliftScale;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.upliftScale = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption volcanismScaleOption = new SliderPercentageOption(
				"config.property.volcanism_scale", 1.0, 3.0, 0.01f, (gamesettings) -> {
					return generatorInfo.volcanismScale;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.volcanismScale = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption eraScaleOption = new SliderPercentageOption(
				"config.property.era_scale", 1.0, 3.0, 0.01f, (gamesettings) -> {
					return generatorInfo.eraScale;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.eraScale = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption erosionScaleOption = new SliderPercentageOption(
				"config.property.erosion_scale", 1.0, 3.0, 0.01f, (gamesettings) -> {
					return generatorInfo.erosionScale;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.erosionScale = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption temperatureScaleOption = new SliderPercentageOption(
				"config.property.temperature_scale", 1.0, 3.0, 0.01f, (gamesettings) -> {
					return generatorInfo.temperatureScale;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.temperatureScale = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption precipitationScaleOption = new SliderPercentageOption(
				"config.property.precipitation_scale", 1.0, 3.0, 0.01f, (gamesettings) -> {
					return generatorInfo.precipitationScale;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.precipitationScale = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		
		SliderPercentageOption elevationAmplitudeOption = new SliderPercentageOption(
				"config.property.elevation_amplitude", 0.0, 2.0, 0.01f, (gamesettings) -> {
					return generatorInfo.elevationAmplitude;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.elevationAmplitude = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption densityAmplitudeOption = new SliderPercentageOption(
				"config.property.density_amplitude", 0.0, 2.0, 0.01f, (gamesettings) -> {
					return generatorInfo.densityAmplitude;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.densityAmplitude = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption upliftAmplitudeOption = new SliderPercentageOption(
				"config.property.uplift_amplitude", 0.0, 2.0, 0.01f, (gamesettings) -> {
					return generatorInfo.upliftAmplitude;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.upliftAmplitude = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption volcanismAmplitudeOption = new SliderPercentageOption(
				"config.property.volcanism_amplitude", 0.0, 2.0, 0.01f, (gamesettings) -> {
					return generatorInfo.volcanismAmplitude;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.volcanismAmplitude = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption eraAmplitudeOption = new SliderPercentageOption(
				"config.property.era_amplitude", 0.0, 2.0, 0.01f, (gamesettings) -> {
					return generatorInfo.eraAmplitude;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.eraAmplitude = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption erosionAmplitudeOption = new SliderPercentageOption(
				"config.property.erosion_amplitude", 0.0, 2.0, 0.01f, (gamesettings) -> {
					return generatorInfo.erosionAmplitude;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.erosionAmplitude = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption temperatureAmplitudeOption = new SliderPercentageOption(
				"config.property.temperature_amplitude", 0.0, 2.0, 0.01f, (gamesettings) -> {
					return generatorInfo.temperatureAmplitude;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.temperatureAmplitude = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption precipitationAmplitudeOption = new SliderPercentageOption(
				"config.property.precipitation_amplitude", 0.0, 2.0, 0.01f, (gamesettings) -> {
					return generatorInfo.precipitationAmplitude;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.precipitationAmplitude = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		
		SliderPercentageOption elevationWeightOption = new SliderPercentageOption(
				"config.property.elevation_weight", -0.6, 0.6, 0.01f, (gamesettings) -> {
					return generatorInfo.elevationWeight;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.elevationWeight = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption densityWeightOption = new SliderPercentageOption(
				"config.property.density_weight", -0.6, 0.6, 0.01f, (gamesettings) -> {
					return generatorInfo.densityWeight;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.densityWeight = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption upliftWeightOption = new SliderPercentageOption(
				"config.property.uplift_weight", -0.6, 0.6, 0.01f, (gamesettings) -> {
					return generatorInfo.upliftWeight;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.upliftWeight = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption volcanismWeightOption = new SliderPercentageOption(
				"config.property.volcanism_weight", -0.6, 0.6, 0.01f, (gamesettings) -> {
					return generatorInfo.volcanismWeight;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.volcanismWeight = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption eraWeightOption = new SliderPercentageOption(
				"config.property.era_weight", -0.6, 0.6, 0.01f, (gamesettings) -> {
					return generatorInfo.eraWeight;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.eraWeight = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption erosionWeightOption = new SliderPercentageOption(
				"config.property.erosion_weight", -0.6, 0.6, 0.01f, (gamesettings) -> {
					return generatorInfo.erosionWeight;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.erosionWeight = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption temperatureWeightOption = new SliderPercentageOption(
				"config.property.temperature_weight", -0.6, 0.6, 0.01f, (gamesettings) -> {
					return generatorInfo.temperatureWeight;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.temperatureWeight = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		SliderPercentageOption precipitationWeightOption = new SliderPercentageOption(
				"config.property.precipitation_weight", -0.6, 0.6, 0.01f, (gamesettings) -> {
					return generatorInfo.precipitationWeight;
			      }, (gamesettings, value) -> {
			    	  generatorInfo.precipitationWeight = value;
			      }, (gamesettings, option) -> {
			    	  return option.getDisplayString() + ((int)(option.get(gamesettings) * 100.0) / 100.0);
			      });
		
		AbstractOption[] options = new AbstractOption[]{
				elevationScaleOption,
				densityScaleOption,
				upliftScaleOption,
				volcanismScaleOption,
				eraScaleOption,
				erosionScaleOption,
				temperatureScaleOption,
				precipitationScaleOption,
				elevationAmplitudeOption,
				densityAmplitudeOption,
				upliftAmplitudeOption,
				volcanismAmplitudeOption,
				eraAmplitudeOption,
				erosionAmplitudeOption,
				temperatureAmplitudeOption,
				precipitationAmplitudeOption,
				elevationWeightOption,
				densityWeightOption,
				upliftWeightOption,
				volcanismWeightOption,
				eraWeightOption,
				erosionWeightOption,
				temperatureWeightOption,
				precipitationWeightOption
			};
		
		optionsRowList = new OptionsRowList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		optionsRowList.addOptions(options);
		this.children.add(optionsRowList);
		
		this.addButton(
				new Button(this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done"), (p_213010_1_) -> {
					this.parent.chunkProviderSettingsJson = this.getGeneratorOptions();
					this.minecraft.displayGuiScreen(this.parent);
				}));
		this.addButton(
				new Button(this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel"), (p_213009_1_) -> {
					this.minecraft.displayGuiScreen(this.parent);
				}));
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_)
	{
		this.renderBackground();
		optionsRowList.render(p_render_1_, p_render_2_, p_render_3_);
		this.drawCenteredString(this.font, this.title.getFormattedText(), this.width / 2, 8, 16777215);
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
}
