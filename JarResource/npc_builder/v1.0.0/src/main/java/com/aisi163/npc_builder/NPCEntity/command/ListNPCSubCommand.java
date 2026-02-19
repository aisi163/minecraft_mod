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
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ListNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc list - 列出所有NPC（格式：名称->所托管实体）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (args.length > 0) {
            throw new CommandException("§c参数过多！用法：" + getUsage(sender));
        }

        List<NPCEntity> npcList = new ArrayList<>();
        for (net.minecraft.entity.Entity entity : world.loadedEntityList) {
            if (entity instanceof NPCEntity) {
                npcList.add((NPCEntity) entity);
            }
        }

        if (npcList.isEmpty()) {
            sender.sendMessage(new TextComponentString("§e当前世界暂无已创建的NPC！"));
            return;
        }

        StringBuilder listStr = new StringBuilder("§a所有NPC列表：");
        for (NPCEntity npc : npcList) {
            String hostingEntityName = null;
            hostingEntityName = npc.getInheritedFrom();
            if (hostingEntityName.isEmpty()) {
                hostingEntityName = "未绑定";
            };
            listStr.append(" §f").append(npc.getNpcName()).append("->").append(hostingEntityName).append("§7,");
        }

        if (listStr.charAt(listStr.length() - 1) == ',') {
            listStr.deleteCharAt(listStr.length() - 1);
        }
        sender.sendMessage(new TextComponentString(listStr.toString()));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return new ArrayList<>();
    }
}