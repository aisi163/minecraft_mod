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
import com.aisi163.npc_builder.NPCEntity.NPCEquipmentSlot;
import com.aisi163.npc_builder.NPCEntity.exceptions.BehaviorInheritanceException;
import com.aisi163.npc_builder.NPCEntity.exceptions.SetPositionException;
import com.aisi163.npc_builder.NPCEntity.exceptions.SetTagNameException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EquipmentSetNPCSubCommand implements INPCSubCommand {
    @Override
    public String getName() {
        return "equipment";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/npc equipment set [NPC名称] [部位] [物品ID] [数量] - 设置NPC托管实体的装备/手持物\n" +
                "  部位可选：head(头部)、chest(胸部)、legs(腿部)、feet(脚部)、mainhand(主手)、offhand(附加手)\n" +
                "  物品ID示例：minecraft:diamond_helmet（钻石头盔）、minecraft:iron_sword（铁剑）、minecraft:bow（弓）\n" +
                "  注意: 使用 /npc equipment set [NPC名称] [部位] minecraft:air 1 可以将 NPC 在 [部位] 上的物品清空";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, World world, String[] args) throws CommandException {
        if (world.isRemote) {
            sender.sendMessage(new TextComponentString("§c错误：该命令仅能在服务端执行！"));
            return;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("set")) {
            sender.sendMessage(new TextComponentString("§c无效子命令！仅支持 set，用法："));
            return;
        }

        String[] setArgs = new String[args.length - 1];
        System.arraycopy(args, 1, setArgs, 0, args.length - 1);

        if (setArgs.length < 3) {
            sender.sendMessage(new TextComponentString("§c参数不足！用法：" + getUsage(sender) + "\n§7示例：/npc equipment set 村庄守卫NPC mainhand minecraft:iron_sword 1"));
            return;
        }

        String npcName = setArgs[0];
        String slotStr = setArgs[1].toLowerCase();
        String itemId = setArgs[2];
        int count = 1;

        if (setArgs.length >= 4) {
            try {
                count = Integer.parseInt(setArgs[3]);
                if (count < 1 || count > 64) {
                    sender.sendMessage(new TextComponentString("§c数量无效！必须是1~64之间的整数"));
                    return;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(new TextComponentString("§c数量格式错误！必须是整数（如1、64）"));
                return;
            }
        }

        NPCEquipmentSlot targetSlot = NPCEquipmentSlot.parse(slotStr);
        if (targetSlot == null) {
            sender.sendMessage(new TextComponentString("§c部位无效！可选部位：\n" +
                    "  §f- head（头部）、chest（胸部）、legs（腿部）、feet（脚部）\n" +
                    "  §f- mainhand（主手）、offhand（附加手）"));
            return;
        }

        ResourceLocation itemRL = new ResourceLocation(itemId);
        if (!ForgeRegistries.ITEMS.containsKey(itemRL)) {
            sender.sendMessage(new TextComponentString("§c物品ID「" + itemId + "」无效！常用物品ID示例：\n" +
                    "  §f- 头盔：minecraft:diamond_helmet（钻石头盔）、minecraft:golden_helmet（金头盔）\n" +
                    "  §f- 胸甲：minecraft:iron_chestplate（铁胸甲）、minecraft:netherite_chestplate（下界合金胸甲）\n" +
                    "  §f- 武器：minecraft:diamond_sword（钻石剑）、minecraft:bow（弓）、minecraft:trident（三叉戟）\n" +
                    "  §f- 工具：minecraft:diamond_pickaxe（钻石镐）、minecraft:iron_axe（铁斧）"));
            return;
        }
        Item targetItem = ForgeRegistries.ITEMS.getValue(itemRL);
        ItemStack itemStack = new ItemStack(targetItem, count);

        NPCEntity targetNPC = findNPCByName(world, npcName);
        if (targetNPC == null) {
            sender.sendMessage(new TextComponentString("§c未找到名称为「" + npcName + "」的NPC！\n§7可输入 /npc list 查看所有NPC名称"));
            return;
        }

        try {
            if (targetNPC.getManagerEntity() == null || !(targetNPC.getManagerEntity() instanceof net.minecraft.entity.EntityLivingBase)) {
                sender.sendMessage(new TextComponentString("§cNPC「" + npcName + "」的托管实体非活物（如盔甲架），无装备槽！"));
            }
        } catch (SetTagNameException | SetPositionException e) {
            sender.sendMessage(new TextComponentString("§当前查询的NPC(名称: " + targetNPC.getNpcName() + ") 托管实体损坏,我们尝试修复, 但: " + e.getMessage()));
            return;
        } catch (BehaviorInheritanceException e) {
            sender.sendMessage(new TextComponentString("§当前查询的NPC(名称: " + targetNPC.getNpcName() + ") 托管实体损坏,我们尝试修复, 但: " + e.toUserFriendlyMessage()));
            return;
        }

        boolean setSuccess = false;
        try {
            setSuccess = targetNPC.setHostingEntityEquipment(targetSlot, itemStack);
        } catch (SetTagNameException | SetPositionException e) {
            sender.sendMessage(new TextComponentString("§当前查询的NPC(名称: " + targetNPC.getNpcName() + ") 托管实体损坏,我们尝试修复, 但: " + e.getMessage()));
            return;
        } catch (BehaviorInheritanceException e) {
            sender.sendMessage(new TextComponentString("§当前查询的NPC(名称: " + targetNPC.getNpcName() + ") 托管实体损坏,我们尝试修复, 但: " + e.toUserFriendlyMessage()));
            return;
        }
        if (setSuccess) {
            sender.sendMessage(new TextComponentString(
                    "§a设置成功！\n" +
                            "§7├─ NPC名称：§f" + npcName + "\n" +
                            "§7├─ 托管实体：§f" + targetNPC.getInheritedFrom() + "\n" +
                            "§7├─ 设置部位：§f" + targetSlot.getDesc() + "\n" +
                            "§7├─ 物品信息：§f" + itemRL + " x" + count + "\n" +
                            "§7└─ 当前装备预览：\n" + targetNPC.getHostingEntityEquipmentInfo()
            ));
        } else {
            throw new CommandException("§c装备设置失败！请检查托管实体是否为活物（如僵尸、村民、骷髅等）");
        }
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
            List<String> subCmds = new ArrayList<>();
            subCmds.add("set");
            return CommandBase.getListOfStringsMatchingLastWord(args, subCmds);
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            List<String> npcNames = new ArrayList<>();
            for (net.minecraft.entity.Entity entity : sender.getEntityWorld().loadedEntityList) {
                if (entity instanceof NPCEntity && ((NPCEntity) entity).getNpcName() != null) {
                    npcNames.add(((NPCEntity) entity).getNpcName());
                }
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, npcNames);
        }

        else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            List<String> slots = new ArrayList<>();
            slots.add("head");
            slots.add("chest");
            slots.add("legs");
            slots.add("feet");
            slots.add("mainhand");
            slots.add("offhand");
            return CommandBase.getListOfStringsMatchingLastWord(args, slots);
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
                List<String> commonItems = new ArrayList<>();
                Set<ResourceLocation> keys = Item.REGISTRY.getKeys();
                for (ResourceLocation rl : keys) {
                if (rl.toString().toLowerCase().startsWith(args[3].toLowerCase())) {
                    commonItems.add(rl.toString());
                }
            };
            return CommandBase.getListOfStringsMatchingLastWord(args, commonItems);

        }
        else if (args.length == 5 && args[0].equalsIgnoreCase("set")) {
            List<String> counts = new ArrayList<>();
            counts.add("1");
            return CommandBase.getListOfStringsMatchingLastWord(args, counts);
        }
        return new ArrayList<>();
    }
}