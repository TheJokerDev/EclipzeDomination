package net.ulterior.EclipzeDomination.levels;

import net.ulterior.EclipzeDomination.Main;
import net.ulterior.EclipzeDomination.utils.Utils;
import net.ulterior.others.FileConfigurationUtil;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class Level {
    private String name;
    private int level;
    private String equation;
    private boolean useEquation;
    private String displayName;

    public Level(String id){
        name = id;
    }

    private boolean exist(String key){
        return getFile().get(key)!=null;
    }

    public FileConfigurationUtil getFile(){
        return Utils.getLevels();
    }

}
