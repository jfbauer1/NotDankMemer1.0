/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Nsfw;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.awt.*;

@DankCommand
public class NekoCommand extends Command {
    public NekoCommand() {
        this.name = "neko";
        this.category = new Category("NSFW");
        this.help = "Sends some nekos ;)";
        this.autoTest = false;
        this.nsfw = true;
        this.requireVote = true;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        @Nullable String msg = Utils.getUrl("https://nekos.life/api/v2/img/lewd");
        if (msg == null) return;
        @Nullable JSONObject obj = new JSONObject(msg);

        String link = obj.getString("url");
        EmbedBuilder builder = new EmbedBuilder().setColor(new Color(165, 29, 255));
        builder.setTitle("Some free neko's ;)");
        builder.setImage(link);
        builder.setFooter("Here " + event.getAuthor().getName() + " have some neko's!", event.getAuthor().getAvatarUrl());
        event.reply(builder.build());
    }
}
