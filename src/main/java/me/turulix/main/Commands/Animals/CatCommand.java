/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:52.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Animals;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.awt.*;
import java.text.MessageFormat;

@DankCommand
public class CatCommand extends Command {
    public CatCommand() {
        this.name = "cat";
        this.category = new Command.Category("Animals");
        this.hidden = false;
        this.help = "Cute cat pictures *Awww*";
        this.arguments = "";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.autoTest = false;
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        @Nullable String msg = Utils.getUrl("http://aws.random.cat/meow");
        assert msg == null;
        @Nullable JSONObject obj = new JSONObject(msg);

        msg = obj.getString("file");
        EmbedBuilder builder = new EmbedBuilder().setColor(new Color(255, 154, 136));
        builder.setTitle("Awwwww!");
        builder.setImage(msg);
        builder.setFooter(MessageFormat.format("Here {0} a cat!", event.getMember().getEffectiveName()), event.getAuthor().getEffectiveAvatarUrl());
        event.reply(builder.build());
    }
}
