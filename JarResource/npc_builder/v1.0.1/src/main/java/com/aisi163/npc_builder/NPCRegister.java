package com.aisi163.npc_builder;

import com.aisi163.npc_builder.NPCEntity.NPCEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class NPCRegister {

    NPCRegister() {
        super();
    };

    @SubscribeEvent
    public void EntityRegister(RegistryEvent.Register<EntityEntry> event) {
        EntityEntry entityEntry = EntityEntryBuilder.create()
                    .id(new ResourceLocation(NPCEntity.RegisterName),
                            NPCEntity.EntityID)
                    .name(NPCEntity.RegisterName)
                    .entity(NPCEntity.class)
                    .tracker(64, 1, true)
                    .build();
            event.getRegistry().register(entityEntry);
    }
}
