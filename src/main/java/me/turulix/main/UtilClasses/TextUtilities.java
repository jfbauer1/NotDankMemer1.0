/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 23:36.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.InputStream;

public class TextUtilities {
    public static void SendUsage(@NotNull CommandEvent event, Command command) {
        if (command.getChildren().length >= 1) {
            @NotNull EmbedBuilder eb = new EmbedBuilder().setColor(new Color(81, 79, 255)).setDescription("There are possible subcommands: ");
            @NotNull StringBuilder commandPart = new StringBuilder();
            for (@NotNull Command cmd : command.getChildren()) {
                commandPart.append("`").append(cmd.getName()).append(" ").append(cmd.getArguments()).append("`").append(" - ").append(cmd.getHelp()).append("\n");
            }
            eb.addField("__Subcommand:__", commandPart.toString(), false);
            event.reply(eb.build());
        } else {
            @NotNull EmbedBuilder eb = new EmbedBuilder().setColor(new Color(81, 79, 255)).setDescription("Usage of the command `" + command.getName() + "` is:");
            @NotNull StringBuilder commandPart = new StringBuilder("`" + command.getName() + " " + command.getArguments() + "`" + " - " + command.getHelp() + "\n");
            eb.addField("__Usage:__", commandPart.toString(), false);
            event.reply(eb.build());
        }
    }

    public static void sendEmbedLocalFile(@NotNull InputStream in, CommandEvent event, @NotNull String fileName) {
        @NotNull EmbedBuilder embed = new EmbedBuilder();
        embed.setImage("attachment://" + fileName).setColor(new Color(26, 255, 41));
        event.getEvent().getChannel().sendFile(in, fileName).embed(embed.build()).queue();

    }

    public static void sendNSFW(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder().setColor(new Color(255, 0, 0));
        builder.setTitle("Activate Nsfw dummy!");
        builder.setImage(DiscordBot.instance.registerStuff.StaticGifs.get("nsfw"));
        event.reply(builder.build());
    }

    public static void sendVote(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder().setColor(new Color(255, 0, 0));
        builder.setTitle("You need to vote for the bot first :3");
        builder.setDescription("You can support the bot and unlock NSFW by clicking [here](https://discordbots.org/bot/277608782123630593/vote)\nIt Would help me out alot!.\n\nIf there are any problems pls contact me on [this](" + DiscordBot.instance.registerStuff.commandClient.getServerInvite() + ") discord.");
        builder.setImage("https://media.giphy.com/media/3oKIPxnFmCtBxP8a9q/source.gif");
        event.reply(builder.build());
    }

    public static String replaceMentionedUsersWithName(CommandEvent event, String msg) {
        for (@NotNull User mentionedUser : event.getMessage().getMentionedUsers()) {
            msg = msg.replace("<@!" + mentionedUser.getIdLong() + ">", event.getGuild().getMemberById(mentionedUser.getIdLong()).getEffectiveName());
            msg = msg.replace("<@" + mentionedUser.getIdLong() + ">", event.getGuild().getMemberById(mentionedUser.getIdLong()).getEffectiveName());
        }
        return msg;
    }

}
