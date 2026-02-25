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

public class PositionChangeSubSubCommand implements INPCSubSubCommand {
    @Override
    public String getName() {
        return "position_change";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc affairs position_change [NPC名称] [X] [Y] [Z] - 为NPC绑定坐标跳转业务（同维度内跳转）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        // 1. 服务端校验
        if (world.isRemote) {
            throw new CommandException("§c错误：该命令仅能在服务端执行！");
        }

        if (args.length < 4) {
            sender.sendMessage(new TextComponentString("§c参数不足！用法：" + getUsage(sender) + "\n§7示例：/npc affairs position_change 村庄守卫NPC ~ ~10 ~（跳转到当前位置上方10格）"));
            return;
        }

        String npcName = args[0];
        double x, y, z;
        BlockPos senderPos = sender.getPosition();

        try {
            x = parseRelativeCoordinate(senderPos.getX(), args[1]);
            y = parseRelativeCoordinate(senderPos.getY(), args[2]);
            z = parseRelativeCoordinate(senderPos.getZ(), args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(new TextComponentString("§c坐标格式错误！必须是数字或~相对坐标（如：100 64 200 或 ~ ~1 ~）"));
            return;
        }

        NPCEntity targetNPC = findNPCByName(world, npcName);
        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到名称为「" + npcName + "」的NPC！\n§7可输入 /npc list 查看所有NPC名称"));
            return;
        }

        if (!targetNPC.getCurrentAffairType().same_affair(NPCAffairsType.NONE_AFFAIR)) {
            sender.sendMessage(new TextComponentString("§cNPC「" + npcName + "」已绑定「" + targetNPC.getCurrentAffairType().getAffair_description() + "」业务！\n§7请先执行 /npc drop_affairs " + npcName + " 删除现有业务，再绑定新业务"));
            return;
        }

        Map<String, Double> params = new HashMap<>();
        params.put("position_change_x", x);
        params.put("position_change_y", y);
        params.put("position_change_z", z);
        boolean bindSuccess = targetNPC.bindDoubleAffairs(NPCAffairsType.POSITION_CHANGE_AFFAIR, params);
        if (bindSuccess) {
            sender.sendMessage(new TextComponentString(
                    "§a 绑定成功！\n" +
                            "§7├─ NPC名称：§f" + npcName + "\n" +
                            "§7├─ 绑定业务：§f" + NPCAffairsType.POSITION_CHANGE_AFFAIR.getAffair_description() + "\n" +
                            "§7├─ 目标维度：§f当前维度（" + world.provider.getDimension() + "）\n" +
                            "§7└─ 目标坐标：§fX=" + String.format("%.1f", x) + ", Y=" + String.format("%.1f", y) + ", Z=" + String.format("%.1f", z)
            ));
        } else {
            sender.sendMessage(new TextComponentString("§c绑定失败！ NPC可能已绑定其他业务，请先删除"));
        }
    }

    private double parseRelativeCoordinate(double base, String input) throws NumberFormatException {
        if (input.equals("~")) return base;
        if (input.startsWith("~")) {
            String offsetStr = input.substring(1);
            if (offsetStr.isEmpty()) return base;
            double offset = Double.parseDouble(offsetStr);
            return base + offset;
        }
        return Double.parseDouble(input);
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
        } else if (args.length >= 2 && args.length <= 4) {
            return CommandBase.getTabCompletionCoordinate(args, 1, targetPos);
        }
        return new ArrayList<>();
    }
}