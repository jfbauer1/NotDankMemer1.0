/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:55.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.ServerAdmin.Config;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.Database.Manager.GuildSettingsDataManager;
import me.turulix.main.DiscordBot;
import me.turulix.main.Logger;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

public class ModlogCommand extends Command {
    public ModlogCommand() {
        this.name = "modlog";
        this.help = "Sets a channel to log stuff for admins.";
        this.arguments = "#channel";
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.autoTest = false;
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        if (event.getMessage().getMentionedChannels().size() >= 1) {
            GuildSettingsDataManager.GuildSettings settings = DiscordBot.instance.registerStuff.database.guildSettingsDataManager.getSettings(event.getGuild());
            try {
                assert settings != null;
                TextChannel channel = event.getMessage().getMentionedChannels().get(0);
                settings.setModlog(channel);
                if (channel.canTalk()) {
                    event.replySuccess("Modlog Channel changed!");
                } else {
                    event.replyError("I cant write in the modlog channel :(");
                }

            } catch (Exception ex) {
                Logger.error(event, ex);
            }
        } else {
            TextUtilities.SendUsage(event, this);
        }
    }
}
