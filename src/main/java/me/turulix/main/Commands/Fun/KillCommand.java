/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.Database.Manager.KillManager;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

@DankCommand
public class KillCommand extends Command {
    public KillCommand() {
        this.name = "kill";
        this.arguments = "<User>";
        this.category = new Command.Category("Fun");
        this.help = "Kills the user :D";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        @NotNull String[] args = event.getMessage().getContentRaw().split(" ");
        DiscordBot discordBot = DiscordBot.instance;
        if (args.length == 2) {
            @NotNull Random rn = new Random();
            if (event.getMessage().getMentionedUsers().size() == 0) {
                TextUtilities.SendUsage(event, this);
                return;
            }
            String mention = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getEffectiveName();
            String author = event.getMember().getEffectiveName();
            @NotNull ArrayList<KillManager.Kill> kill = discordBot.registerStuff.database.killManager.getKills();
            event.reply(kill.get(rn.nextInt(kill.size())).getMsg().replace("$author", author).replace("$mention", mention));
        } else {
            TextUtilities.SendUsage(event, this);
        }
    }
}
