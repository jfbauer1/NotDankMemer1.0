/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.Permission;
import org.jetbrains.annotations.NotNull;

@DankCommand
public class NikoCommand extends Command {
    public NikoCommand() {
        this.name = "niko";
        this.help = "Well ^^";
        this.hidden = true;
        this.arguments = "<Message>";
        this.category = new Command.Category("Fun");
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        event.reply("https://www.youtube.com/watch?v=3oD8tj3eBls");
    }
}
