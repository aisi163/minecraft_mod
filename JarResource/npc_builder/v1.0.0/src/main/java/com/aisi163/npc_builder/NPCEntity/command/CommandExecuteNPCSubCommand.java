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

import com.aisi163.npc_builder.NPCEntity.NPCAffairsType;
import com.aisi163.npc_builder.NPCEntity.NPCEntity;
import com.aisi163.npc_builder.NPCEntity.exceptions.SetPositionException;
import com.aisi163.npc_builder.NPCEntity.exceptions.SetTagNameException;
import com.aisi163.npc_builder.NPCEntity.exceptions.BehaviorInheritanceException;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommandExecuteNPCSubCommand implements INPCSubSubCommand {
    @Override
    public String getName() {
        return "command_execute";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc add_affairs command_execute [NPC名称] [minecraft_command] - 让指定NPC执行Minecraft原生命令\n" +
                "§7示例：\n" +
                "  §f/npc add_affairs command_execute 守卫NPC say 欢迎来到村庄！\n" +
                "  §f/npc add_affairs command_execute 商人NPC give @p minecraft:diamond 1\n" +
                "  §f/npc add_affairs command_execute 弓箭手NPC summon minecraft:arrow ~ ~1 ~";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("§c参数不足！用法：" + getUsage(sender));
        }

        String npcName = args[0];
        String mcCommand = "";
        for (int i = 1; i < args.length; i++) {
            mcCommand += args[i] + " ";
        };
        mcCommand = mcCommand.trim();


        String finalCommand = mcCommand.toString();
        if (finalCommand.isEmpty()) {
            sender.sendMessage(new TextComponentString("§c要执行的Minecraft命令不能为空！"));
            return;
        }

        NPCEntity targetNPC = findNPCByName(world, npcName);
        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到名称为「" + npcName + "」的NPC！"));
            return;
        }

        try {
            Entity executorEntity = targetNPC.getManagerEntity();
            if (executorEntity == null) {
                sender.sendMessage(new TextComponentString("§cNPC「" + npcName + "」暂无托管实体，无法执行命令！"));
                return;
            }
            if (!targetNPC.getCurrentAffairType().same_affair(NPCAffairsType.NONE_AFFAIR)){
                sender.sendMessage(new TextComponentString("§c绑定失败！ NPC可能已绑定其他业务，请先删除"));
            } else {
                Map<String, String> String_Param = new HashMap<>();
                String_Param.put("command_str", finalCommand);
                boolean bindSuccess = targetNPC.bindStringAffairs(NPCAffairsType.COMMAND_EXECUTE_AFFAIR, String_Param);
                if (bindSuccess) {
                    sender.sendMessage(new TextComponentString(
                            "§a 绑定成功！\n" +
                                    "§7├─ NPC名称：§f" + npcName + "\n" +
                                    "§7├─ 绑定业务：§f" + NPCAffairsType.COMMAND_EXECUTE_AFFAIR.getAffair_description() + "\n" +
                                    "§7└─ 绑定命令：§f" + finalCommand
                    ));
                } else {
                    sender.sendMessage(new TextComponentString("§c绑定失败！ NPC可能已绑定其他业务，请先删除"));
                }
            }
        } catch (SetTagNameException | BehaviorInheritanceException | SetPositionException e) {
            sender.sendMessage(new TextComponentString("§c获取NPC托管实体失败：" + e.getMessage()));
        }  catch (Exception e) {
            sender.sendMessage(new TextComponentString("§c未知错误：" + e.getMessage()));
        }
    }

    private NPCEntity findNPCByName(World world, String npcName) {
        if (world == null || world.loadedEntityList == null) {
            return null;
        }
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof NPCEntity && ((NPCEntity) entity).getNpcName() != null) {
                NPCEntity npc = (NPCEntity) entity;
                if (npc.getNpcName().equals(npcName)) {
                    return npc;
                }
            }
        }
        return null;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            List<String> npcNames = new ArrayList<>();
            World world = sender.getEntityWorld();
            if (world != null && world.loadedEntityList != null) {
                for (Entity entity : world.loadedEntityList) {
                    if (entity instanceof NPCEntity && ((NPCEntity) entity).getNpcName() != null) {
                        npcNames.add(((NPCEntity) entity).getNpcName());
                    }
                }
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, npcNames);
        }
        return new ArrayList<>();
    }
}