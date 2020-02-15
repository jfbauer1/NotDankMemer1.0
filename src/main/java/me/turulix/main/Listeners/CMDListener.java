/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Listeners;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.CommandListener;
import me.turulix.main.Logger;
import me.turulix.main.UtilClasses.FormatUtil;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class CMDListener implements CommandListener, EventListener {
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        switch (event.getClass().getName()) {
            case "net.dv8tion.jda.api.events.message.MessageReceivedEvent":
                onMessage((MessageReceivedEvent) event);
                break;
            case "net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent":
                break;
            case "net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent":
                break;
            case "net.dv8tion.jda.api.events.user.update.UserUpdateGameEvent":
                break;
            case "net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent":
                break;
            case "net.dv8tion.jda.api.events.StatusChangeEvent":
                break;
            case "net.dv8tion.jda.api.events.http.HttpRequestEvent":
                break;
            case "net.dv8tion.jda.api.events.guild.GuildReadyEvent":
                break;
            case "net.dv8tion.jda.api.events.ReadyEvent":
                break;
            case "net.dv8tion.jda.api.events.user.UserTypingEvent":
                break;
            case "net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent":
                break;
            case "net.dv8tion.jda.api.events.channel.priv.PrivateChannelCreateEvent":
                break;
            case "net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent":
                break;
            case "net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent":
                break;
            case "net.dv8tion.jda.api.events.message.MessageUpdateEvent":
                break;
            case "net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent":
                break;
            case "net.dv8tion.jda.api.events.message.MessageEmbedEvent":
                break;
            case "net.dv8tion.jda.api.events.guild.voice.GuildVoiceSuppressEvent":
                break;
            case "net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent":
                break;
            case "net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent":
                break;
            case "net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent":
                break;
            case "net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent":
                break;
            case "net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent":
                break;
            default:
                Logger.info(event.getClass().getName());
        }
    }

    @Override
    public void onCommand(CommandEvent event, Command command) {
    }

    @Override
    public void onCompletedCommand(CommandEvent event, Command command) {
    }

    @Override
    public void onTerminatedCommand(CommandEvent event, Command command) {
    }

    @Override
    public void onNonCommandMessage(MessageReceivedEvent e) {
    }

    @Override
    public void onCommandException(@NotNull CommandEvent event, Command command, @NotNull Throwable throwable) {
        Logger.error(event, throwable);
        event.reply("Hmmm strange something didn't work there :3\nI told my makers to look into this. :c");
    }

    private void onMessage(MessageReceivedEvent e) {
        if (e.isFromType(ChannelType.TEXT)) {
            if (e.getGuild().getIdLong() == 264445053596991498L) {
                return;
            }
        }
        if (e.getChannelType().isGuild()) {
            if (e.getMessage().getEmbeds().size() >= 1) {
                for (@NotNull MessageEmbed embed : e.getMessage().getEmbeds()) {
                    String message = FormatUtil.removeEscapeChars(embed.toString());
                    Logger.info("[" + e.getGuild().getName() + "] " + "[" + e.getChannel().getName() + "] " + "[EMBED] " + e.getAuthor().getName() + ": " + message);
                }
            } else {
                String message = FormatUtil.removeEscapeChars(e.getMessage().getContentRaw());
                Logger.info("[" + e.getGuild().getName() + "] " + "[" + e.getChannel().getName() + "] " + "[MSG] " + e.getAuthor().getName() + ": " + message);
            }
        } else if (e.getChannelType() == ChannelType.PRIVATE) {
            String message = FormatUtil.removeEscapeChars(e.getMessage().getContentRaw());
            if (e.getAuthor() == e.getJDA().getSelfUser()) {
                Logger.info("[PM] " + e.getAuthor().getName() + " --> " + e.getChannel().getName() + ": " + message);
            } else {
                Logger.info("[PM] " + e.getAuthor().getName() + " --> " + e.getJDA().getSelfUser().getName() + ": " + message);
            }
        }
    }


}
