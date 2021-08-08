package net.ulterior.others.menus;

import me.clip.placeholderapi.PlaceholderAPI;
import net.ulterior.EclipzeDomination.Main;
import net.ulterior.EclipzeDomination.utils.Utils;
import net.ulterior.others.ItemsUtil;
import net.ulterior.others.SimpleItem;
import net.ulterior.others.visual.Animation;
import net.ulterior.others.visual.Color;
import net.ulterior.others.visual.MinecraftColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Button {
    private final SimpleItem icon;
    private final boolean hasWaveEffect;
    private final boolean hasWaveColors;
    private int slot;
    private String slots;
    private final List<String> leftclick;
    private final List<String> rightclick;
    private final List<String> shiftclick;
    private final List<String> multiclick;
    private final ConfigurationSection config;
    private final Player player;

    public Button(ConfigurationSection section, Player player){
        config = section;
        this.player = player;
        icon = ItemsUtil.createItem(section, player);
        hasWaveEffect = section.get("waveEffect")!=null && section.getBoolean("waveEffect");
        hasWaveColors = section.get("waveColors")!=null;
        if (section.getString("slot")!=null) {
            try {
                Integer.parseInt(Objects.requireNonNull(section.getString("slot")));
                slot = section.getInt("slot");
            } catch (NumberFormatException e) {
                slots = section.getString("slot");
            }
        }
        leftclick = section.getStringList("actions.leftclick");
        rightclick = section.getStringList("actions.rightclick");
        shiftclick = section.getStringList(".actions.shiftclick");
        multiclick = section.getStringList(".actions.multiclick");
    }

    public Player getPlayer() {
        return player;
    }

    public static void setPlaceHolders(SimpleItem item, Player p) {
        if (Main.isPapiLoaded()) {
            item.setDisplayName(PlaceholderAPI.setPlaceholders(p, item.getDisplayName()));
            for (int i = 0; i < item.getLore().size(); i++) {
                item.getLore().set(i, PlaceholderAPI.setPlaceholders(p, item.getLore().get(i)));
            }
        }
    }

    public SimpleItem getIcon() {
        if (hasWaveEffect){
            SimpleItem item = icon.clone();
            setPlaceHolders(item, getPlayer());
            Color[] colors;
            if (hasWaveColors) {
                String[] colorsString = Objects.requireNonNull(config.getString("waveColors")).split(",");
                List<Color> colorList = new ArrayList<>();
                Color color = null;
                for (String s : colorsString) {
                    try {
                        color = MinecraftColor.valueOf(s.toUpperCase()).getColor();
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                    if (color != null) {
                        colorList.add(color);
                    }
                }
                if (colorsString.length == 0) {
                    try {
                        color = MinecraftColor.valueOf(Objects.requireNonNull(config.getString("waveColors")).toUpperCase()).getColor();
                    } catch (IllegalArgumentException ignored) {
                    }
                    colors = new Color[]{color};
                } else {
                    colors = colorList.toArray(new Color[0]);
                }
                if (colors.length == 0){
                    colors = new Color[]{ MinecraftColor.WHITE.getColor(), MinecraftColor.GOLD.getColor()};
                }
            } else {
                colors = new Color[]{ MinecraftColor.WHITE.getColor(), MinecraftColor.GOLD.getColor()};
            }
            item.setDisplayName(Animation.wave(item.getDisplayName(), colors));
            return item;
        }
        return icon;
    }

    public int getSlot() {
        return slot;
    }

    public String getSlots() {
        return slots;
    }

    public void executeActions(InventoryClickEvent var1){
        Player p = (Player) var1.getWhoClicked();
        Object actions = config.get("actions");
        boolean hasActions = actions != null;
        if (hasActions) {
            List<String> commands = new ArrayList<>();
            if (var1.isLeftClick()) {
                commands.addAll(leftclick);
                commands.addAll(multiclick);
            }else if (var1.isRightClick()){
                commands.addAll(rightclick);
                commands.addAll(multiclick);
            } else if (var1.isShiftClick()){
                commands.addAll(shiftclick);
                commands.addAll(multiclick);
            } else {
                commands.addAll(multiclick);
            }
            for (String command : commands) {
                String string;
                if (Main.isPapiLoaded()) {
                    command = PlaceholderAPI.setPlaceholders(p, command);
                }
                if (command.startsWith("[player]")) {
                    string = command.replace("[player]", "");
                    p.chat(string);
                } else if (command.startsWith("[console]")) {
                    string = command.replace("[console]", "");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replaceFirst("/", ""));
                } else if (command.equals("[close]")) {
                    p.closeInventory();
                } else if (command.startsWith("[message]")) {
                    string = command.replace("[message]", "");
                    Utils.sendMessage(p, string);
                } else if (command.startsWith("[open]")){
                    string = command.replace("[open]", "");
                    Menu menu = MenuListener.getPlayerMenu(p, string);
                    if (menu != null) {
                        p.openInventory(menu.getInventory());
                    } else {
                        Utils.sendMessage(p, "{prefix}&7(&c!&7) &cThe menu &f"+string+" &cdoesn't exist!");
                    }
                }
            }
        }
    }
}
