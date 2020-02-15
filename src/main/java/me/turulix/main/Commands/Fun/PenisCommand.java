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
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@DankCommand
public class PenisCommand extends Command {
    public PenisCommand() {
        this.name = "penis";
        this.arguments = "<User>";
        this.category = new Command.Category("Fun");
        this.help = "We all know who got the longes ;)";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        String msgRaw = event.getMessage().getContentRaw();
        @NotNull String[] args = msgRaw.split(" ");
        if ((args.length == 2) && (!event.getMessage().getMentionedUsers().isEmpty())) {
            @NotNull Random rnd = new Random();

            long seed = event.getMessage().getMentionedUsers().get(0).getIdLong();
            rnd.setSeed(seed);
            int rndNumber = rnd.nextInt(30);
            @NotNull StringBuilder PenisLength = new StringBuilder("8");
            for (int i = 0; i < rndNumber; i++) {
                PenisLength.append("=");
            }
            PenisLength.append("D");
            if (seed == 262702226693160970L || seed == 141268459991334912L) {
                PenisLength = new StringBuilder("8===============================D");
            } else if (seed == 241998290206326785L) {
                PenisLength = new StringBuilder("8D");
            } else if (seed == 277608782123630593L) {
                PenisLength = new StringBuilder("8bit");
            } else if (seed == 324838112213729280L) {
                PenisLength = new StringBuilder("4bit");
            }
            event.reply(event.getMessage().getMentionedUsers().get(0).getAsMention() + "'s Size: " + PenisLength);
        } else {
            TextUtilities.SendUsage(event, this);
        }
    }
}
