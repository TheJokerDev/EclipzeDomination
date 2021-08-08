package net.ulterior.EclipzeDomination.kits;

import net.ulterior.EclipzeDomination.Main;
import net.ulterior.others.ItemsUtil;
import net.ulterior.others.SimpleItem;
import org.bukkit.configuration.ConfigurationSection;

public class KitItem {
    private String name;
    private SimpleItem item;
    private int slot;

    public KitItem(ConfigurationSection section){
        name = section.getName();
        slot = section.getInt("slot");
        item = ItemsUtil.createItem(section, null);
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public SimpleItem getItem() {
        return item;
    }
}
