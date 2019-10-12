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
import net.dv8tion.jda.core.Permission;
import org.jetbrains.annotations.NotNull;

@DankCommand
public class PlayCommand extends Command {
    public static PlayCommand instance;
    public PlayCommand() {
        this.name = "play";
        this.arguments = "<Link>";
        this.category = new Category("Music");
        this.help = "Adds the song to the songlist";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.autoTest = false;
        instance = this;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        DiscordBot.instance.registerStuff.musicManager.Play(event);
    }
}
