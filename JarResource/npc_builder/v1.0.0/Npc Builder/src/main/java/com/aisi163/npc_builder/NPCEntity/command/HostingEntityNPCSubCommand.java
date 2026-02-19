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

public class HostingEntityNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "hosting_entity";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc hosting_entity [NPC名称] [实体注册名] - 为指定NPC更换托管实体（支持：minecraft:zombie/skeleton/villager等）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        // 1. 严格服务端校验
        if (world.isRemote) {
            throw new CommandException("§c错误：该命令仅能在服务端执行！");
        }

        // 2. 参数数量校验（至少需要 NPC名称 + 实体注册名）
        if (args.length < 2) {
            sender.sendMessage(new TextComponentString("§c参数不足！正确用法：" + getUsage(sender) + "\n§7示例：/npc hosting_entity 村庄守卫NPC minecraft:skeleton"));
            return;
        }

        String npcName = args[0];
        String entityRegName = args[1];

        NPCEntity targetNPC = null;
        for (net.minecraft.entity.Entity entity : world.loadedEntityList) {
            if (entity instanceof NPCEntity && ((NPCEntity) entity).getNpcName() != null
                    && ((NPCEntity) entity).getNpcName().equals(npcName)) {
                targetNPC = (NPCEntity) entity;
                break;
            }
        }
        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到名称为「" + npcName + "」的NPC！\n§7可输入 /npc list 查看所有NPC名称"));
            return;
        }

        ResourceLocation entityRL = new ResourceLocation(entityRegName);
        if (!EntityList.getEntityNameList().contains(entityRL) || EntityList.getClass(entityRL) == null) {
            sender.sendMessage(new TextComponentString("§c实体注册名「" + entityRegName + "」无效！\n§7常用合法实体注册名：\n  - minecraft:zombie（僵尸）\n  - minecraft:skeleton（骷髅）\n  - minecraft:villager（村民）\n  - minecraft:creeper（苦力怕）\n  - minecraft:spider（蜘蛛）"));
            return;
        }

        try {
            String original_entity_reg_name = targetNPC.getInheritedFrom();

            targetNPC.UpdateHostingEntity(entityRegName);

            sender.sendMessage(new TextComponentString(
                    "§a操作成功！\n" +
                            "§7├─ NPC名称：§f" + npcName + "\n" +
                            "§7├─ 原托管实体：§f" + original_entity_reg_name + " → 新托管实体：§f" + entityRegName + "\n" +
                            "§7└─ 新实体UID：§f" + (targetNPC.getHostingUID() == null ? "未生成" : targetNPC.getHostingUID())
            ));

        } catch (BehaviorInheritanceException e) {
            sender.sendMessage(new TextComponentString("§c更换托管实体失败：" + e.getMessage()));
        } catch (Exception e) {
            sender.sendMessage(new TextComponentString("§c更换托管实体失败：内部错误 → " + e.getMessage() + "\n§7请检查实体注册名是否正确"));
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
        } else if (args.length == 2) {
            List<String> entityNames = new ArrayList<>();
            for (ResourceLocation rl : EntityList.getEntityNameList()) {
                if (rl.toString().toLowerCase().startsWith(args[1].toLowerCase())) {
                    entityNames.add(rl.toString());
                }
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, entityNames);
        }
        return new ArrayList<>();
    }
}