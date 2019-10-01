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
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        this.name = "prefix";
        this.help = "Changes the serverwide prefix";
        this.category = new Category("ServerAdmin");
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.arguments = "<New Prefix>";
        this.guildOnly = true;
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        @Nullable GuildSettingsDataManager.GuildSettings settings = (GuildSettingsDataManager.GuildSettings) DiscordBot.instance.registerStuff.commandClient.getSettingsManager().getSettings(event.getGuild());
        @NotNull String[] args = event.getArgs().split(" ");
        if (event.getArgs().split(" ").length == 1 && !event.getArgs().equalsIgnoreCase("")) {
            assert settings != null;
            settings.setPrefix(args[0]);
            event.replySuccess("Changed the prefix to \"" + args[0] + "\"");
        } else {
            event.replyError("Something went wrong here o.O");
        }
    }
}
