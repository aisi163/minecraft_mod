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

import com.aisi163.npc_builder.NPCEntity.NPCEntity;
import com.aisi163.npc_builder.NPCEntity.exceptions.BehaviorInheritanceException;
import com.aisi163.npc_builder.NPCEntity.exceptions.SetPositionException;
import com.aisi163.npc_builder.NPCEntity.exceptions.SetTagNameException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class EquipmentGetNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "equipment_get";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc equipment_get [NPC名称] - 查询NPC托管实体的所有装备/手持物信息";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        // 1. 参数校验
        if (args.length < 1) {
            sender.sendMessage(new TextComponentString("§c参数不足！用法：" + getUsage(sender) + "\n§7示例：/npc equipment_get 村庄守卫NPC"));
            return;
        }

        String npcName = args[0];

        NPCEntity targetNPC = findNPCByName(world, npcName);
        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到名称为「\" + npcName + \"」的NPC！\\n§7可输入 /npc list 查看所有NPC名称"));
            return;
        }

        try {
            if (targetNPC.getManagerEntity() == null) {
                sender.sendMessage(new TextComponentString("§cNPC「" + npcName + "」暂无托管实体！"));
                return;
            }
        } catch (SetTagNameException | SetPositionException e) {
            sender.sendMessage(new TextComponentString("§当前查询的NPC(名称: " + targetNPC.getNpcName() + ") 托管实体损坏,我们尝试修复, 但: " + e.getMessage()));
            return;
        } catch (BehaviorInheritanceException e) {
            sender.sendMessage(new TextComponentString("§当前查询的NPC(名称: " + targetNPC.getNpcName() + ") 托管实体损坏,我们尝试修复, 但: " + e.toUserFriendlyMessage()));
            return;
        }

        sender.sendMessage(new TextComponentString(
                "§a===== NPC「" + npcName + "」装备信息 =====\n" +
                        "§7托管实体类型：§f" + targetNPC.getInheritedFrom() + "\n" +
                        "§7装备详情：\n" + targetNPC.getHostingEntityEquipmentInfo()
        ));
    }

    private NPCEntity findNPCByName(World world, String npcName) {
        for (net.minecraft.entity.Entity entity : world.loadedEntityList) {
            if (entity instanceof NPCEntity && ((NPCEntity) entity).getNpcName() != null
                    && ((NPCEntity) entity).getNpcName().equals(npcName)) {
                return (NPCEntity) entity;
            }
        }
        return null;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            List<String> npcNames = new ArrayList<>();
            for (net.minecraft.entity.Entity entity : sender.getEntityWorld().loadedEntityList) {
                if (entity instanceof NPCEntity && ((NPCEntity) entity).getNpcName() != null) {
                    npcNames.add(((NPCEntity) entity).getNpcName());
                }
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, npcNames);
        }
        return new ArrayList<>();
    }
}