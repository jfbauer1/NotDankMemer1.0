/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.DiscordBot;
import me.turulix.main.Logger;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.Random;

@DankCommand
public class TrumpCommand extends Command {
    public TrumpCommand() {
        this.name = "trump";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.help = "Sends you a quote of Trump";
        this.category = new Category("Fun");
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        try {
            @Nullable String msg = Utils.getUrl("https://api.whatdoestrumpthink.com/api/v1/quotes");
            @NotNull Random rn = new Random();
            assert msg == null;
            @Nullable JSONObject obj = new JSONObject(msg);

            JSONArray arr = obj.getJSONObject("messages").getJSONArray("non_personalized");
            int rnNumber = rn.nextInt(arr.length());
            String Quote = arr.getString(rnNumber);
            int trumpbildNumber = rn.nextInt(DiscordBot.instance.registerStuff.trumpPictures.size());

            EmbedBuilder builder = new EmbedBuilder().setColor(new Color(6, 32, 255));
            builder.setTitle("Quote: " + rnNumber + " Trump 2018: \"" + Quote + "\"");
            builder.setImage(DiscordBot.instance.registerStuff.trumpPictures.get(trumpbildNumber));
            builder.setFooter("No " + event.getAuthor().getName() + " just no.", event.getAuthor().getAvatarUrl());
            event.reply(builder.build());

        } catch (Exception exe) {
            Logger.error(event, exe);
        }
    }
}
