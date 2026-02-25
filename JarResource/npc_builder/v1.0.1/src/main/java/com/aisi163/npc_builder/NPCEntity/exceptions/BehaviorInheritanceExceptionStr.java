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

package com.aisi163.npc_builder.NPCEntity.exceptions;

import java.util.ArrayList;
import java.util.List;

class BehaviorInheritanceExceptionStr {

    public static List<String> exception_str_mapping = new ArrayList<>();
    public static String unknown_exception_str = "BehaviorInheritanceException: Unknown exception info";

    public static List<String> user_friendly_exception_str_mapping = new ArrayList<>();
    public static String unknown_user_exception_str = "发生未知错误，请联系管理员！";

    static {
        exception_str_mapping.add("BehaviorInheritanceException: Are you sure %s is right register format in Minecraft ???");
        exception_str_mapping.add("BehaviorInheritanceException: Unknown entity register name %s in Minecraft");
        exception_str_mapping.add("BehaviorInheritanceException: Entity can't inherit behavior from %s");
        exception_str_mapping.add("BehaviorInheritanceException: Entity already had inherited behavior from other entity");
        exception_str_mapping.add("BehaviorInheritanceException: Are you sure %s is a inheritable entity in Minecraft ???");
        exception_str_mapping.add("BehaviorInheritanceException: Entity can't inherit behavior from %s because returning null after invoking NCPEntity.BuildHostingEntity(...)");


        user_friendly_exception_str_mapping.add("实体注册名%s格式错误！正确格式应为「minecraft:实体名」（例如：minecraft:zombie）");
        user_friendly_exception_str_mapping.add("未找到名为%s的实体！请检查实体名是否正确");
        user_friendly_exception_str_mapping.add("无法从%s继承行为！该实体不支持行为托管（仅支持怪物实体如僵尸/骷髅等）");
        user_friendly_exception_str_mapping.add("该NPC已继承其他实体的行为！请先清除原有继承关系再重新设置");
        user_friendly_exception_str_mapping.add("%s不是合法的MC实体！仅支持继承游戏内可召唤的实体（如怪物/生物）");
        user_friendly_exception_str_mapping.add("创建%s的托管实体失败！请检查实体是否支持托管，或联系模组作者排查");
    }

    public static String to_str(BehaviorInheritanceExceptionEnum exception_, String register_entity_name) {
        int exception_code = exception_.ordinal();
        if (exception_code < exception_str_mapping.size()) {
            if (exception_code != BehaviorInheritanceExceptionEnum.ALREADY_INHERITED_WITH_OTHER.ordinal()) {
                return String.format(exception_str_mapping.get(exception_code), register_entity_name);
            } else {
                return exception_str_mapping.get(exception_code);
            }
        } else {
            return unknown_exception_str;
        }
    }

    public static String toUserStr(BehaviorInheritanceExceptionEnum exception_, String register_entity_name) {
        int exception_code = exception_.ordinal();
        if (exception_code < user_friendly_exception_str_mapping.size()) {
            if (exception_code == BehaviorInheritanceExceptionEnum.ALREADY_INHERITED_WITH_OTHER.ordinal()) {
                return user_friendly_exception_str_mapping.get(exception_code);
            }
            return String.format(user_friendly_exception_str_mapping.get(exception_code), register_entity_name);
        }
        return unknown_user_exception_str;
    }

}
