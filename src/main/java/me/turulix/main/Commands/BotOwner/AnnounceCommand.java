/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 23:04.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.BotOwner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;

@DankCommand
public class AnnounceCommand extends Command {
    public AnnounceCommand() {
        this.name = "announce";
        this.ownerCommand = true;
        this.help = "Sends an announcement to all Modlog channels";
        this.arguments = "<Text>";
        this.autoTest = false;
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {

        /*
        if (!event.getArgs().equalsIgnoreCase("")) {
            DiscordBot.instance.registerStuff.shardManager.getGuilds().forEach(guild -> {
                GuildSettingsDataManager.GuildSettings settings = DiscordBot.instance.registerStuff.database.guildSettingsDataManager.getSettings(guild);
                if (settings != null && settings.getModlogChannel() != null) {
                    TextChannel channel = guild.getTextChannelById(settings.getModlogChannel());
                    if (channel != null) {
                        if (channel.canTalk()) {
                            channel.sendMessage(event.getArgs()).queue();
                        }
                    }
                }
            });
        } else {
            TextUtilities.SendUsage(event, this);
        }
        */
    }
}
