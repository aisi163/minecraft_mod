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


public class AffairNPCSubCommand implements INPCSubCommand {
    private final Map<String, INPCSubSubCommand> subSubCommandMap = new HashMap<>();

    public AffairNPCSubCommand() {
        subSubCommandMap.put("dimension_change", new DimensionChangeSubSubCommand());
        subSubCommandMap.put("position_change", new PositionChangeSubSubCommand());
        subSubCommandMap.put("command_execute", new CommandExecuteNPCSubCommand());
        subSubCommandMap.put("server_change", new ServerChangeSubSubCommand());
    }

    @Override
    public String getName() {
        return "add_affair";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc affair [二级子命令] [参数] - NPC业务管理（维度/坐标/服务器跳转）\n" +
                "  二级子命令：\n" +
                "    dimension_change [NPC名称] [维度码] [X] [Y] [Z] - 绑定维度跳转业务\n" +
                "    position_change [NPC名称] [X] [Y] [Z] - 绑定坐标跳转业务\n" +
                "    command_execute [NPC名称] [minecraft_command] - 让触发 NPC 交互的玩家执行 Minecraft 原生命令\n" +
                " §c server_change [NPC名称] [服务器地址] [端口] - 绑定服务器跳转业务 (此功能暂未开发)";
    };

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.sendMessage(new TextComponentString("§c缺少二级子命令！用法：" + getUsage(sender)));
            return;
        }

        String subSubCmdName = args[0].toLowerCase();
        String[] subSubArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subSubArgs, 0, args.length - 1);

        INPCSubSubCommand subSubCommand = subSubCommandMap.get(subSubCmdName);
        if (subSubCommand != null) {
            subSubCommand.execute(server, sender, world, subSubArgs);
        } else {
            StringBuilder errorMsg = new StringBuilder("§c未知二级子命令：").append(subSubCmdName).append("\n");
            errorMsg.append("§7可用二级子命令：");
            for (String cmd : subSubCommandMap.keySet()) {
                errorMsg.append(" §f").append(cmd).append("§7,");
            }
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            sender.sendMessage(new TextComponentString(errorMsg.toString()));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, subSubCommandMap.keySet());
        } else if (args.length >= 2) {
            String subSubCmdName = args[0].toLowerCase();
            INPCSubSubCommand subSubCommand = subSubCommandMap.get(subSubCmdName);
            if (subSubCommand != null) {
                String[] subSubArgs = new String[args.length - 1];
                System.arraycopy(args, 1, subSubArgs, 0, args.length - 1);
                return subSubCommand.getTabCompletions(server, sender, subSubArgs, targetPos);
            }
        }
        return new ArrayList<>();
    }
}