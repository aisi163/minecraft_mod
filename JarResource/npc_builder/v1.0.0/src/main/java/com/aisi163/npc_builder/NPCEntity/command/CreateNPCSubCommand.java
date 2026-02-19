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
import com.aisi163.npc_builder.NPCEntity.exceptions.*;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CreateNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc create [坐标X] [坐标Y] [坐标Z] [继承实体ID] [NPC名称] [标签名] - 创建NPC（继承实体如：minecraft:zombie）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (args.length < 6) {
            throw new CommandException("§c参数不足！用法：" + getUsage(sender));
        }

        double x, y, z;
        BlockPos senderPos = sender.getPosition();
        try {
            x = args[0].equals("~") ? senderPos.getX() : Double.parseDouble(args[0]);
            y = args[1].equals("~") ? senderPos.getY() : Double.parseDouble(args[1]);
            z = args[2].equals("~") ? senderPos.getZ() : Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(new TextComponentString("§c坐标格式错误！必须是数字或~（如：100 64 200 或 ~ ~ ~）"));
            return;
        }

        String inheritEntityId = args[3];
        if (!EntityList.getEntityNameList().contains(new ResourceLocation(inheritEntityId))) {
            sender.sendMessage(new TextComponentString("§c继承实体不存在！支持的实体：minecraft:zombie（僵尸）、minecraft:skeleton（骷髅）等"));
            return;
        }

        String npcName = args[4];
        if (npcName.isEmpty() || npcName.contains(" ")) {
            sender.sendMessage(new TextComponentString("§cNPC名称不能为空且不能包含空格！"));
            return;
        }

        StringBuilder tagName = new StringBuilder(args[5]);
        for (int i = 6; i < args.length; i++) {
            tagName.append(" ").append(args[i]);
        }
        if (tagName.length() == 0) {
            sender.sendMessage(new TextComponentString("§cNPC标签名不能为空！"));
            return;
        }

        try {
            NPCEntity npc = new NPCEntity(world, npcName, inheritEntityId);
            npc.SetPos(x, y, z);
            npc.SetTagName(tagName.toString());
            world.spawnEntity(npc);
            world.spawnEntity(npc.getManagerEntity());
            if (sender instanceof net.minecraft.entity.player.EntityPlayer) {
                net.minecraft.entity.player.EntityPlayer player = (net.minecraft.entity.player.EntityPlayer) sender;
                float playerYaw = player.rotationYaw;
                float playerPitch = player.rotationPitch;

                npc.rotationYaw = playerYaw;
                npc.rotationPitch = playerPitch;

                net.minecraft.entity.Entity managerEntity = npc.getManagerEntity();
                if (managerEntity != null) {
                    managerEntity.rotationYaw = playerYaw;
                    managerEntity.rotationPitch = playerPitch;
                    if (managerEntity instanceof net.minecraft.entity.EntityLivingBase) {
                        ((net.minecraft.entity.EntityLivingBase) managerEntity).rotationYawHead = playerYaw;
                    }
                }
            };

            sender.sendMessage(new TextComponentString(
                    "§a NPC创建成功！\n" +
                            "§7- 名称：§f" + npcName + "\n" +
                            "§7- 标签：§f" + tagName + "\n" +
                            "§7- 坐标：§fX=" + x + ", Y=" + y + ", Z=" + z + "\n" +
                            "§7- 继承实体：§f" + inheritEntityId
            ));
        } catch (BehaviorInheritanceException e) {
            sender.sendMessage(new TextComponentString("§c创建失败：" + e.toUserFriendlyMessage()));
        } catch (NameConflictException e){
            sender.sendMessage(new TextComponentString("§c创建失败：" + e.toUserFriendlyMessage()));
        } catch (SetPositionException | SetTagNameException e) {
            sender.sendMessage(new TextComponentString("§c创建失败：" + e.getMessage()));
        };
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length >= 1 && args.length <= 3) {
            return CommandBase.getTabCompletionCoordinate(args, 0, targetPos);
        } else if (args.length == 4) {
            List<String> entityNames = new ArrayList<>();
            for (ResourceLocation rl : EntityList.getEntityNameList()) {
                if (rl.toString().toLowerCase().startsWith(args[3].toLowerCase())) {
                    entityNames.add(rl.toString());
                }
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, entityNames);
        }
        return new ArrayList<>();
    }
}