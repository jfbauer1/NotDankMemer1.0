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
import net.dv8tion.jda.api.entities.Member;

import java.util.Random;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 05.01.2019 06:54
 */
@DankCommand
public class LoveCommand extends Command {
    public LoveCommand() {
        this.name = "love";
        this.arguments = "<@User>";
        this.category = new Category("Fun");
        this.help = "Its a match! maybe.";
        this.autoTest = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_READ};
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        //Usage: +love @User | + love @User @User2
        Member user1;
        Member user2;
        switch (event.getMessage().getMentionedMembers().size()) {
            case 0:
                TextUtilities.SendUsage(event, this);
                return;
            case 1:
                user1 = event.getGuild().getMember(event.getAuthor());
                user2 = event.getMessage().getMentionedMembers().get(0);
                break;
            default:
                user1 = event.getMessage().getMentionedMembers().get(0);
                user2 = event.getMessage().getMentionedMembers().get(1);
                break;
        }
        Random random = new Random();
        random.setSeed(user1.getUser().getIdLong() + user2.getUser().getIdLong());
        int precentage = (user1.getUser().getIdLong() == user2.getUser().getIdLong() ? 101 : random.nextInt(101));
        String result;
        if (precentage < 45) {
            result = context.get("command.love.bad");
        } else if (precentage < 75) {
            result = context.get("command.love.okay");
        } else if (precentage < 100) {
            result = context.get("command.love.good");
        } else {
            result = context.get("command.love.perfect");
            if (precentage == 101) {
                result = context.get("command.love.self");
            }
        }
        event.reply(result);
    }
}
