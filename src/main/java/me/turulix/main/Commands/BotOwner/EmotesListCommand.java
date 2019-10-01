/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:52.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.BotOwner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import me.turulix.main.DiscordBot;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;


public class EmotesListCommand extends Command {

    @NotNull
    private final Paginator.Builder pbuilder;
    private CommandEvent originalCommand;

    public EmotesListCommand(EventWaiter waiter) {
        this.name = "emotes";
        this.autoTest = false;
        this.help = "Shows all emotes the bot can use";
        this.category = new Category("BotOwner");
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_MANAGE};
        this.ownerCommand = true;
        pbuilder = new Paginator.Builder().setColumns(1).setItemsPerPage(10).showPageNumbers(true).waitOnSinglePage(true).useNumberedItems(false).setFinalAction(m -> {
            try {
                m.clearReactions().queue();
            } catch (PermissionException ex) {
                m.delete().queue();
            }
        }).setEventWaiter(waiter).setFinalAction(message -> {
            message.delete().queue();
            originalCommand.getMessage().delete().queue();
        }).setTimeout(1, TimeUnit.MINUTES);

    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        originalCommand = event;
        int page = 1;
        if (!event.getArgs().isEmpty()) {
            try {
                page = Integer.parseInt(event.getArgs());
            } catch (NumberFormatException e) {
                event.reply(event.getClient().getError() + " `" + event.getArgs() + "` is not a valid integer!");
                return;
            }
        }
        pbuilder.clearItems();

        for (@NotNull Emote emote : DiscordBot.instance.registerStuff.shardManager.getEmotes()) {
            for (Role roles : emote.getGuild().getSelfMember().getRoles()) {
                if (emote.getRoles().isEmpty() || emote.getRoles().contains(roles)) {
                    pbuilder.addItems(emote.getAsMention() + " ID: `" + "<:" + emote.getName() + " : " + emote.getIdLong() + ">" + "` Server: " + (emote.getGuild() == null ? "Fake Emote" : emote.getGuild().getIdLong()));
                    break;
                }
            }
        }

        Paginator p = pbuilder.setColor(event.isFromType(ChannelType.TEXT) ? event.getSelfMember().getColor() : Color.black).setText(event.getClient().getSuccess() + " Emotes that **" + event.getSelfUser().getName() + "** can use :D").setUsers(event.getAuthor()).build();
        p.paginate(event.getChannel(), page);
    }
}
