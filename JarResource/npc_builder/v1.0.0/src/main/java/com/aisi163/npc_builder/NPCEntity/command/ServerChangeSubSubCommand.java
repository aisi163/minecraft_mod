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

public class ServerChangeSubSubCommand implements INPCSubSubCommand {
    @Override
    public String getName() {
        return "server_change";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "§c /npc affairs server_change [NPC名称] [服务器地址] [端口] - 为NPC绑定服务器跳转业务（仅记录参数，需配合模组网络逻辑实现跳转）(注: 此功能暂未开发)";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (world.isRemote) {
            throw new CommandException("§c错误：该命令仅能在服务端执行！");
        }

        if (args.length < 3) {
            sender.sendMessage(new TextComponentString("§c参数不足！用法：" + getUsage(sender) + "\n§7示例：/npc affairs server_change 村庄守卫NPC 192.168.1.100 25565"));
            return;
        }

        String npcName = args[0];
        String serverHost = args[1];
        int serverPort;

        try {
            serverPort = Integer.parseInt(args[2]);
            if (serverPort < 0 || serverPort > 65535) {
                sender.sendMessage(new TextComponentString("端口超出范围（0~65535）"));
                return;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(new TextComponentString("§c端口格式错误！必须是0~65535之间的整数（如：25565）\n§7错误原因：" + e.getMessage()));
            return;
        }

        NPCEntity targetNPC = findNPCByName(world, npcName);
        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到名称为「" + npcName + "」的NPC！\n§7可输入 /npc list 查看所有NPC名称"));
            return;
        }
        sender.sendMessage(new TextComponentString("§c无法使用此命令: 功能还未开发完毕, 开发者正在开发中...."));
    }

    // 辅助方法：查找NPC
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
        } else if (args.length == 3) {
            List<String> commonPorts = new ArrayList<>();
            commonPorts.add("25565");
            return CommandBase.getListOfStringsMatchingLastWord(args, commonPorts);
        }
        return new ArrayList<>();
    }
}