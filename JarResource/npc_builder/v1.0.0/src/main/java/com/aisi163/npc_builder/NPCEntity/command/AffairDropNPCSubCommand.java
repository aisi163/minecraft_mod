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
import java.util.List;

public class AffairDropNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "drop_affair";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc drop_affair [NPC名称] - 删除指定NPC的当前绑定业务（恢复无业务状态）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (world.isRemote) {
            throw new CommandException("§c错误：该命令仅能在服务端执行！");
        }

        if (args.length < 1) {
            sender.sendMessage(new TextComponentString("§c参数不足！用法：" + getUsage(sender) + "\n§7示例：/npc affairs_drop 村庄守卫NPC"));
            return;
        }

        String npcName = args[0];

        NPCEntity targetNPC = findNPCByName(world, npcName);
        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到名称为「" + npcName + "」的NPC！\n§7可输入 /npc list 查看所有NPC名称"));
            return;
        }


        if (targetNPC.getCurrentAffairType().same_affair(NPCAffairsType.NONE_AFFAIR)) {
            sender.sendMessage(new TextComponentString("§cNPC「" + npcName + "」当前无绑定业务，无需删除！"));
            return;
        }
        String oldAffairsDesc = targetNPC.getCurrentAffairType().getAffair_description();
        targetNPC.clearAffairs();
        targetNPC.setCustomNameTag("§c【运维中】无业务");
       sender.sendMessage(new TextComponentString(
                "§a删除成功！\n" +
                        "§7├─ NPC名称：§f" + npcName + "\n" +
                        "§7├─ 已删除业务：§f" + oldAffairsDesc + "\n" +
                        "§7└─ 当前状态：§f无绑定业务（可重新绑定新业务）"
        ));
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
        }
        return new ArrayList<>();
    }
}