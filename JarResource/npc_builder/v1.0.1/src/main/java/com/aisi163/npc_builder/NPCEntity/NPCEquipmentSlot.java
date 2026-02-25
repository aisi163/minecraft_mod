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

import net.minecraft.inventory.EntityEquipmentSlot;

public enum NPCEquipmentSlot {
    HEAD("头部", EntityEquipmentSlot.HEAD),
    CHEST("身体", EntityEquipmentSlot.CHEST),
    LEGS("腿部", EntityEquipmentSlot.LEGS),
    FEET("脚部", EntityEquipmentSlot.FEET),
    MAINHAND("主手", EntityEquipmentSlot.MAINHAND),
    OFFHAND("附加手（副手）", EntityEquipmentSlot.OFFHAND);

    private final String desc;
    private final EntityEquipmentSlot slot;

    NPCEquipmentSlot(String desc, EntityEquipmentSlot slot) {
        this.desc = desc;
        this.slot = slot;
    }

    public String getDesc() {
        return desc;
    }

    public EntityEquipmentSlot getSlot() {
        return slot;
    }

    public static NPCEquipmentSlot parse(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        try {
            return valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

