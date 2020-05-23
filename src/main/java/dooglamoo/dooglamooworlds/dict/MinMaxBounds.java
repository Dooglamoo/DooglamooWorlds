/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.dict;

import javax.annotation.Nullable;

import net.minecraft.util.JSONUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MinMaxBounds
{
    public static final MinMaxBounds UNBOUNDED = new MinMaxBounds(0, 3);
    private final Integer min;
    private final Integer max;

    public MinMaxBounds(@Nullable Integer min, @Nullable Integer max)
    {
        this.min = min;
        this.max = max;
    }
    
    public int getMin()
    {
    	return min;
    }
    
    public int getMax()
    {
    	return max;
    }

    public static MinMaxBounds deserialize(@Nullable JsonElement element)
    {
        if (element != null && !element.isJsonNull())
        {
            if (JSONUtils.isNumber(element))
            {
                int f2 = JSONUtils.getInt(element, "value");
                if (f2 < 0) f2 = 0;
                else if (f2 > 3) f2 = 3;
                return new MinMaxBounds(f2, f2);
            }
            else
            {
                JsonObject jsonobject = JSONUtils.getJsonObject(element, "value");
                Integer f = jsonobject.has("min") ? JSONUtils.getInt(jsonobject, "min") : 0;
                Integer f1 = jsonobject.has("max") ? JSONUtils.getInt(jsonobject, "max") : 3;
                return new MinMaxBounds(f, f1);
            }
        }
        else
        {
            return UNBOUNDED;
        }
    }
}
