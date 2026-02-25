/*
MIT License

Copyright (c) 2026 aisi163

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.aisi163.npc_builder.NPCEntity;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public enum NPCAffairsType {
    NONE_AFFAIR(
            "none_affair",
            "无业务"),

    POSITION_CHANGE_AFFAIR(
            "position_change_affair",
            "坐标跳转业务",
            "position_change_x","position_change_y","position_change_z"),

    DIMENSION_CHANGE_AFFAIR(
            "dimension_change_affair",
            "维度跳转",
            "dimension_code","dimension_change_x","dimension_change_y","dimension_change_z"
    ),

    COMMAND_EXECUTE_AFFAIR(
            "command_execute",
            "命令执行",
            "command_str"
    );

    public static final Map<String, NPCAffairsType> npc_affairs = new HashMap<>();

    static {
       npc_affairs.put("none_affair", NONE_AFFAIR);
       npc_affairs.put("position_change_affair", POSITION_CHANGE_AFFAIR);
       npc_affairs.put("dimension_change_affair", DIMENSION_CHANGE_AFFAIR);
       npc_affairs.put("command_execute", COMMAND_EXECUTE_AFFAIR);
    }

    private final String affair_type;
    private final String description;
    private final String[] with_argument;

    NPCAffairsType(String affairs_type, String description, String...  with_argument) {
        this.affair_type = affairs_type;
        this.description = description;
        this.with_argument = with_argument;
    }

    public String getAffair_type() {
        return affair_type;
    }

    public String getAffair_description() {
        return description;
    }

    public NPCAffairsType change_to(String affairs_type) {
        return npc_affairs.get(affairs_type);
    };

    public String check_string_argument_map(Map<String, String> argument_map){
        for (String argument_key: with_argument) {
            if (!argument_map.containsKey(argument_key)) {
                return argument_key;
            }
        }
        return "";
    }

    public String check_double_argument_map(Map<String, Double> argument_map){
        for (String argument_key: with_argument) {
            if (!argument_map.containsKey(argument_key)) {
                return argument_key;
            }
        }
        return "";
    }

    public void push_NBT_to_affair_argument_double_map(Map<String, Double> argument_map, NBTTagCompound nbt_tag){
       for (String argument_key: with_argument){
            if (nbt_tag.hasKey(argument_key)) {
                Double argument_value = nbt_tag.getDouble(argument_key);
                argument_map.put(argument_key, argument_value);
            }
       }
    }

    public void push_NBT_to_affair_argument_string_map(Map<String, String> argument_map, NBTTagCompound nbt_tag){
        for (String argument_key: with_argument){
            if (nbt_tag.hasKey(argument_key)) {
                String argument_value = nbt_tag.getString(argument_key);
                argument_map.put(argument_key, argument_value);
            }
        }
    }

    public void push_affair_double_argument_map_to_NBT(Map<String, Double> argument_map, NBTTagCompound nbt_tag){
        for (String argument_key: with_argument){
            if (argument_map.containsKey(argument_key)) {
               nbt_tag.setDouble(argument_key, argument_map.get(argument_key));
            }
        }
    }


    public void push_affair_string_argument_map_to_NBT(Map<String, String> argument_map, NBTTagCompound nbt_tag){
        for (String argument_key: with_argument){
            if (argument_map.containsKey(argument_key)) {
                nbt_tag.setString(argument_key, argument_map.get(argument_key));
            }
        }
    }

    public String get_affair_string_info(Map<String,String> argument_map) {
        StringBuilder affair_info = new StringBuilder();

        Set<String> keys = argument_map.keySet();
        boolean is_affair_broken = false;

        affair_info.append("业务类型(").append(this.affair_type).append(")\n");
        for (String key: keys) {
            if (!argument_map.containsKey(key)) {
                is_affair_broken = true;
                affair_info.append("§c业务数据损坏, 未检测到 ").append(argument_map).append("\n");
            }
            else {
                affair_info.append("业务参数: ").append(key).append(" -> 值: ").append(argument_map.get(key)).append("\n");
            }
        };

        if (is_affair_broken){
            affair_info.append("抱歉,我们无法为您提供此NPC的完整业务数据, 原因: 业务数据损坏");
        }
        return affair_info.toString();
    };

    public String get_affair_double_info(Map<String,Double> argument_map) {
        StringBuilder affair_info = new StringBuilder();

        Set<String> keys = argument_map.keySet();
        boolean is_affair_broken = false;

        affair_info.append("业务类型(").append(this.affair_type).append(")\n");
        for (String key: keys) {
            if (!argument_map.containsKey(key)) {
                is_affair_broken = true;
                affair_info.append("§c业务数据损坏, 未检测到 ").append(argument_map).append("\n");
            }
            else {
                affair_info.append("业务参数: ").append(key).append(" -> 值: ").append(argument_map.get(key)).append("\n");
            }
        };

        if (is_affair_broken){
            affair_info.append("抱歉,我们无法为您提供此NPC的完整业务数据, 原因: 业务数据损坏");
        }
        return affair_info.toString();
    };

    public Double get_affair_double_argument_value(Map<String, Double> argument_map, String key){
        return argument_map.getOrDefault(key, 0.0D);
    };

    public String get_affair_string_argument_value(Map<String,String> argument_map, String key){
        return argument_map.getOrDefault(key, "");
    };

    public boolean same_affair(NPCAffairsType other) {
        return other.getAffair_type().equals(this.affair_type);
    }
}

