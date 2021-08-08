package net.ulterior.EclipzeDomination.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.ulterior.EclipzeDomination.Main;
import net.ulterior.others.DefaultFontInfo;
import net.ulterior.others.FileConfigurationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static String ct(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static List<String> ct (List<String> list){
        return list.stream().map(Utils::ct).collect(Collectors.toList());
    }
    public static String[] ct (String... list){
        return Arrays.stream(list).map(Utils::ct).toArray(String[]::new);
    }

    public static void sendMessage(CommandSender sender, List<String> list){
        list.forEach(s->sendMessage(sender, s));
    }
    public static void sendMessage(CommandSender sender, String... list){
        Arrays.stream(list).forEach(s->sendMessage(sender, s));
    }
    public static void sendMessage(CommandSender sender, String msg){
        msg = Utils.ct(msg);
        msg = msg.replace("\\n", "\n");
        if (msg.contains("\n")){
            String[] msg2 = msg.split("\n");
            sendMessage(sender, msg2);
            return;
        }
        boolean hasPrefix = msg.contains("{prefix}");
        boolean isCentered = msg.contains("{center}");
        boolean isBroadCast = msg.contains("{broadcast}");

        if (hasPrefix){
            msg = msg.replace("{prefix}", "");
            msg = Main.getPrefix()+msg;
        }
        if (sender instanceof Player){
            msg = Main.isPapiLoaded() ? PlaceholderAPI.setPlaceholders((Player)sender, msg) : msg;
        } else {
            msg = Main.isPapiLoaded() ? PlaceholderAPI.setPlaceholders(null, msg) : msg;
        }
        if (isCentered){
            msg = msg.replace("{center}", "");
            msg = getCenteredMSG(msg);
        }
        if (isBroadCast){
            msg = msg.replace("{broadcast}", "");
            String finalMsg = msg;
            Bukkit.getOnlinePlayers().forEach(p -> Utils.sendMessage(p, finalMsg));
            sendMessage(Bukkit.getConsoleSender(), msg);
            return;
        }

        if (sender instanceof Player){
            sender.sendMessage(msg);
        } else {
            Bukkit.getConsoleSender().sendMessage(msg);
        }
    }

    public static List<FileConfigurationUtil> getKits(){
        File folder = new File(Main.getPlugin().getDataFolder()+File.separator+"kits");
        if (!folder.exists()){
            folder.mkdir();
        }
        if (folder.listFiles().length == 0){
            Main.getPlugin().saveResource("kits.yml", false);
        }
        List<FileConfigurationUtil> files = new ArrayList<>();
        for (File f : folder.listFiles()){
            if (f.getName().endsWith(".yml")){
                files.add(new FileConfigurationUtil(f));
            }
        }
        return files;
    }

    public static String getCenteredMSG(String message){
        if(message == null || message.equals("")){
            return null;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }
}
