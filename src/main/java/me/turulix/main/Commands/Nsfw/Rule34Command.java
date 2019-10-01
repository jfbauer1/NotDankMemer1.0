/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:55.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Nsfw;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.Random;

@DankCommand
public class Rule34Command extends Command {
    public Rule34Command() {
        this.name = "rule34";
        this.arguments = "<Search Term>";
        this.help = "If it exists there's porn of it.";
        this.category = new Category("NSFW");
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.autoTest = false;
        this.nsfw = true;
        this.requireVote = true;
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        String downloadedString = Utils.getUrl("https://r34-json-api.herokuapp.com/posts?tags=" + event.getArgs().replace(" ", "%20"));
        if (downloadedString.startsWith("<!DOCTYPE html>")) {
            event.reply("The Command is currently broken cause of the api being down OwO");
            return;
        }
        JSONObject obj = new JSONObject("{\"array\":" + downloadedString + "}");
        JSONArray array = obj.getJSONArray("array");
        if (array.length() == 0) {
            event.reply("There was actually nothing found for: " + event.getArgs());
            return;
        }
        Random random = new Random();
        EmbedBuilder builder = new EmbedBuilder().setColor(new Color(165, 29, 255));
        builder.setTitle("It exists...");
        builder.setImage(array.getJSONObject(random.nextInt(array.length())).getString("file_url"));
        builder.setFooter("Here " + event.getAuthor().getName() + "!", event.getAuthor().getAvatarUrl());
        event.reply(builder.build());
    }
}
