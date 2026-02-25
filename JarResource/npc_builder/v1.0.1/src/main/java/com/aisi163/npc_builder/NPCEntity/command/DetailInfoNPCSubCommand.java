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
import com.aisi163.npc_builder.NPCEntity.exceptions.SetPositionException;
import com.aisi163.npc_builder.NPCEntity.exceptions.BehaviorInheritanceException;
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

public class DetailInfoNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "detail_info";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc detail_info [NPC名称] - 查看指定NPC的详细信息";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString("§c参数错误！用法：" + getUsage(sender)));
            return;
        }

        String npcName = args[0];
        NPCEntity targetNPC = null;
        for (net.minecraft.entity.Entity entity : world.loadedEntityList) {
            if (entity instanceof NPCEntity && ((NPCEntity) entity).getNpcName() != null
                    && ((NPCEntity) entity).getNpcName().equals(npcName)) {
                targetNPC = (NPCEntity) entity;
                break;
            }
        }

        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到名称为「" + npcName + "」的NPC！"));
            return;
        }

        try {
            String detail = "§7[1] §f名称: " + targetNPC.getNpcName() + " (标签名: " + targetNPC.getTagName() + ")" +
                    " | 坐标：X=" + targetNPC.getPosX() + ", Y=" + targetNPC.getPosY() + ", Z=" + targetNPC.getPosZ() +
                    " | 继承实体: " + targetNPC.getInheritedFrom() + " 继承实体UID: " + targetNPC.getHostingUID() +
                    " 实体Obj: " + (targetNPC.getManagerEntity() != null ? targetNPC.getManagerEntity().toString() : "null") + " 绑定业务: " + targetNPC.getCurrentAffairType().getAffair_description();
            sender.sendMessage(new TextComponentString(detail));
        } catch (SetTagNameException | SetPositionException e) {
            sender.sendMessage(new TextComponentString("§当前查询的NPC(名称: " + targetNPC.getNpcName() + ") 托管实体损坏,我们尝试修复, 但: " + e.getMessage()));
        } catch (BehaviorInheritanceException e) {
            sender.sendMessage(new TextComponentString("§当前查询的NPC(名称: " + targetNPC.getNpcName() + ") 托管实体损坏,我们尝试修复, 但: " + e.toUserFriendlyMessage()));
            return;
        }
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