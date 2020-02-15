/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:55.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.ServerAdmin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.Commands.ServerAdmin.Config.ModlogCommand;
import me.turulix.main.Commands.ServerAdmin.Config.PrefixCommand;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;

@DankCommand
public class ConfigCommand extends Command {
    public ConfigCommand() {
        this.name = "config";
        this.help = "";
        this.category = new Category("ServerAdmin");
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.children = new Command[]{new PrefixCommand(), new ModlogCommand()};
        this.autoTest = false;
    }


    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        TextUtilities.SendUsage(event, this);
    }
}
