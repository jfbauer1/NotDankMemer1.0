/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Nsfw;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.model.Filters;
import me.turulix.main.DiscordBot;
import me.turulix.main.RegisterStuff;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.SubClasses.DatabaseInterface;
import me.turulix.main.UtilClasses.SubClasses.SubClasses;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@DankCommand
public class RandomCommand extends Command {
    public RandomCommand() {
        this.name = "random";
        this.category = new Command.Category("NSFW");
        this.help = "A Random message some user added :D you can also add your owns";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.hidden = false;
        this.children = new Command[]{new Add(), new Report(), new Remove()};
        this.autoTest = false;
        this.nsfw = true;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        @NotNull Random rn = new Random();
        @NotNull ArrayList<SubClasses.RandomClass> randoms = DiscordBot.instance.registerStuff.database.randomManager.getRandoms();
        SubClasses.RandomClass random = randoms.get(rn.nextInt(randoms.size()));
        @NotNull List<String> urls = Utils.extractUrls(random.getRandom());
        @NotNull EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Random Stuff ID: " + random.getID() + " by " + random.getCREATOR_TAG());
        embedBuilder.setColor(new Color(255, 178, 9));
        if (urls.size() >= 1) {
            embedBuilder.setImage(urls.get(0));
        }
        embedBuilder.setDescription(random.getRandom());
        embedBuilder.setFooter(DiscordBot.instance.registerStuff.commandClient.getPrefix() + "random add <Your Msg> | " + DiscordBot.instance.registerStuff.commandClient.getPrefix() + "random report <ID>", null);
        event.reply(embedBuilder.build());
    }

    private class Add extends Command {
        public Add() {
            this.name = "add";
            this.help = "Adds your own message.";
            this.arguments = "<Your Msg>";
            this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
            this.cooldown = 10;
            this.cooldownScope = CooldownScope.USER;
        }

        @Override
        protected void execute(@NotNull CommandEvent event, I18nContext context) {
            @NotNull String[] args = event.getArgs().split(" ");
            if (args.length > 0 && !event.getArgs().equalsIgnoreCase("")) {
                UUID id = DiscordBot.instance.registerStuff.database.randomManager.addRandom(event.getArgs(), event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getIdLong());
                event.reply("Message added! Your ID is " + id.toString());
            } else {
                TextUtilities.SendUsage(event, this);
            }
        }
    }

    private class Report extends Command {
        public Report() {
            this.name = "report";
            this.arguments = "<ID>";
            this.help = "Reports a random message.";
            this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
            this.cooldown = 10;
            this.cooldownScope = CooldownScope.USER;
        }

        @Override
        protected void execute(@NotNull CommandEvent event, I18nContext context) {
            DatabaseInterface databaseInterface = new DatabaseInterface();
            @NotNull String[] args = event.getArgs().split(" ");
            UUID id;
            if (args.length > 0 && !event.getArgs().equalsIgnoreCase("")) {
                try {
                    id = UUID.fromString(args[0]);
                } catch (Exception ex) {
                    event.reply("ID must be a UUID!");
                    return;
                }
                @NotNull RegisterStuff registerStuff = DiscordBot.instance.registerStuff;
                if (databaseInterface.getDocument(Filters.eq("_id", id), "RandomList").size() == 0) {
                    event.reply("ID " + id + " not found.");
                    return;
                }
                Guild supportServer = registerStuff.shardManager.getGuildById(registerStuff.NotDankMemerServerID);
                TextChannel reportChannel = supportServer.getTextChannelById(registerStuff.NotDankMemerReportChannelID);
                @NotNull EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("ID: " + id + " Reporting user: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator());
                for (@NotNull SubClasses.RandomClass random : registerStuff.database.randomManager.getRandoms()) {
                    if (random.getID().toString().equals(id.toString())) {
                        eb.setDescription("Randoms Message: \n" + random.getRandom());
                        break;
                    }
                }
                reportChannel.sendMessage(eb.build()).queue();
                event.reply("Message Reported");

            } else {
                TextUtilities.SendUsage(event, this);
            }
        }
    }

    public class Remove extends Command {
        public Remove() {
            this.ownerCommand = true;
            this.name = "remove";
            this.help = "Removes the Random message";
            this.arguments = "<ID>";
        }

        @Override
        protected void execute(@NotNull CommandEvent event, I18nContext context) {
            @NotNull String[] args = event.getArgs().split(" ");
            UUID id;
            if (args.length > 0 && !event.getArgs().equalsIgnoreCase("")) {
                try {
                    id = UUID.fromString(args[0]);
                } catch (Exception ex) {
                    TextUtilities.SendUsage(event, this);
                    return;
                }
                DiscordBot.instance.registerStuff.database.randomManager.removeRandom(id);
                DiscordBot.instance.registerStuff.database.randomManager.invalidateCache();
                event.reply("The Random Message with the ID: " + id + " got deleted.");
            } else {
                TextUtilities.SendUsage(event, this);
            }
        }
    }

}