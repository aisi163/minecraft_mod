package com.aisi163.npc_builder;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import com.aisi163.npc_builder.NPCEntity.command.CommandNPC;

public class ServerProxy implements ModIProxy {

    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("[Physical Server] preInit");
        MinecraftForge.EVENT_BUS.register(new BasicServerFunction());
        MinecraftForge.EVENT_BUS.register(new NPCRegister());
    }

    public void init(FMLInitializationEvent event) {
        System.out.println("[Physical Server] Init");
    }

    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("[Physical Server] PostInit");
    }

    public void serverStarting(FMLServerStartingEvent event) {
        System.out.print("[Physical Server] FMLServerStartingEvent event");
        event.registerServerCommand(new CommandNPC());
    }
}
