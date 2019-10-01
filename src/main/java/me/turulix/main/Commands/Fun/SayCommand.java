/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.Permission;
import org.jetbrains.annotations.NotNull;

@DankCommand
public class SayCommand extends Command {
    public SayCommand() {
        this.name = "say";
        this.help = "I say what you want me to say";
        this.arguments = "<Message>";
        this.category = new Command.Category("Fun");
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.cooldown = 5;
        this.cooldownScope = CooldownScope.USER;
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        if (event.getArgs().split(" ").length >= 1 && !event.getArgs().equalsIgnoreCase("")) {
            event.getMessage().delete().queue();
            String msg = event.getArgs();
            msg = TextUtilities.replaceMentionedUsersWithName(event, msg);
            event.reply(msg);
        }
    }
}
