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
import java.util.*;

public class CommandNPC extends CommandBase {
    private final Map<String, INPCSubCommand> subCommandMap = new HashMap<>();

    public CommandNPC() {
        registerSubCommand(new CreateNPCSubCommand());
        registerSubCommand(new ListNPCSubCommand());
        registerSubCommand(new DetailInfoNPCSubCommand());
        registerSubCommand(new MoveToNPCSubCommand());
        registerSubCommand(new RemoveNPCSubCommand());
        registerSubCommand(new RenameNPCSubCommand());
        registerSubCommand(new RenameTagNPCSubCommand());
        registerSubCommand(new HostingEntityNPCSubCommand());
        registerSubCommand(new HelpNPCSubCommand());
        registerSubCommand(new AffairNPCSubCommand());
        registerSubCommand(new AffairSearchNPCSubCommand());
        registerSubCommand(new AffairDropNPCSubCommand());
        registerSubCommand(new EquipmentSetNPCSubCommand());
        registerSubCommand(new EquipmentGetNPCSubCommand());
    }

    private void registerSubCommand(INPCSubCommand subCommand) {
        subCommandMap.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public String getName() {
        return "npc";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        StringBuilder usage = new StringBuilder("\n");
        for (INPCSubCommand subCommand : subCommandMap.values()) {
            usage.append("§7- ").append(subCommand.getUsage(sender)).append("\n");
        }
        return usage.toString();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            sender.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }

        String subCommandName = args[0].toLowerCase();
        INPCSubCommand subCommand = subCommandMap.get(subCommandName);
        if (subCommand == null) {
            throw new CommandException("§c未知子命令！支持的命令：create、list、detail_info、move_to、remove、rename、rename_tag");
        }

        World world = sender.getEntityWorld();
        if (world.isRemote) {
            throw new CommandException("§c该命令仅能在服务端执行！");
        }

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        subCommand.execute(server, sender, world, subArgs);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, subCommandMap.keySet());
        }

        String subCommandName = args[0].toLowerCase();
        INPCSubCommand subCommand = subCommandMap.get(subCommandName);
        if (subCommand != null) {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            return subCommand.getTabCompletions(server, sender, subArgs, targetPos);
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(getRequiredPermissionLevel(), getName());
    }
}