package net.ulterior.EclipzeDomination.kits;

import net.ulterior.EclipzeDomination.Main;
import net.ulterior.EclipzeDomination.utils.Utils;
import net.ulterior.others.FileConfigurationUtil;

import java.io.File;
import java.util.HashMap;

public class KitsManager {
    public static HashMap<String, Kit> kitsManager;

    public static void initKits(){
        kitsManager = new HashMap<>();

        for (String s : Utils.getKits().stream().map(FileConfigurationUtil::getFile).map(File::getName).toArray(String[]::new)){
            s = s.replace(".yml", "");
            new Kit(s);
        }

        Main.log(0, kitsManager.isEmpty() ? "{prefix}&cAny kit loaded!" : "{prefix}&e"+kitsManager.size()+" &akits loaded correctly!");

    }
}
