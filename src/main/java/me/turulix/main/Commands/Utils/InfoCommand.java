/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:55.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Utils;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@DankCommand
public class InfoCommand extends Command {
    public InfoCommand() {
        this.name = "info";
        this.category = new Command.Category("Miscellaneous");
        this.hidden = false;
        this.help = "Gets infos about something.";
        this.arguments = "<Subcommand>";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.children = new Command[]{new Emote(), new User()};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        TextUtilities.SendUsage(event, this);
    }

    public class Emote extends Command {
        public Emote() {
            this.name = "emote";
            this.category = new Command.Category("utils");
            this.hidden = false;
            this.help = "Shows the id of an emote or the UTF-8 Emote";
            this.arguments = "<Emote>";
            this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        }

        @Override
        protected void execute(@NotNull CommandEvent event, I18nContext context) {
            String str = event.getArgs().split(" ")[0];
            if (str.matches("<:.*:\\d+>")) {
                String id = str.replaceAll("<:.*:(\\d+)>", "$1");
                net.dv8tion.jda.core.entities.Emote emote = DiscordBot.instance.registerStuff.shardManager.getEmoteById(id);
                if (emote == null) {
                    event.reply(DiscordBot.instance.registerStuff.commandClient.getWarning() + "Unknown emote:\n" + "ID: **" + id + "**\n" + "Guild: Unknown\n" + "URL: https://discordcdn.com/emojis/" + id + ".png");
                    return;
                }
                event.reply(DiscordBot.instance.registerStuff.commandClient.getSuccess() + "Emote **" + emote.getName() + "**:\n" + "ID: **" + emote.getId() + "**\n" + "Guild: " + (emote.getGuild() == null ? "Unknown" : "**" + emote.getGuild().getName() + "**") + "\n" + "URL: " + emote.getImageUrl());
                return;
            }
            if (str.codePoints().count() > 10) {
                event.reply(DiscordBot.instance.registerStuff.commandClient.getError() + "Invalid emote, or input is too long");
                return;
            }
            StringBuilder builder = new StringBuilder(DiscordBot.instance.registerStuff.commandClient.getSuccess() + "Emoji/Character info:");
            str.codePoints().forEachOrdered(code -> {
                char[] chars = Character.toChars(code);
                String hex = Integer.toHexString(code).toUpperCase();
                while (hex.length() < 4) hex = "0" + hex;
                builder.append("\n`\\u").append(hex).append("`   ");
                if (chars.length > 1) {
                    String hex0 = Integer.toHexString(chars[0]).toUpperCase();
                    String hex1 = Integer.toHexString(chars[1]).toUpperCase();
                    while (hex0.length() < 4) hex0 = "0" + hex0;
                    while (hex1.length() < 4) hex1 = "0" + hex1;
                    builder.append("[`\\u").append(hex0).append("\\u").append(hex1).append("`]   ");
                }
                builder.append(String.valueOf(chars)).append("   _").append(Character.getName(code)).append("_");
            });
            event.reply(builder.toString());
        }
    }

    public class User extends Command {
        public User() {
            this.name = "user";
            this.category = new Command.Category("Utils");
            this.hidden = false;
            this.help = "Gets infos about the user";
            this.arguments = "<User>";
            this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        }

        @Override
        protected void execute(@NotNull CommandEvent event, I18nContext context) {
            @NotNull String[] args = event.getArgs().split(" ");
            Member memb;
            if (event.getMessage().getMentionedUsers().size() >= 1) {
                memb = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
            } else {
                memb = event.getMember();
            }

            String NAME = memb.getEffectiveName();
            @NotNull String TAG = memb.getUser().getName() + "#" + memb.getUser().getDiscriminator();
            @NotNull String GUILD_JOIN_DATE = memb.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME);
            @NotNull String DISCORD_JOINED_DATE = memb.getUser().getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME);
            String ID = memb.getUser().getId();
            String STATUS = memb.getOnlineStatus().getKey();
            @NotNull StringBuilder ROLES = new StringBuilder();

            String AVATAR = memb.getUser().getAvatarUrl();
            String GAME;
            try {
                GAME = memb.getGame().getName();
            } catch (Exception e2) {
                GAME = "-/-";
            }
            for (@NotNull Role r : memb.getRoles()) {
                ROLES.append(r.getName()).append(", ");
            }
            if (ROLES.length() > 0) {
                ROLES = new StringBuilder(ROLES.substring(0, ROLES.length() - 2));
            } else {
                ROLES = new StringBuilder("No roles on this server.");
            }
            if (AVATAR == null) {
                AVATAR = "No Avatar";
            }
            EmbedBuilder builder = new EmbedBuilder().setColor(Color.GREEN);
            builder.setDescription(":spy:   **User information for " + memb.getUser().getName() + ":**").addField("Name / Nickname", NAME, false).addField("User Tag", TAG, false).addField("ID", ID, false).addField("Current Status", STATUS, false).addField("Current Game", GAME, false).addField("Roles", ROLES.toString(), false).addField("Guild Joined", GUILD_JOIN_DATE, false).addField("Discord Joined", DISCORD_JOINED_DATE, false).addField("Avatar-URL", AVATAR, false);
            if (!Objects.equals(AVATAR, "No Avatar")) {
                builder.setThumbnail(AVATAR);
            }
            event.reply(builder.build());
        }
    }
}
