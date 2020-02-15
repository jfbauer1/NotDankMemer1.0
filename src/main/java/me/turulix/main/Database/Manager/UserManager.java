/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 22.01.19 00:20.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Database.Manager;


import com.mongodb.client.model.Filters;
import me.turulix.main.Logger;
import me.turulix.main.UtilClasses.SubClasses.DatabaseInterface;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UserManager extends DatabaseInterface {
    public static HashMap<Long, UserSettings> cache = new HashMap<>();

    public UserManager() {
        collectionName = "UserDatabase";
    }

    public UserSettings getUserSettings(Long id) {
        Bson filters = Filters.eq("_id", id);
        if (cache.containsKey(id)) return cache.get(id);
        Document document = getDocument(filters);
        if (document == null) {
            insert(new Document().append("_id", id));
            document = getDocument(filters);
        }

        String lang = document.containsKey("language") ? document.getString("language") : "en_US";
        UserSettings settings = new UserSettings(lang, hasUserVoted(id));

        cache.put(id, settings);
        return settings;
    }


    public UserSettings getUserSettings(User user) {
        return getUserSettings(user.getIdLong());
    }

    public UserSettings getUserSettings(Member member) {
        return getUserSettings(member.getUser().getIdLong());
    }

    public boolean hasUserVoted(Long id) {
        try {
            Date date = getDocument(Filters.eq("_id", id)).getDate("voteDate");
            if (date == null) {
                return false;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 7);
            return date.before(calendar.getTime());
        } catch (Exception ex) {
            Logger.error("Could not find user", ex);
        }
        return true;
    }

    public void removeUser(Long id) {
        cache.remove(id);
    }

    public void clearCache() {
        cache.clear();
    }

    public class UserSettings {
        String lang;
        Boolean hasVoted = true;

        public UserSettings(String lang, Boolean hasVoted) {
            this.lang = lang;
            this.hasVoted = hasVoted;
        }

        public String getLang() {
            return lang;
        }

        public Boolean getHasVoted() {
            return hasVoted;
        }

        public void setHasVoted(Boolean hasVoted) {
            this.hasVoted = hasVoted;
        }
    }
}
