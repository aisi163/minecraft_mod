package com.aisi163.npc_builder;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

public interface ModIProxy {
    void preInit(FMLPreInitializationEvent event);
    void init(FMLInitializationEvent event);
    void postInit(FMLPostInitializationEvent event);
    void serverStarting(FMLServerStartingEvent event);
}