package net.ulterior.EclipzeDomination.kits;

import net.ulterior.EclipzeDomination.Main;
import net.ulterior.others.FileConfigurationUtil;
import net.ulterior.others.ItemsUtil;
import net.ulterior.others.SimpleItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Kit {
    private String name;
    private SimpleItem icon;
    private int price = 0;
    private int levelRequired = 0;
    private String permission = null;
    private List<KitItem> items;

    public Kit (String name){
        this.name = name;
        try {
            icon = ItemsUtil.createItem(getFile().getSection("icon"), null);
        } catch (Exception e) {
            Main.log(1, "Trying to load kit icon of Kit &e"+getName()+"&c.");
        }
        if (exist("settings.price")){
            price = getFile().getInt("settings.price");
        }
        if (exist("settings.level")){
            levelRequired = getFile().getInt("settings.level");
        }

        if (exist("settings.permission")){
            permission = getFile().getString("settings.permission");
        }

        if (exist("items")){
            items = new ArrayList<>();
            for (String s : getFile().getSection("items").getKeys(false)){
                KitItem item;
                try {
                    item = new KitItem(getFile().getSection("items."+s));
                } catch (Exception e) {
                    Main.log(1, "Trying to load kit item of Kit &e"+getName()+"&c named: &b"+s+".");
                    continue;
                }
                items.add(item);
            }
        } else {
            return;
        }

        KitsManager.kitsManager.put(getName(), this);
    }

    private boolean exist(String key){
        return getFile().get(key)!=null;
    }

    public FileConfigurationUtil getFile(){
        return new FileConfigurationUtil(new File(Main.getPlugin().getDataFolder()+ File.separator+"menu"), getName()+".yml");
    }

    public List<KitItem> getItems() {
        return items;
    }

    public void setItems(Player p){
        Inventory inv = p.getInventory();
        inv.clear();
        inv.setContents(null);
        inv.setStorageContents(null);
        for (KitItem kitItem : getItems()){
            inv.setItem(kitItem.getSlot(), kitItem.getItem().build());
        }
        p.updateInventory();
    }

    public String getName() {
        return name;
    }

    public SimpleItem getIcon() {
        return icon;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getPrice() {
        return price;
    }

    public String getPermission() {
        return permission;
    }
}
