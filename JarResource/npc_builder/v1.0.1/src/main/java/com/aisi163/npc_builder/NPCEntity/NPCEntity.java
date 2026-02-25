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

import com.aisi163.npc_builder.NPCBuilder;
import com.aisi163.npc_builder.NPCEntity.exceptions.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCEntity extends EntityArmorStand {
    public static final boolean DebugMode = true;
    ICommandSender debug_sender = null;
    public String debug_callback_str = "";

    public static final String EntityName = "npc_entity";
    public static final String RegisterName = NPCBuilder.MODID + ":" + EntityName;
    public static final int EntityID = 498223;

    public static final HashMap<String, Method> Attributes_list = new HashMap<>();

    public static final String Obj_inherited_from_Attribute = "inherited_from";
    public static final String Obj_posX_Attribute = "posX";
    public static final String Obj_posY_Attribute = "posY";
    public static final String Obj_posZ_Attribute = "posZ";
    public static final String Obj_tag_name_Attribute = "npc_tag";
    public static final String Obj_npc_name_Attribute = "npc_name";
    public static final String Obj_affairs_type_Attribute = "affairs_type";
    public static final String Obj_hosting_entity_equipment_Attribute = "hosting_entity_equipment";
    public static final String Obj_original_hostingUID_Attribute = "original_hosting_entity_uuid";

    public static void DiscoverySetString(NBTTagCompound obj, String key, String value) {
        obj.setString(key, value);
    }

    public static void DiscoverySetDouble(NBTTagCompound obj, String key, double value) {
        obj.setDouble(key, value);
    }

    static {
        try {
            Method set_string = NPCEntity.class.getDeclaredMethod("DiscoverySetString", NBTTagCompound.class, String.class, String.class);
            Method set_double = NPCEntity.class.getDeclaredMethod("DiscoverySetDouble", NBTTagCompound.class, String.class, double.class);
            Attributes_list.put(Obj_inherited_from_Attribute, set_string);
            Attributes_list.put(Obj_posX_Attribute, set_double);
            Attributes_list.put(Obj_posY_Attribute, set_double);
            Attributes_list.put(Obj_posZ_Attribute, set_double);
            Attributes_list.put(Obj_tag_name_Attribute, set_string);
            Attributes_list.put(Obj_npc_name_Attribute, set_string);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String default_inherited_entity = "minecraft:zombie";
    public static final String default_tag_name = "unknown_tag_name";
    public static final double default_entity_posX = 0.0D;
    public static final double default_entity_posY = 120.0D;
    public static final double default_entity_posZ = 0.0D;
    public static final NPCAffairsType default_affair_obj = NPCAffairsType.NONE_AFFAIR;
    public static final String default_affair_type = default_affair_obj.getAffair_type();

    public Entity manager_entity = null;
    private double pos_X = default_entity_posX;
    private double pos_Y = default_entity_posY;
    private double pos_Z = default_entity_posZ;
    private String inherited_from = default_inherited_entity;
    private String tag_name = default_tag_name;
    private UUID hostingUID = null;
    private String npc_name = null;
    private NPCAffairsType affair_obj = default_affair_obj;
    private String affairs_type = default_affair_type;
    private Map<String, Double> affair_double_argument = new HashMap<>();
    private Map<String, String> affair_string_argument = new HashMap<>();
    private Map<String, ItemStack> equipments_map = new HashMap<>();
    private boolean is_swap = false;
    private boolean kill_original_hosting_entity = false;

    private String original_hostingUID = "";

    private void UpdateMyInitializedState(World worldIn) {
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setEntityInvulnerable(true);
        this.setPosition(this.pos_X, this.pos_Y, this.pos_Z);
        this.setCustomNameTag("§c【运维中】无业务");
        this.setAlwaysRenderNameTag(true);
    }

    private void AttributeLoadFromNBT(World worldIn, NBTTagCompound nbt_tag) throws BehaviorInheritanceException, SetTagNameException, SetPositionException {
        if (nbt_tag == null || worldIn == null) return;

        if (nbt_tag.hasKey(Obj_inherited_from_Attribute) && !nbt_tag.getString(Obj_inherited_from_Attribute).isEmpty()) {
            this.inherited_from = nbt_tag.getString(Obj_inherited_from_Attribute);
        }

        if (nbt_tag.hasKey(Obj_posX_Attribute)) {
            this.pos_X = nbt_tag.getDouble(Obj_posX_Attribute);
        }
        if (nbt_tag.hasKey(Obj_posY_Attribute)) {
            this.pos_Y = nbt_tag.getDouble(Obj_posY_Attribute);
        }
        if (nbt_tag.hasKey(Obj_posZ_Attribute)) {
            this.pos_Z = nbt_tag.getDouble(Obj_posZ_Attribute);
        }


        if (nbt_tag.hasKey(Obj_tag_name_Attribute)) {
            tag_name = nbt_tag.getString(Obj_tag_name_Attribute);
        }

        if (nbt_tag.hasKey(Obj_npc_name_Attribute)) {
            npc_name = nbt_tag.getString(Obj_npc_name_Attribute);
            if (this.CheckMyNameConflictWithOthers(worldIn)) {
                this.npc_name = npc_name + ".name_traffic:" + UUID.randomUUID();
            }
        }

        if (nbt_tag.hasKey(Obj_original_hostingUID_Attribute)){
            this.original_hostingUID = nbt_tag.getString(Obj_original_hostingUID_Attribute);
        }

        if (nbt_tag.hasKey(Obj_affairs_type_Attribute)){
            affairs_type = nbt_tag.getString(Obj_affairs_type_Attribute);
            affair_obj = affair_obj.change_to(affairs_type);
            if (NPCAffairsType.COMMAND_EXECUTE_AFFAIR.same_affair(this.affair_obj)){
                affair_obj.push_NBT_to_affair_argument_string_map(this.affair_string_argument, nbt_tag);
            } else {
                affair_obj.push_NBT_to_affair_argument_double_map(this.affair_double_argument, nbt_tag);
            }
        };

        equipments_map = new HashMap<>();
        if (nbt_tag.hasKey(Obj_hosting_entity_equipment_Attribute)) {
            NBTTagCompound equipmentTag = nbt_tag.getCompoundTag(Obj_hosting_entity_equipment_Attribute);
            if (!worldIn.isRemote) {
                for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                    if (equipmentTag.hasKey(slot.getName())) {
                        NBTTagCompound stackTag = equipmentTag.getCompoundTag(slot.getName());
                        ItemStack stack = new ItemStack(stackTag);
                        equipments_map.put(slot.getName(), stack);
                    }
                }
            }
        }

        this.manager_entity = this.BuildHostingEntity(this.inherited_from, worldIn);
        this.SetPos(this.pos_X, this.pos_Y, this.pos_Z);
        this.SetTagName(this.tag_name);
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
             if (equipments_map.containsKey(slot.getName())){
                 this.manager_entity.setItemStackToSlot(slot, equipments_map.get(slot.getName()));
             }
        }
    }

    private Object getAttributeValue(String attributeName) throws NoSuchFieldException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(attributeName);
        field.setAccessible(true);
        return field.get(this);
    }

    public void AttributesPushToEntity(NBTTagCompound nbt_tag) {
        if (nbt_tag == null) return;

        nbt_tag.setString(Obj_inherited_from_Attribute, this.inherited_from);
        nbt_tag.setDouble(Obj_posX_Attribute,this.pos_X);
        nbt_tag.setDouble(Obj_posY_Attribute,this.pos_Y);
        nbt_tag.setDouble(Obj_posZ_Attribute,this.pos_Z);
        nbt_tag.setString(Obj_tag_name_Attribute, this.tag_name);

        if (this.npc_name == null) {
            nbt_tag.setString(Obj_npc_name_Attribute, "unknown_npc_name:" + UUID.randomUUID());
        } else {
            nbt_tag.setString(Obj_npc_name_Attribute, this.npc_name);
        }

        nbt_tag.setString(Obj_affairs_type_Attribute, this.affairs_type);
        if (this.affair_obj.same_affair(NPCAffairsType.COMMAND_EXECUTE_AFFAIR)){
            this.affair_obj.push_affair_string_argument_map_to_NBT(this.affair_string_argument, nbt_tag);
        } else {
            this.affair_obj.push_affair_double_argument_map_to_NBT(this.affair_double_argument, nbt_tag);
        }

        NBTTagCompound equipmentTag = new NBTTagCompound();
        if (this.manager_entity != null && this.manager_entity instanceof EntityLivingBase) {
            EntityLivingBase livingEntity = (EntityLivingBase) this.manager_entity;
            for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                ItemStack stack = livingEntity.getItemStackFromSlot(slot);
                if (!stack.isEmpty()) {
                    equipmentTag.setTag(slot.getName(), stack.writeToNBT(new NBTTagCompound()));
                }
            }
        }
        nbt_tag.setTag(Obj_hosting_entity_equipment_Attribute, equipmentTag);

        if (this.hostingUID != null) {
            nbt_tag.setString(Obj_original_hostingUID_Attribute, this.hostingUID.toString());
        }
    }

    private void AttributePushToEntity(NBTTagCompound nbt_tag, String attribute_name) throws InvocationTargetException,
            IllegalAccessException, NoSuchFieldException, AttributeNotExistException {
        if (Attributes_list.containsKey(attribute_name)) {
            Attributes_list.get(attribute_name).invoke(
                    null, nbt_tag, attribute_name, this.getAttributeValue(attribute_name));
        } else {
            throw new AttributeNotExistException(attribute_name);
        }
    }

    private void SetHostingEntityAttribute(Entity HostingEntity) throws SetPositionException, SetTagNameException {
        if (HostingEntity == null) return;

        if (HostingEntity instanceof EntityCreature) {
            ((EntityCreature) HostingEntity).setNoAI(true);
            HostingEntity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET,1));
        }

        HostingEntity.setNoGravity(true);
        HostingEntity.setEntityInvulnerable(true);
    }

    private void UpdateHostingEntityInitializedState(Entity HostingEntity, World worldIn) throws SetPositionException, SetTagNameException {
        if (HostingEntity == null || worldIn == null || worldIn.isRemote) return;
        this.SetHostingEntityAttribute(HostingEntity);
    }

    private void KillOriginalEntity(World worldIn) {
        if (worldIn.isRemote || this.original_hostingUID == null) return;
        for (Entity entity : worldIn.loadedEntityList.toArray(new Entity[0])) {
            if (entity.getPersistentID().toString().equals(original_hostingUID) && !entity.isDead) {
                entity.setDead();
                worldIn.removeEntity(entity);
                break;
            }
        }
    };

    public Entity BuildHostingEntity(String inherited_from, World worldIn) throws BehaviorInheritanceException, SetTagNameException, SetPositionException {
        if (worldIn == null || worldIn.isRemote || inherited_from == null) return null;

        Entity newHostingEntity = EntityList.createEntityByIDFromName(new ResourceLocation(inherited_from), worldIn);
        if (newHostingEntity == null) {
            throw new BehaviorInheritanceException(
                    BehaviorInheritanceExceptionEnum.HOSTING_ENTITY_ERROR,
                    inherited_from
            );
        }
        this.UpdateHostingEntityInitializedState(newHostingEntity, worldIn);
        this.hostingUID = newHostingEntity.getPersistentID();
        return newHostingEntity;
    }

    private boolean CheckMyNameConflictWithOthers(World worldIn) {
        if (worldIn == null || this.npc_name == null || this.npc_name.isEmpty()) {
            return false;
        }

        for (Entity entity : worldIn.loadedEntityList) {
            if (entity instanceof NPCEntity && entity != this) {
                NPCEntity otherNpc = (NPCEntity) entity;
                if (otherNpc.npc_name != null && otherNpc.npc_name.equals(this.npc_name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public NPCEntity(World worldIn) {
        super(worldIn);
    }

    public NPCEntity(World worldIn, String npc_name, String inherit_entity_reg_name) throws BehaviorInheritanceException,
            SetPositionException, NameConflictException, SetTagNameException {
        super(worldIn);
        this.is_swap = true;
        if (inherit_entity_reg_name == null) throw new IllegalArgumentException("参数不能为空");

        this.UpdateMyInitializedState(worldIn);
        inherit_entity_reg_name = !inherit_entity_reg_name.isEmpty() ? inherit_entity_reg_name : default_inherited_entity;
        this.setBehaviorInheritance(inherit_entity_reg_name);

        this.manager_entity = this.BuildHostingEntity(inherit_entity_reg_name, worldIn);

        if (this.manager_entity != null && !worldIn.isRemote) {
            this.UpdateHostingEntityInitializedState(this.manager_entity, worldIn);
        }

        this.npc_name = npc_name;
        if (this.CheckMyNameConflictWithOthers(worldIn)) {
            throw new NameConflictException(this.npc_name);
        }
    }

    // Debug Only
    public NPCEntity(World worldIn, String npc_name, String inherit_entity_reg_name, ICommandSender sender) throws BehaviorInheritanceException,
            SetPositionException, NameConflictException, DebugModeNoOpenException, SetTagNameException {
        super(worldIn);
        this.is_swap = true;
        if (DebugMode) {
            debug_sender = sender;
            this.UpdateMyInitializedState(worldIn);
            inherit_entity_reg_name = (inherit_entity_reg_name != null && !inherit_entity_reg_name.isEmpty()) ? inherit_entity_reg_name : default_inherited_entity;
            this.setBehaviorInheritance(inherit_entity_reg_name);
            this.manager_entity = this.BuildHostingEntity(inherit_entity_reg_name, worldIn);

            if (this.manager_entity != null && !worldIn.isRemote) {
                this.UpdateHostingEntityInitializedState(this.manager_entity, worldIn);
            }

            this.npc_name = npc_name;
            if (this.CheckMyNameConflictWithOthers(worldIn)) {
                throw new NameConflictException(this.npc_name);
            }
        } else {
            throw new DebugModeNoOpenException();
        }
    }

    public void setBehaviorInheritance(String inherit_entity_reg_name) throws BehaviorInheritanceException {
        if (inherit_entity_reg_name == null || inherit_entity_reg_name.equals(RegisterName)) {
            throw new BehaviorInheritanceException(
                    BehaviorInheritanceExceptionEnum.REGISTER_NAME_TRAFFIC,
                    inherit_entity_reg_name == null ? "null" : inherit_entity_reg_name
            );
        }

        ResourceLocation resourceLocation;
        Class<? extends Entity> entityClass;

        try {
            resourceLocation = new ResourceLocation(inherit_entity_reg_name);
            entityClass = EntityList.getClass(resourceLocation);
        }
        catch (Exception e) {
            throw new BehaviorInheritanceException(
                    BehaviorInheritanceExceptionEnum.REGISTER_NAME_FORMAT_ERROR,
                    inherit_entity_reg_name);
        }

        if (entityClass == null) {
            throw new BehaviorInheritanceException(BehaviorInheritanceExceptionEnum.UNKNOWN_REGISTER_NAME,
                    resourceLocation.toString());
        }

        if (!Entity.class.isAssignableFrom(entityClass)) {
            throw new BehaviorInheritanceException(BehaviorInheritanceExceptionEnum.NO_ENTITY,
                    resourceLocation.toString());
        }
        this.inherited_from = inherit_entity_reg_name;
    }

    public void SetPos(double x, double y, double z) throws SetPositionException {
        try {
            this.pos_X = x;
            this.pos_Y = y;
            this.pos_Z = z;
            double tagOffset = -(this.height - this.manager_entity.height) + 0.25D;
            double npcY = this.pos_Y + tagOffset;
            this.setPosition(this.pos_X, npcY, this.pos_Z);
            if (this.manager_entity != null) {
                this.manager_entity.setPosition(this.pos_X, this.pos_Y, this.pos_Z);
            }
        } catch (Exception e) {
            throw new SetPositionException(x, y, z, e);
        }
    }

    public void SetTagName(String tag_name) throws SetTagNameException {
        try {
            this.tag_name = tag_name;
            if (this.manager_entity != null) {
                this.manager_entity.setCustomNameTag(tag_name);
                this.manager_entity.setAlwaysRenderNameTag(true);
            }
        } catch (Exception e){
            throw new SetTagNameException(tag_name,e);
        }
    }

    public void SetNPCName(String new_name){
        this.npc_name = new_name;
    }

    public void UpdateHostingEntity(String inherit_entity_reg_name) throws BehaviorInheritanceException, SetPositionException, SetTagNameException {
        Map<String, ItemStack> original_items = new HashMap<>();

        if (this.manager_entity != null) {
            if (this.manager_entity instanceof EntityLivingBase) {
                EntityLivingBase livingEntity = (EntityLivingBase) this.manager_entity;
                for (NPCEquipmentSlot slot : NPCEquipmentSlot.values()) {
                    ItemStack stack = livingEntity.getItemStackFromSlot(slot.getSlot());
                    original_items.put(slot.getSlot().getName(), stack.copy());
                }
            }
            if (!this.manager_entity.isDead) {
                this.manager_entity.setDead();
            }
        }

        this.setBehaviorInheritance(inherit_entity_reg_name);
        this.manager_entity = this.BuildHostingEntity(this.inherited_from, this.world);
        this.SetHostingEntityAttribute(this.manager_entity);
        this.SetPos(this.pos_X, this.pos_Y, this.pos_Z);
        this.SetTagName(this.getTagName());

        if (this.manager_entity instanceof EntityLivingBase) {
            EntityLivingBase livingEntity = (EntityLivingBase) this.manager_entity;
            for (NPCEquipmentSlot slot : NPCEquipmentSlot.values()) {
                if (original_items.containsKey(slot.getSlot().getName())) {
                    livingEntity.setItemStackToSlot(slot.getSlot(),
                            original_items.get(slot.getSlot().getName()).copy());
                }
            }
        }

        if (!this.manager_entity.isDead && !this.world.loadedEntityList.contains(this.manager_entity)) {
            world.spawnEntity(this.manager_entity);
        }
    }

    public Entity getManagerEntity() throws SetTagNameException, BehaviorInheritanceException, SetPositionException {
        return this.manager_entity;
    }
    public double getPosX() { return this.pos_X; }
    public double getPosY() { return this.pos_Y; }
    public double getPosZ() { return this.pos_Z; }
    public String getInheritedFrom() { return this.inherited_from; }
    public String getTagName() { return this.tag_name; }
    public UUID getHostingUID() { return this.hostingUID; }
    public String getNpcName() { return this.npc_name; }
    public NPCAffairsType getCurrentAffairType() { return this.affair_obj; }
    public String getAffairsInfo() {
        if (this.affair_obj.equals(NPCAffairsType.NONE_AFFAIR)){
            return "[没有绑定任何业务]";
        } else {
            if (this.affair_obj.equals(NPCAffairsType.COMMAND_EXECUTE_AFFAIR)) {
                return this.affair_obj.get_affair_string_info(this.affair_string_argument);
            } else {
                return this.affair_obj.get_affair_double_info(this.affair_double_argument);
            }
        }
    }
    public Map<String, ItemStack> getEquipments() {
        return this.equipments_map;
    }

    public String getHostingEntityEquipmentInfo() {
        StringBuilder equipmentInfo = new StringBuilder();

        try {
            Entity hostingEntity = this.getManagerEntity();

            if (hostingEntity == null) {
                equipmentInfo.append("§c暂无托管实体");
                return equipmentInfo.toString();
            }

            if (!(hostingEntity instanceof EntityLivingBase)) {
                equipmentInfo.append("§c托管实体非活物（无装备槽）");
                return equipmentInfo.toString();
            }

            EntityLivingBase livingEntity = (EntityLivingBase) hostingEntity;
            for (NPCEquipmentSlot slot : NPCEquipmentSlot.values()) {
                ItemStack stack = livingEntity.getItemStackFromSlot(slot.getSlot());
                String itemDesc = stack.isEmpty()
                        ? "无"
                        : stack.getItem().getRegistryName().toString() + " x" + stack.getCount();

                equipmentInfo.append("§7- ").append(slot.getDesc()).append("：§f").append(itemDesc).append("\n");
            }

            if (equipmentInfo.length() > 0) {
                equipmentInfo.deleteCharAt(equipmentInfo.length() - 1);
            }
        } catch (SetTagNameException | BehaviorInheritanceException | SetPositionException e) {
            equipmentInfo.append("§c获取装备信息失败：").append(e.getMessage());
        }

        return equipmentInfo.toString();
    }

    public boolean setHostingEntityEquipment(NPCEquipmentSlot targetSlot, ItemStack itemStack) throws SetTagNameException, BehaviorInheritanceException, SetPositionException {
        if (targetSlot == null || itemStack == null) {
            return false;
        }

        Entity hostingEntity = this.getManagerEntity();

        if (!(hostingEntity instanceof EntityLivingBase)) {
            return false;
        }

        EntityLivingBase livingEntity = (EntityLivingBase) hostingEntity;
        livingEntity.setItemStackToSlot(targetSlot.getSlot(), itemStack);

        return true;
    }


    public void clearAffairs() {
        if (this.affair_obj.same_affair(NPCAffairsType.COMMAND_EXECUTE_AFFAIR)){
            this.affair_string_argument.clear();
        } else {
            this.affair_double_argument.clear();
        }
        this.affair_obj = default_affair_obj;
        this.affairs_type = this.affair_obj.getAffair_type();
    }


    public boolean bindStringAffairs(NPCAffairsType npcAffairsType, Map<String, String> params) {
        if (this.affair_obj.same_affair(npcAffairsType)){
            return false;
        }
        NPCAffairsType new_type = this.affair_obj.change_to(npcAffairsType.getAffair_type());
        String check_callback = new_type.check_string_argument_map(params);
        if (!check_callback.isEmpty()){
            return false;
        }

        this.affair_obj = new_type;
        this.affairs_type = this.affair_obj.getAffair_type();
        this.affair_string_argument = params;
        this.setCustomNameTag("§a【命令执行】点击 NPC 执行命令");
        return true;
    }

    public boolean bindDoubleAffairs(NPCAffairsType npcAffairsType, Map<String, Double> params) {
        if (this.affair_obj.same_affair(npcAffairsType)){
            return false;
        }
        NPCAffairsType new_type = this.affair_obj.change_to(npcAffairsType.getAffair_type());
        String check_callback = new_type.check_double_argument_map(params);
        if (!check_callback.isEmpty()){
            return false;
        }
        this.affair_obj = new_type;
        this.affairs_type = this.affair_obj.getAffair_type();
        this.affair_double_argument = params;
        if (this.affair_obj.same_affair(NPCAffairsType.POSITION_CHANGE_AFFAIR)) {
            Double x = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "position_change_x");
            Double y = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "position_change_y");
            Double z = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "position_change_z");
            this.setCustomNameTag("§a【传送至】当前维度 (" + x + "," + y + "," + z + ")");
        } else if (this.affair_obj.same_affair(NPCAffairsType.DIMENSION_CHANGE_AFFAIR)){
            Double dimension_code = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "dimension_code");
            Double x = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "dimension_change_x");
            Double y = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "dimension_change_y");
            Double z = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "dimension_change_z");
            String dimension_name = "";
            switch ((int)dimension_code.doubleValue()){
                case 1: dimension_name = "下界"; break;
                case 2: dimension_name = "末地"; break;
                default: dimension_name = "主世界"; break;
            }
            this.setCustomNameTag("§a【传送至】" + dimension_name + " (" + x + "," + y + "," + z + ")");
        }

        return true;
    }

    public void DoAffairs(EntityPlayer who_triggered){
        if (this.affair_obj.same_affair(NPCAffairsType.COMMAND_EXECUTE_AFFAIR)) {
            String check_callback = this.affair_obj.check_string_argument_map(this.affair_string_argument);
            if (check_callback.isEmpty()) {
                who_triggered.sendMessage(new TextComponentString("[系统提示] §9欢迎,我是 NPC " + this.getNpcName() + " 很高兴为您服务"));
                who_triggered.sendMessage(new TextComponentString("[系统提示] 执行命令..."));
                String command = this.affair_obj.get_affair_string_argument_value(this.affair_string_argument,"command_str");
                who_triggered.getServer().commandManager.executeCommand(who_triggered, "/"+ command);
                who_triggered.sendMessage(new TextComponentString("[系统提示] 命令执行完毕"));
                return;
            }
        } else {
            String check_callback = this.affair_obj.check_double_argument_map(this.affair_double_argument);
            if (check_callback.isEmpty()) {
                if (!this.affair_obj.same_affair(NPCAffairsType.NONE_AFFAIR)) {
                    who_triggered.sendMessage(new TextComponentString("[系统提示] §9欢迎,我是 NPC " + this.getNpcName() + " 很高兴为您服务"));
                    who_triggered.sendMessage(new TextComponentString("[系统提示]" + MinecraftContentManager.getRandomContent()));

                    if (this.affair_obj.same_affair(NPCAffairsType.DIMENSION_CHANGE_AFFAIR)) {
                        Double dimension_code = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "dimension_code");
                        Double x = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "dimension_change_x");
                        Double y = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "dimension_change_y");
                        Double z = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "dimension_change_z");

                        String dimension_name = "";
                        int game_dimension_code = 0;
                        switch ((int) dimension_code.doubleValue()) {
                            case 1:
                                dimension_name = "下界";
                                game_dimension_code = -1;
                                break;
                            case 2:
                                dimension_name = "末地";
                                game_dimension_code = 1;
                                break;
                            default:
                                dimension_name = "主世界";
                                break;
                        }
                        who_triggered.sendMessage(new TextComponentString("[系统提示] §a我们即将到达: " + dimension_name + ", 坐标: (" + x + "," + y + "," + z + ")"));
                        if (!who_triggered.getServer().getWorld(game_dimension_code).equals(who_triggered.world)) {
                            int finalGame_dimension_code = game_dimension_code;
                            who_triggered.getServer().addScheduledTask(() -> {
                                who_triggered.getServer().commandManager.executeCommand(this, "/forge setdim " + who_triggered.getName() + " " + finalGame_dimension_code + " " + x + " " + y + " " + z);
                            });
                        } else {
                            who_triggered.getServer().commandManager.executeCommand(this, "/tp " + who_triggered.getName() + " " + x + " " + y + " " + z);
                        }
                        return;
                    } else if (this.affair_obj.same_affair(NPCAffairsType.POSITION_CHANGE_AFFAIR)) {
                        Double x = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "position_change_x");
                        Double y = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "position_change_y");
                        Double z = this.affair_obj.get_affair_double_argument_value(this.affair_double_argument, "position_change_z");
                        who_triggered.sendMessage(new TextComponentString("[系统提示] §a我们即将到达 坐标: (" + x + "," + y + "," + z + ")"));
                        who_triggered.getServer().commandManager.executeCommand(this, "/tp " + who_triggered.getName() + " " + x + " " + y + " " + z);
                        return;
                    }
                } else {
                    who_triggered.sendMessage(new TextComponentString("[系统提示] §c抱歉,管理员并未设置此NPC的业务,无法为您提供服务"));
                    return;
                }
            }
        }
        who_triggered.sendMessage(new TextComponentString("§c哦吼,代码出错了"));
        who_triggered.sendMessage(new TextComponentString("§c很抱歉,此NPC无法为您服务,请通知服务器管理员来修复"));
        this.setCustomNameTag("§c【运维中】NPC业务损坏,请重新设置业务");
        this.setAlwaysRenderNameTag(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!kill_original_hosting_entity){
            KillOriginalEntity(world);
            kill_original_hosting_entity = true;
        }
        if (!is_swap && this.manager_entity != null) {
            this.world.spawnEntity(this.manager_entity);
            is_swap = true;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt_tag) {
        super.readFromNBT(nbt_tag);
        try {
            this.AttributeLoadFromNBT(this.getEntityWorld(), nbt_tag);
        } catch (BehaviorInheritanceException | SetTagNameException | SetPositionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt_tag) {
        nbt_tag = super.writeToNBT(nbt_tag);
        this.AttributesPushToEntity(nbt_tag);
        return nbt_tag;
    }
}