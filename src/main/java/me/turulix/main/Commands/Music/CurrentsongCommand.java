/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

@DankCommand
public class CurrentsongCommand extends Command {
    public CurrentsongCommand() {
        this.name = "currentsong";
        this.arguments = "";
        this.category = new Category("Music");
        this.help = "Shows the currentsong";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        DiscordBot.instance.registerStuff.musicManager.Currentsong(event);
    }
}
