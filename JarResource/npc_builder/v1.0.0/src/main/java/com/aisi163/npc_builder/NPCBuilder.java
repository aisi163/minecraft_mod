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

package com.aisi163.npc_builder;

import com.aisi163.npc_builder.NPCEntity.NPCEntity;
import com.aisi163.npc_builder.NPCEntity.command.CommandNPC;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod(modid = NPCBuilder.MODID, name = NPCBuilder.NAME, version = NPCBuilder.VERSION)
@Mod.EventBusSubscriber(modid = NPCBuilder.MODID)
public class NPCBuilder
{
    public static final String MODID = "npc_builder";
    public static final String NAME = "NPC Builder";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {}

    @EventHandler
    public void init(FMLInitializationEvent event) {}

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandNPC());
    }

    @SubscribeEvent
    public static void EntityRegister(RegistryEvent.Register<EntityEntry> entity_register){
        EntityEntry npcEntry = null;

        if (isLogicalServer() || isPureClient()) {
            npcEntry = EntityEntryBuilder.create()
                    .entity(NPCEntity.class)
                    .id(new ResourceLocation(NPCBuilder.MODID, "server_npc"), NPCEntity.EntityID)
                    .name(NPCEntity.EntityName)
                    .tracker(64, 1, true)
                    .build();
        }
        else {
            npcEntry = EntityEntryBuilder.create()
                    .entity(ClientNPCEntity.class)
                    .id(new ResourceLocation(NPCBuilder.MODID, "client_npc"), ClientNPCEntity.EntityID)
                    .name(ClientNPCEntity.EntityName)
                    .tracker(64, 1, true)
                    .build();
        }
        entity_register.getRegistry().register(npcEntry);
    }

    private static boolean isLogicalServer() {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            return true;
        }
        return FMLCommonHandler.instance().getMinecraftServerInstance() != null;
    }

    private static boolean isPureClient() {
        return FMLCommonHandler.instance().getSide().isClient()
                && FMLCommonHandler.instance().getMinecraftServerInstance() == null;
    }
}