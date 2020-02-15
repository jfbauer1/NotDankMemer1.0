/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.Database.Manager.RoastManager;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

@DankCommand
public class RoastCommand extends Command {
    public RoastCommand() {
        this.name = "roast";
        this.arguments = "";
        this.category = new Command.Category("Fun");
        this.help = "Roast yourself";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        DiscordBot discordBot = DiscordBot.instance;
        @NotNull Random rn = new Random();
        @NotNull ArrayList<RoastManager.Roast> roasts = discordBot.registerStuff.database.roastManager.getRoasts();
        String author = event.getMember().getEffectiveName();
        event.reply(roasts.get(rn.nextInt(roasts.size())).getMsg().replace("$author", author));
    }
}
