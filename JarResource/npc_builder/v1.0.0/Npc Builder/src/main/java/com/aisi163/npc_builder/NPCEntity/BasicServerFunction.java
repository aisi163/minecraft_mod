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

package com.aisi163.npc_builder.NPCEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;



@Mod.EventBusSubscriber()
public class BasicServerFunction {

    @SubscribeEvent
    public static void onEntityAttacked(AttackEntityEvent event) {
        EntityPlayer attacker = event.getEntityPlayer();
        Entity attackedEntity = event.getTarget();
        if (attacker == null || attackedEntity == null) {
            return;
        }

        UUID attacked_entity_uid = attackedEntity.getPersistentID();
        for (Entity entity : attacker.world.loadedEntityList) {
            if (entity instanceof NPCEntity) {
                NPCEntity targetNpc = (NPCEntity) entity;
                if (attacked_entity_uid.equals(targetNpc.getHostingUID())) {
                    event.setCanceled(true);
                    targetNpc.DoAffairs(attacker);
                    break;
                };
            }
        }
    }
}