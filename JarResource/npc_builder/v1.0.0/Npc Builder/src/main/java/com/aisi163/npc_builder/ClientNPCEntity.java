package com.aisi163.npc_builder;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientNPCEntity extends EntityArmorStand {
    public static final String EntityName = "npc_entity";
    public static final String RegisterName = NPCBuilder.MODID + ":" + EntityName;
    public static final int EntityID = 498223;

    @SideOnly(Side.CLIENT)
    public ClientNPCEntity(World world) {
        super(world);
        this.setNoGravity(true);
        this.setInvisible(true);
    };

}