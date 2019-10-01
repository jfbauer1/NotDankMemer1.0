package me.turulix.main.Files;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import me.turulix.main.DiscordBot;
import me.turulix.main.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 22.03.2019 23:18
 */
public class TomlManager {
    String settingsPath = DiscordBot.instance.registerStuff.filePath + "/Settings/";
    File tomlFile = new File(settingsPath + "Config.toml");
    private Config toml;
    private Map<String, Object> originalConfigs;

    public TomlManager() {
        if (!tomlFile.exists()) {
            TomlWriter writer = new TomlWriter();
            try {
                writer.write(new Config(), tomlFile);
            } catch (IOException e) {
                Logger.error(e);
            }
        }
        originalConfigs = new Toml().read(tomlFile).toMap();
        toml = new Toml().read(tomlFile).to(Config.class);

    }

    public void save() {
        TomlWriter writer = new TomlWriter();
        if (!toml.testMode) {
            originalConfigs.put("runOneTimeCode", false);
            try {
                writer.write(originalConfigs, tomlFile);
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }

    public Config getToml() {
        return toml;
    }
}
