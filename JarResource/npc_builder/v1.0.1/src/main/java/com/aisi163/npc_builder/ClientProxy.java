package com.aisi163.npc_builder;

import com.aisi163.npc_builder.NPCEntity.command.CommandNPC;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClientProxy implements ModIProxy {

    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("[Physical Client] preInit");
        MinecraftForge.EVENT_BUS.register(new BasicServerFunction());
        MinecraftForge.EVENT_BUS.register(new NPCRegister());
    }

    public void init(FMLInitializationEvent event) {
        System.out.println("[Physical Client] Init");
    }

    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("[Physical Client] PostInit");
    }

    public void serverStarting(FMLServerStartingEvent event) {
        System.out.print("[Physical Client] FMLServerStartingEvent event");
        event.registerServerCommand(new CommandNPC());
    }

}
