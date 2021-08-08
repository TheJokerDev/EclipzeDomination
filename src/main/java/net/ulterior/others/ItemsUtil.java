package net.ulterior.others;

import me.TheJokerDev.Cosmetics.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ItemsUtil {

    public static SimpleItem createItem(ConfigurationSection section, Player p) {
        String[] var1;
        int int1 = 0;
        int int2 = 0;
        int int3 = 0;
        SimpleItem item = new SimpleItem(XMaterial.BEDROCK).setDisplayName("&7&oName null");
        boolean hasMaterial = section.get("material") != null;
        String materialStr;
        XMaterial material = null;
        boolean hasData = section.get("data") != null;
        boolean hasRemoveAttributes = section.get("removeAttributes") != null;
        int data;
        boolean hasAmount = section.get("amount") != null;
        int amount;
        boolean hasGlow = section.get("glowing") != null;
        boolean glowing;
        boolean hasSkullData = section.get("skull") != null;
        String skullData;
        boolean hasColor = section.get("color") != null;
        String color;
        boolean hasFireWorkData = section.get("firework") != null;
        boolean hasDisplayName = section.get("meta.name") != null;
        boolean hasLore = section.get("meta.lore") != null;
        boolean hasEnchants = section.get("enchantments") != null;

        if (hasMaterial) {
            materialStr = section.getString("material");
            try {
                if (materialStr.contains(";")) {
                    String[] materialS = materialStr.split(";");
                    materialStr = materialS[new Random().nextInt(materialS.length)];
                    material = XMaterial.valueOf(materialStr.toUpperCase());
                } else {
                    material = XMaterial.valueOf(materialStr.toUpperCase());
                }
            } catch (IllegalArgumentException e) {
                Utils.sendMessage(Bukkit.getConsoleSender(), "{prefix}&cMaterial on " + section.getName() + " is not valid.");
            }
            if (material != null) {
                if (!material.isSupported()){
                    item.setMaterial(XMaterial.BEDROCK);
                } else {
                    item.setMaterial(material);
                }
            }
        }
        if (hasData) {
            data = section.getInt("data");
            item.setDurability(data);
        }
        if (hasAmount) {
            amount = section.getInt("amount");
            item.setAmount(amount);
        }
        if (hasGlow) {
            glowing = section.getBoolean("glowing");
            item.setGlowing(glowing);
        }
        if (material == XMaterial.PLAYER_HEAD && hasSkullData) {
            skullData = section.getString("skull").replaceAll("%player_name%", p.getName());
            if (skullData.startsWith("base-")) {
                skullData = skullData.replace("base-", "");
                item.setItem(SkullUtils.getHead(skullData));
            } else if (skullData.startsWith("uuid-")) {
                skullData = skullData.replace("uuid-", "");
                UUID uuid = UUID.fromString(skullData);
                item.setItem(SkullUtils.getHead(uuid));
            } else if (skullData.startsWith("name-")) {
                skullData = skullData.replace("name-", "");
                OfflinePlayer pf = Bukkit.getOfflinePlayer(skullData);
                item.setItem(SkullUtils.getHead(pf));
            } else if (skullData.startsWith("url-")) {
                skullData = skullData.replace("url-", "");
                skullData = "http://textures.minecraft.net/texture/" + skullData;
                item.setItem(SkullUtils.getHead(skullData));
            }
        }
        if (material == XMaterial.FIREWORK_STAR && hasFireWorkData) {
            ItemMeta meta = item.build().getItemMeta();
            FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
            color = section.getString("firework");
            Color color1 = null;
            var1 = color.split("-");
            if (var1.length == 3) {
                int1 = isNumeric(var1[0]) ? Integer.parseInt(var1[0]) : 0;
                int2 = isNumeric(var1[1]) ? Integer.parseInt(var1[1]) : 0;
                int3 = isNumeric(var1[2]) ? Integer.parseInt(var1[2]) : 0;
            } else {
                int1 = 0;
                int2 = 0;
                int3 = 0;
            }
            color1 = Color.fromRGB(int1, int2, int3);
            FireworkEffect effect = FireworkEffect.builder().withColor(color1).build();
            metaFw.setEffect(effect);
            item.setFireworkEffectMeta(metaFw);
        }
        if ((material.name().contains("LEATHER")) && hasColor) {
            color = section.getString("color");
            Color color1;
            var1 = color.split("-");
            if (var1.length == 3) {
                int1 = isNumeric(var1[0]) ? Integer.parseInt(var1[0]) : 0;
                int2 = isNumeric(var1[1]) ? Integer.parseInt(var1[1]) : 0;
                int3 = isNumeric(var1[2]) ? Integer.parseInt(var1[2]) : 0;
            } else {
                int1 = 0;
                int2 = 0;
                int3 = 0;
            }
            color1 = Color.fromRGB(int1, int2, int3);
            item.setColor(color1);
        }
        if (hasEnchants) {
            List<String> enchants = section.getStringList("enchantments");
            for (String s : enchants){
                String[] var1a = s.split(",");
                item.addEnchantment(Enchantment.getByName(var1a[0].toUpperCase()), Integer.parseInt(var1a[1]));
                Utils.sendMessage(Bukkit.getConsoleSender(), "{prefix}&eEnchantment for "+section.getName()+": &e"+var1a[0]+"&a with level: &b"+var1a[1]);
            }
            item.setShowEnchantments(true);
        }
        if (hasDisplayName) {
                item.setDisplayName(section.getString("meta.name"));
        }
        if (hasLore) {
            item.setLore(section.getStringList("meta.lore"));
        }
        if (hasRemoveAttributes) {
            item.setHideFlags(true);
        }
        return item;
    }

    public static boolean isNumeric(String var0) {
        try {
            Integer.parseInt(var0);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }
}
