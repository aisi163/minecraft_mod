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

package com.aisi163.npc_builder.NPCEntity.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpNPCSubCommand implements INPCSubCommand {
    private final Map<String, String> detailedHelpMap = new HashMap<>();

    public HelpNPCSubCommand() {
        initDetailedHelp();
    }

    private void initDetailedHelp() {
        detailedHelpMap.put("create",
                "§a===== /npc create 详细帮助 =====\n" +
                        "§7【功能】§f创建一个自定义NPC（基于指定实体，如僵尸、骷髅）\n" +
                        "§7【用法】§f/npc create [坐标X] [坐标Y] [坐标Z] [继承实体ID] [NPC名称] [标签名]\n" +
                        "§7【参数说明】\n" +
                        "  §f[坐标X/Y/Z]：NPC生成的位置，支持数字（如100）或~（当前位置），~1表示当前位置+1\n" +
                        "  §f[继承实体ID]：NPC的基础实体类型，比如：\n" +
                        "    - minecraft:zombie（僵尸）、minecraft:skeleton（骷髅）\n" +
                        "    - minecraft:villager（村民）、minecraft:creeper（苦力怕）\n" +
                        "  §f[NPC名称]：NPC的唯一标识，不能有空格（如：我的第一个NPC → 我的第一个NPC 不行，要写 我的第一个NPC 换成 我的第一个NPC 无空格）\n" +
                        "  §f[标签名]：NPC头顶显示的名称，支持空格（如：村庄守卫）\n" +
                        "§7【示例】\n" +
                        "  §f/npc create 100 64 200 minecraft:zombie 村庄守卫NPC 村庄守卫\n" +
                        "  §f/npc create ~ ~1 ~ minecraft:skeleton 骷髅弓箭手 骷髅弓箭手\n" +
                        "§7【注意事项】\n" +
                        "  §f- NPC名称不能重复、不能有空格\n" +
                        "  §f- 继承实体ID必须是游戏里存在的（比如不能写minecraft:abc）");

        detailedHelpMap.put("list",
                "§a===== /npc list 详细帮助 =====\n" +
                        "§7【功能】§f查看当前世界所有已创建的NPC，仅显示“NPC名称→托管实体”（不换行）\n" +
                        "§7【用法】§f/npc list\n" +
                        "§7【参数说明】§f无参数\n" +
                        "§7【示例】\n" +
                        "  §f/npc list → 输出: 所有NPC列表：村庄守卫NPC->僵尸, 骷髅弓箭手->骷髅\n" +
                        "§7【注意事项】§f如果显示“暂无NPC”，说明当前世界还没创建任何NPC");

        detailedHelpMap.put("detail_info",
                "§a===== /npc detail_info 详细帮助 =====\n" +
                        "§7【功能】§f查看指定NPC的所有详细信息（坐标、继承实体、UID等）\n" +
                        "§7【用法】§f/npc detail_info [NPC名称]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：创建NPC时设置的“唯一标识名称”（不是标签名）\n" +
                        "§7【示例】\n" +
                        "  §f/npc detail_info 村庄守卫NPC\n" +
                        "  §f输出格式：§7[1] §f名称: 村庄守卫NPC (标签名: 村庄守卫) | 坐标：X=100.0, Y=64.0, Z=200.0 | 继承实体: minecraft:zombie 继承实体UID: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx 实体Obj: EntityZombie['村庄守卫'/123, l='ServerLevel[world]', x=100.00, y=64.00, z=200.00]\n" +
                        "§7【注意事项】§f输入的是NPC名称（创建时的唯一标识），不是标签名");

        detailedHelpMap.put("move_to",
                "§a===== /npc move_to 详细帮助 =====\n" +
                        "§7【功能】§f把指定NPC移动到目标坐标\n" +
                        "§7【用法】§f/npc move_to [NPC名称] [坐标X] [坐标Y] [坐标Z]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：要移动的NPC的唯一标识名称\n" +
                        "  §f[坐标X/Y/Z]：目标位置，支持数字或~（当前位置）\n" +
                        "§7【示例】\n" +
                        "  §f/npc move_to 村庄守卫NPC 200 64 300 → 移动到X=200,Y=64,Z=300\n" +
                        "  §f/npc move_to 骷髅弓箭手 ~ ~5 ~ → 移动到当前位置上方5格\n" +
                        "§7【注意事项】§f移动后NPC的托管实体（如僵尸）会一起移动");

        detailedHelpMap.put("remove",
                "§a===== /npc remove 详细帮助 =====\n" +
                        "§7【功能】§f删除指定NPC（包括其托管的实体，如僵尸）\n" +
                        "§7【用法】§f/npc remove [NPC名称]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：要删除的NPC的唯一标识名称\n" +
                        "§7【示例】\n" +
                        "  §f/npc remove 村庄守卫NPC → 删除名为“村庄守卫NPC”的NPC\n" +
                        "§7【注意事项】§f删除后无法恢复，请谨慎操作");

        detailedHelpMap.put("rename",
                "§a===== /npc rename 详细帮助 =====\n" +
                        "§7【功能】§f修改NPC的唯一标识名称（不是头顶的标签名）\n" +
                        "§7【用法】§f/npc rename [原NPC名称] [新NPC名称]\n" +
                        "§7【参数说明】\n" +
                        "  §f[原NPC名称]：NPC当前的唯一标识名称\n" +
                        "  §f[新NPC名称]：新的唯一标识名称，不能重复、不能有空格\n" +
                        "§7【示例】\n" +
                        "  §f/npc rename 村庄守卫NPC 小镇守卫NPC → 把NPC名称从“村庄守卫NPC”改成“小镇守卫NPC”\n" +
                        "§7【注意事项】§f新名称不能和现有NPC重名，否则会提示失败");

        detailedHelpMap.put("rename_tag",
                "§a===== /npc rename_tag 详细帮助 =====\n" +
                        "§7【功能】§f修改NPC头顶显示的标签名（支持空格）\n" +
                        "§7【用法】§f/npc rename_tag [NPC名称] [新标签名]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：NPC的唯一标识名称（不是标签名）\n" +
                        "  §f[新标签名]：NPC头顶要显示的新名称，支持空格\n" +
                        "§7【示例】\n" +
                        "  §f/npc rename_tag 小镇守卫NPC 小镇最强守卫 → 标签名改成“小镇最强守卫”\n" +
                        "  §f/npc rename_tag 骷髅弓箭手 远程输出 → 标签名改成“远程输出”\n" +
                        "§7【注意事项】§f修改后NPC头顶的名称会立即更新");

        detailedHelpMap.put("hosting_entity",
                "§a===== /npc hosting_entity 详细帮助 =====\n" +
                        "§7【功能】§f为已创建的NPC更换托管实体（比如把僵尸NPC换成骷髅），旧实体自动删除，新实体和NPC同位置\n" +
                        "§7【用法】§f/npc hosting_entity [NPC名称] [实体注册名]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：要更换实体的NPC唯一标识名称（如：村庄守卫NPC）\n" +
                        "  §f[实体注册名]：新的托管实体类型，格式为“minecraft:实体名”，常用示例：\n" +
                        "    - minecraft:zombie（僵尸）、minecraft:skeleton（骷髅）\n" +
                        "    - minecraft:villager（村民）、minecraft:creeper（苦力怕）\n" +
                        "    - minecraft:spider（蜘蛛）、minecraft:enderman（末影人）\n" +
                        "§7【示例】\n" +
                        "  §f/npc hosting_entity 村庄守卫NPC minecraft:skeleton → 把村庄守卫NPC的托管实体从僵尸换成骷髅\n" +
                        "  §f/npc hosting_entity 骷髅弓箭手 minecraft:creeper → 把骷髅弓箭手的托管实体换成苦力怕\n" +
                        "§7【注意事项】\n" +
                        "  §f- 实体注册名必须是游戏里存在的（不能写minecraft:abc）\n" +
                        "  §f- 更换后旧托管实体会被删除，新实体出现在NPC当前位置\n" +
                        "  §f- 可输入 /npc list 查看NPC当前的托管实体类型");

        detailedHelpMap.put("add_affair",
                "§a===== /npc add_affair 详细帮助 =====\n" +
                        "§7【功能】§f为NPC绑定跳转业务（维度/坐标/服务器），每个NPC仅能绑定一个业务\n" +
                        "§7【用法】§f/npc add_affair [二级子命令] [参数]\n" +
                        "§7【二级子命令说明】\n" +
                        "  §f- dimension_change [NPC名称] [维度码] [X] [Y] [Z]：绑定维度跳转（维度码0=主世界，1=下界，2=末地）\n" +
                        "  §f- position_change [NPC名称] [X] [Y] [Z]：绑定同维度坐标跳转\n" +
                        "  §f- command_execute [NPC名称] [minecraft_command]：详细帮助详细请输入 /npc help command_execute 查看\n" +
                        "  §f- server_change [NPC名称] [服务器地址] [端口]：绑定服务器跳转（仅记录参数）\n" +
                        "§7【示例】\n" +
                        "  §f/npc add_affair dimension_change 村庄守卫NPC 1 ~ ~ ~ → 绑定到下界当前位置\n" +
                        "  §f/npc add_affair position_change 村庄守卫NPC 200 64 300 → 绑定到坐标200,64,300\n" +
                        "  §f/npc add_affair server_change 村庄守卫NPC 192.168.1.100 25565 → 绑定到192.168.1.100:25565\n" +
                        "§7【注意事项】\n" +
                        "  §f- 每个NPC仅能绑定一个业务，绑定新业务前需先执行 /npc drop_affair 删除旧业务\n" +
                        "  §c- /npc add_affair server_change (命令功能暂未开发)");

        detailedHelpMap.put("command_execute",
                "§a===== /npc add_affair command_execute 详细帮助 =====\n" +
                        "§7【功能】§f让指定NPC的托管实体执行Minecraft原生命令\n" +
                        "§7【用法】§f/npc add_affair command_execute [NPC名称] [MC命令内容]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：已创建的NPC唯一名称（无空格）\n" +
                        "  §f[MC命令内容]：要执行的Minecraft原生命令（支持带空格，无需加/）\n" +
                        "§7【权限要求】§f执行玩家需拥有对应MC命令的权限（如OP）\n" +
                        "§7【常用示例】\n" +
                        "  §f1. 发送公共消息：/npc add_affair command_execute 守卫NPC say 禁止入内！\n" +
                        "  §f2. 给玩家物品：/npc add_affair command_execute 商人NPC give @p minecraft:diamond 1\n" +
                        "  §f3. 生成实体：/npc add_affair command_execute 弓箭手NPC summon minecraft:arrow ~ ~1 ~\n" +
                        "  §f4. 移动NPC：/npc add_affair command_execute 农民NPC tp ~ ~ ~10\n" +
                        "  §f5. 设置方块：/npc add_affair command_execute 建筑工NPC setblock ~ ~1 ~ minecraft:stone\n" +
                        "§7【注意事项】\n" +
                        "  §f- 命令中的相对坐标（~）基于NPC托管实体的位置计算\n" +
                        "  §f- 若NPC无托管实体，命令会执行失败并提示");

        detailedHelpMap.put("search_affair",
                "§a===== /npc _search_affair 详细帮助 =====\n" +
                        "§7【功能】§f查询指定NPC的当前绑定业务（无业务则提示“无绑定业务”）\n" +
                        "§7【用法】§f/npc search_affair [NPC名称]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：要查询的NPC唯一标识名称\n" +
                        "§7【示例】\n" +
                        "  §f/npc affair_search 村庄守卫NPC → 输出：===== NPC「村庄守卫NPC」业务信息 =====\n" +
                        "                                        当前业务：维度跳转\n" +
                        "                                        业务参数：dimension_code=1，x=100.0，y=64.0，z=200.0\n" +
                        "§7【注意事项】§f查询的是NPC的绑定业务，不是当前位置/状态");

        detailedHelpMap.put("drop_affair",
                "§a===== /npc drop_affair 详细帮助 =====\n" +
                        "§7【功能】§f删除指定NPC的当前绑定业务，恢复无业务状态\n" +
                        "§7【用法】§f/npc drop_affair [NPC名称]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：要删除业务的NPC唯一标识名称\n" +
                        "§7【示例】\n" +
                        "  §f/npc drop_affair 村庄守卫NPC → 删除该NPC的所有绑定业务\n" +
                        "§7【注意事项】\n" +
                        "  §f- 仅能删除已绑定的业务，无业务时执行会提示失败\n" +
                        "  §f- 删除后可重新为NPC绑定新业务");

        detailedHelpMap.put("equipment",
                "§a===== /npc equipment 详细帮助 =====\n" +
                        "§7【功能】§f设置NPC托管实体的装备/手持物（仅支持活物实体：僵尸、骷髅、村民等）\n" +
                        "§7【用法】§f/npc equipment set [NPC名称] [部位] [物品ID] [数量]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：要设置装备的NPC唯一标识名称\n" +
                        "  §f[部位]：可选值（小写）：\n" +
                        "    - head：头部（头盔/帽子）、chest：胸部（胸甲）、legs：腿部（护腿）、feet：脚部（靴子）\n" +
                        "    - mainhand：主手（武器/工具）、offhand：附加手（副手，如盾牌、弓）\n" +
                        "  §f[物品ID]：MC原生物品ID（格式：minecraft:物品名），如minecraft:diamond_sword\n" +
                        "  §f[数量]：可选，默认1，范围1~64（仅堆叠物品有效）\n" +
                        "§7【示例】\n" +
                        "  §f/npc equipment set 村庄守卫NPC mainhand minecraft:iron_sword 1 → 主手装备铁剑\n" +
                        "  §f/npc equipment set 村庄守卫NPC head minecraft:diamond_helmet → 头部装备钻石头盔\n" +
                        "  §f/npc equipment set 骷髅弓箭手 offhand minecraft:bow → 附加手装备弓\n" +
                        "§7【注意事项】\n" +
                        "  §f- 仅支持活物实体（僵尸、骷髅、村民等），盔甲架等无装备槽的实体无法设置\n" +
                        "  §f- 物品ID必须是MC原生ID（如minecraft:netherite_sword），自定义物品需确保已注册");

        detailedHelpMap.put("equipment",
                "§a===== /npc equipment 详细帮助 =====\n" +
                        "§7【功能】§f设置NPC托管实体的装备/手持物（仅支持活物实体：僵尸、骷髅、村民等）\n" +
                        "§7【用法】§f/npc equipment set [NPC名称] [部位] [物品ID] [数量]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：要设置装备的NPC唯一标识名称\n" +
                        "  §f[部位]：可选值（小写）：\n" +
                        "    - head：头部（头盔/帽子）、chest：胸部（胸甲）、legs：腿部（护腿）、feet：脚部（靴子）\n" +
                        "    - mainhand：主手（武器/工具）、offhand：附加手（副手，如盾牌、弓）\n" +
                        "  §f[物品ID]：MC原生物品ID（格式：minecraft:物品名），如minecraft:diamond_sword\n" +
                        "  §f[数量]：可选，默认1，范围1~64（仅堆叠物品有效）\n" +
                        "§7【示例】\n" +
                        "  §f/npc equipment set 村庄守卫NPC mainhand minecraft:iron_sword 1 → 主手装备铁剑\n" +
                        "  §f/npc equipment set 村庄守卫NPC head minecraft:diamond_helmet → 头部装备钻石头盔\n" +
                        "  §f/npc equipment set 骷髅弓箭手 offhand minecraft:bow → 附加手装备弓\n" +
                        "§7【注意事项】\n" +
                        "  §f- 仅支持活物实体（僵尸、骷髅、村民等），盔甲架等无装备槽的实体无法设置\n" +
                        "  §f- 物品ID必须是MC原生ID（如minecraft:netherite_sword），自定义物品需确保已注册\n"+
                        "  §f- 使用/npc equipment set [NPC名称] [部位] minecraft:air 1 可以将 NPC 在 [部位] 上的物品清空\n");

        detailedHelpMap.put("equipment_get",
                "§a===== /npc equipment_get 详细帮助 =====\n" +
                        "§7【功能】§f查询NPC托管实体的所有装备/手持物信息\n" +
                        "§7【用法】§f/npc equipment_get [NPC名称]\n" +
                        "§7【参数说明】\n" +
                        "  §f[NPC名称]：要查询的NPC唯一标识名称\n" +
                        "§7【示例】\n" +
                        "  §f/npc equipment_get 村庄守卫NPC → 输出该NPC托管实体的头部/胸部/主手等装备信息\n" +
                        "§7【注意事项】§f无托管实体或托管实体非活物时，会提示对应错误信息\n");
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc help [子命令名] - 查看NPC命令的详细帮助（不加子命令名显示所有子命令简要帮助）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (args.length == 0) {
            StringBuilder briefHelp = new StringBuilder("§a===== NPC命令总帮助 =====\n");
            briefHelp.append("§7输入 /npc help [子命令名] 查看详细帮助 (如：/npc help create)\n");
            briefHelp.append("§7所有子命令列表：\n");

            for (Map.Entry<String, String> entry : detailedHelpMap.entrySet()) {
                String subCmd = entry.getKey();
                String briefUsage = entry.getValue().split("§7【用法】§f")[1].split("\n")[0];
                briefHelp.append("  §f- ").append(briefUsage).append("\n");
            }

            sender.sendMessage(new TextComponentString(briefHelp.toString()));
            return;
        }

        String subCmd = args[0].toLowerCase();
        if (detailedHelpMap.containsKey(subCmd)) {
            sender.sendMessage(new TextComponentString(detailedHelpMap.get(subCmd)));
        } else {
            StringBuilder errorMsg = new StringBuilder("§c未知子命令：").append(subCmd).append("\n");
            errorMsg.append("§7可用子命令：");
            for (String cmd : detailedHelpMap.keySet()) {
                errorMsg.append(" §f").append(cmd).append("§7,");
            }
            if (errorMsg.charAt(errorMsg.length() - 1) == ',') {
                errorMsg.deleteCharAt(errorMsg.length() - 1);
            }
            sender.sendMessage(new TextComponentString(errorMsg.toString()));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, detailedHelpMap.keySet());
        }
        return new ArrayList<>();
    }
}