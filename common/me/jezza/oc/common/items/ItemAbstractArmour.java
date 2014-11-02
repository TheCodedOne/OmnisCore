package me.jezza.oc.common.items;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ItemAbstractArmour extends ItemArmor {
    private String textureLocation;
    private int slot;

    public ItemAbstractArmour(ArmorMaterial armorMaterial, ArmourRenderIndex renderIndex, ArmourSlotIndex armourIndex, String name, String textureLocation) {
        super(armorMaterial, renderIndex.ordinal(), armourIndex.ordinal());
        slot = armourIndex.ordinal();
        this.textureLocation = textureLocation;
        setMaxDamage(0);
        setName(name);
        register(name);
    }

    public ItemAbstractArmour register(String name) {
        GameRegistry.registerItem(this, name);
        return this;
    }

    public void setName(String name) {
        setUnlocalizedName(name);
        setTextureName(name);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        return armorType == slot;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return textureLocation;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        if (requireTextureReg())
            itemIcon = register.registerIcon(getModIdentifier() + getIconString());
    }

    public boolean requireTextureReg() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemInformation information = new ItemInformation();
        addInformation(information);
        information.addToList(list);
    }

    public void addInformation(ItemInformation information) {
    }

    public enum ArmourSlotIndex {
        HEAD, CHESTPLATE, LEGGINGS, BOOTS
    }

    public enum ArmourRenderIndex {
        CLOTH, CHAIN, IRON, DIAMOND, GOLD
    }

    public String getModIdentifier() {
        return Loader.instance().activeModContainer().getModId() + ":";
    }
}