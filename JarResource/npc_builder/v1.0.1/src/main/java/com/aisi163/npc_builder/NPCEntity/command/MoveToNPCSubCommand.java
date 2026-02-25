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

public class MoveToNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "move_to";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc move_to [NPC名称] [坐标X] [坐标Y] [坐标Z] - 移动指定NPC到目标坐标（支持~相对坐标，如~ ~1 ~-2）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (args.length < 4) {
            throw new CommandException("§c参数不足！用法：" + getUsage(sender));
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
            throw new CommandException("§c未找到名称为「" + npcName + "」的NPC！");
        }

        double x, y, z;
        BlockPos senderPos = targetNPC.getPosition();
        try {
            x = parseRelativeCoordinate(senderPos.getX(), args[1]);
            y = parseRelativeCoordinate(senderPos.getY(), args[2]);
            z = parseRelativeCoordinate(senderPos.getZ(), args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(new TextComponentString("§c坐标格式错误！必须是数字或~相对坐标（如：100 64 200 或 ~ ~1 ~-2）"));
            return;
        }

        try {
            targetNPC.SetPos(x, y, z);
            sender.sendMessage(new TextComponentString(
                    "§aNPC「" + npcName + "」已移动到坐标：" +
                            "X=" + String.format("%.1f", x) + ", Y=" + String.format("%.1f", y) + ", Z=" + String.format("%.1f", z)
            ));
        } catch (SetPositionException e) {
            sender.sendMessage(new TextComponentString("§c移动失败：" + e.getMessage()));
        }
    }

    private double parseRelativeCoordinate(double base, String input) throws NumberFormatException {
        if (input.equals("~")) {
            return base;
        }
        if (input.startsWith("~")) {
            String offsetStr = input.substring(1);
            if (offsetStr.isEmpty()) {
                return base;
            }
            double offset = Double.parseDouble(offsetStr);
            return base + offset;
        }
        return Double.parseDouble(input);
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
        } else if (args.length >= 2 && args.length <= 4) {
            return CommandBase.getTabCompletionCoordinate(args, 1, targetPos);
        }
        return new ArrayList<>();
    }
}