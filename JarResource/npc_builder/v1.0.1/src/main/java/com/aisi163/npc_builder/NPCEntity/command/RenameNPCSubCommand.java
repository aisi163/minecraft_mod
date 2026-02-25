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

public class RenameNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc rename [原NPC名称] [新NPC名称] - 重命名指定NPC（名称不可重复/含空格）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (args.length != 2) {
            sender.sendMessage(new TextComponentString("§c参数错误！用法：" + getUsage(sender)));
            return;
        }

        String originalName = args[0];
        String newName = args[1];

        if (newName.isEmpty() || newName.contains(" ")) {
            sender.sendMessage(new TextComponentString("§c新名称不能为空且不能包含空格！"));
            return;
        }

        NPCEntity targetNPC = null;
        for (net.minecraft.entity.Entity entity : world.loadedEntityList) {
            if (entity instanceof NPCEntity && ((NPCEntity) entity).getNpcName() != null
                    && ((NPCEntity) entity).getNpcName().equals(originalName)) {
                targetNPC = (NPCEntity) entity;
                break;
            }
        }

        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到原名称为「" + originalName + "」的NPC！"));
            return;
        }

        boolean nameConflict = false;
        for (net.minecraft.entity.Entity entity : world.loadedEntityList) {
            if (entity instanceof NPCEntity && entity != targetNPC
                    && ((NPCEntity) entity).getNpcName() != null
                    && ((NPCEntity) entity).getNpcName().equals(newName)) {
                nameConflict = true;
                break;
            }
        }

        if (nameConflict) {
            sender.sendMessage(new TextComponentString("§c新名称「" + newName + "」已被占用！"));
            return;
        }

        targetNPC.SetNPCName(newName);
        sender.sendMessage(new TextComponentString("§aNPC已重命名：「" + originalName + "」→「" + newName + "」"));
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