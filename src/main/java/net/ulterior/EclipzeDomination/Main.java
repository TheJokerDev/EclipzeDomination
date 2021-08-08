package net.ulterior.EclipzeDomination;

import net.ulterior.EclipzeDomination.kits.KitsManager;
import net.ulterior.EclipzeDomination.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;

        KitsManager.initKits();
    }

    public static boolean isPapiLoaded(){
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static String getPrefix(){
        return Utils.ct(plugin.getConfig().getString("prefix"));
    }

    public static void log(int mode, String msg){
        if (mode == 0){
            Utils.sendMessage(Bukkit.getConsoleSender(), msg);
        } else if (mode == 1){
            Utils.sendMessage(Bukkit.getConsoleSender(), "&c&lError: &7"+msg);
        } else if (mode == 2){
            if (getPlugin().getConfig().getBoolean("debug")) {
                Utils.sendMessage(Bukkit.getConsoleSender(), "&e&lDebug: &7" + msg);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
