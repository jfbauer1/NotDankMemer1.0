/*
 * Developed by Turulix on 21.01.19 22:28.
 * Last modified 21.01.19 22:28.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Database.Manager;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import com.mongodb.client.model.Filters;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.SubClasses.DatabaseInterface;
import me.turulix.main.UtilClasses.SubClasses.FixedCache;
import net.dv8tion.jda.core.entities.Guild;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class GuildSettingsDataManager extends DatabaseInterface implements GuildSettingsManager {
    private final FixedCache<Long, GuildSettings> cache = new FixedCache<>(1000);

    public GuildSettingsDataManager() {
        collectionName = "GuildSettings";
    }

    @Override
    public GuildSettings getSettings(@NotNull Guild guild) {
        return returnSettings(guild);
    }

    public GuildSettings getSettings(@NotNull Long guildID) {
        Guild guild = DiscordBot.instance.registerStuff.shardManager.getGuildById(guildID);
        return returnSettings(guild);
    }

    public GuildSettings getSettings(@NotNull String guildID) {
        Guild guild = DiscordBot.instance.registerStuff.shardManager.getGuildById(guildID);
        return returnSettings(guild);
    }

    private GuildSettings returnSettings(Guild guild) {
        if (cache.contains(guild.getIdLong())) return cache.get(guild.getIdLong());
        GuildSettings settings = new GuildSettings(guild);
        cache.put(guild.getIdLong(), settings);
        return settings;
    }


    @Override
    public void init() {

    }

    @Override
    public void shutdown() {

    }

    public void invalidateCache(Guild guild) {
        invalidateCache(guild.getIdLong());
    }

    public void invalidateCache(long guildId) {
        cache.pull(guildId);
    }

    public class GuildSettings implements GuildSettingsProvider {
        @Nullable
        private String prefix;
        private Guild guild;
        private String lang;
        private Bson filters;

        private GuildSettings(Guild guild) {
            this.guild = guild;
            filters = Filters.eq("_id", guild.getIdLong());

            Document document = getDocument(filters);
            if (document == null) {
                insert(new Document().append("_id", guild.getIdLong()));
                document = getDocument(filters);
            }
            prefix = document.containsKey("prefix") ? document.getString("prefix") : DiscordBot.instance.registerStuff.commandClient.getPrefix();
            lang = document.containsKey("language") ? document.getString("language") : "en_US";
        }


        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
            update(filters, "prefix", prefix);
        }

        public Guild getGuild() {
            return guild;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
            update(filters, "lang", lang);
        }


        @Override
        public Collection<String> getPrefixes() {
            return Collections.singleton(prefix);
        }
    }
}
