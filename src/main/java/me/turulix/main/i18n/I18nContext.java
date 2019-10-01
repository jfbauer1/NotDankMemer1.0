/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 22.01.19 00:20.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.i18n;

import me.turulix.main.Database.Manager.GuildSettingsDataManager;
import me.turulix.main.Database.Manager.UserManager;
import me.turulix.main.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 21.01.2019 22:03
 */
public class I18nContext {
    private GuildSettingsDataManager.GuildSettings guildSettings;
    private UserManager.UserSettings userSettings;

    public I18nContext(GuildSettingsDataManager.GuildSettings guildSettings, UserManager.UserSettings userSettings) {
        this.guildSettings = guildSettings;
        this.userSettings = userSettings;
    }

    public String get(String s) {
        try {
            ResourceBundle context = I18n.getResourceBundle(getContextLanguage());
            return context.getString(s);
        } catch (Exception ignored) {
        }
        try {
            ResourceBundle context = I18n.getResourceBundle(new Locale("en", "US"));
            return context.getString(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Logger.sendToDiscord("No key Named: " + s + " found in en_US");
        return s;
    }

    public Locale getContextLanguage() {
        if (guildSettings.getLang() == null && userSettings.getLang() == null) {
            return new Locale("en", "US");
        }

        String lang;
        if (userSettings.getLang() == null) {
            lang = guildSettings.getLang();
        } else {
            lang = userSettings.getLang();
        }
        ResourceBundle context = I18n.getResourceBundle(new Locale("en", "US"));
        return context == null ? new Locale("en", "US") : new Locale(lang.split("_")[0], lang.split("_")[1]);
    }
}
