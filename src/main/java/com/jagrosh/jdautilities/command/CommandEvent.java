/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command;

import com.jagrosh.jdautilities.command.impl.CommandClientImpl;
import com.jagrosh.jdautilities.commons.utils.SafeIdUtil;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.utils.Checks;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class CommandEvent {
    public static int MAX_MESSAGES = 2;
    private final MessageReceivedEvent event;
    private final CommandClient client;
    private String args;

    public CommandEvent(MessageReceivedEvent event, String args, CommandClient client) {
        this.event = event;
        this.args = args == null ? "" : args;
        this.client = client;
    }

    public static ArrayList<String> splitMessage(String stringtoSend) {
        ArrayList<String> msgs = new ArrayList<String>();
        if (stringtoSend != null) {
            stringtoSend = stringtoSend.replace("@everyone", "@\u0435veryone").replace("@here", "@h\u0435re").trim();
            while (stringtoSend.length() > 2000) {
                String temp;
                int leeway = 2000 - stringtoSend.length() % 2000;
                int index = stringtoSend.lastIndexOf("\n", 2000);
                if (index < leeway) {
                    index = stringtoSend.lastIndexOf(" ", 2000);
                }
                if (index < leeway) {
                    index = 2000;
                }
                if (!(temp = stringtoSend.substring(0, index).trim()).equals("")) {
                    msgs.add(temp);
                }
                stringtoSend = stringtoSend.substring(index).trim();
            }
            if (!stringtoSend.equals("")) {
                msgs.add(stringtoSend);
            }
        }
        return msgs;
    }

    public String getArgs() {
        return this.args;
    }

    void setArgs(String args) {
        this.args = args;
    }

    public MessageReceivedEvent getEvent() {
        return this.event;
    }

    public CommandClient getClient() {
        return this.client;
    }

    public void linkId(Message message) {
        Checks.check(message.getAuthor().equals(this.getSelfUser()), "Attempted to link a Message who's author was not the bot!");
        ((CommandClientImpl) this.client).linkIds(this.event.getMessageIdLong(), message);
    }

    public void reply(String message) {
        this.sendMessage(this.event.getChannel(), message);
    }

    public void reply(String message, Consumer<Message> success) {
        this.sendMessage(this.event.getChannel(), message, success);
    }

    public void reply(String message, Consumer<Message> success, Consumer<Throwable> failure) {
        this.sendMessage(this.event.getChannel(), message, success, failure);
    }

    public void reply(MessageEmbed embed) {
        this.event.getChannel().sendMessage(embed).queue(m -> {
            if (this.event.isFromType(ChannelType.TEXT)) {
                this.linkId(m);
            }
        });
    }

    public void reply(MessageEmbed embed, Consumer<Message> success) {
        this.event.getChannel().sendMessage(embed).queue(m -> {
            if (this.event.isFromType(ChannelType.TEXT)) {
                this.linkId(m);
            }
            success.accept(m);
        });
    }

    public void reply(MessageEmbed embed, Consumer<Message> success, Consumer<Throwable> failure) {
        this.event.getChannel().sendMessage(embed).queue(m -> {
            if (this.event.isFromType(ChannelType.TEXT)) {
                this.linkId(m);
            }
            success.accept(m);
        }, failure);
    }

    public void reply(Message message) {
        this.event.getChannel().sendMessage(message).queue(m -> {
            if (this.event.isFromType(ChannelType.TEXT)) {
                this.linkId(m);
            }
        });
    }

    public void reply(Message message, Consumer<Message> success) {
        this.event.getChannel().sendMessage(message).queue(m -> {
            if (this.event.isFromType(ChannelType.TEXT)) {
                this.linkId(m);
            }
            success.accept(m);
        });
    }

    public void reply(Message message, Consumer<Message> success, Consumer<Throwable> failure) {
        this.event.getChannel().sendMessage(message).queue(m -> {
            if (this.event.isFromType(ChannelType.TEXT)) {
                this.linkId(m);
            }
            success.accept(m);
        }, failure);
    }

    public void reply(File file, String filename) {
        this.event.getChannel().sendFile(file, filename, null).queue();
    }

    public void reply(String message, File file, String filename) {
        Message msg = message == null ? null : new MessageBuilder().append(CommandEvent.splitMessage(message).get(0)).build();
        this.event.getChannel().sendFile(file, filename, msg).queue();
    }

    public /* varargs */ void replyFormatted(String format, Object... args) {
        this.sendMessage(this.event.getChannel(), String.format(format, args));
    }

    public void replyOrAlternate(MessageEmbed embed, String alternateMessage) {
        try {
            this.event.getChannel().sendMessage(embed).queue();
        } catch (PermissionException e) {
            this.reply(alternateMessage);
        }
    }

    public void replyOrAlternate(String message, File file, String filename, String alternateMessage) {
        Message msg = message == null ? null : new MessageBuilder().append(CommandEvent.splitMessage(message).get(0)).build();
        try {
            this.event.getChannel().sendFile(file, filename, msg).queue();
        } catch (Exception e) {
            this.reply(alternateMessage);
        }
    }

    public void replyInDm(String message) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.reply(message);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> this.sendMessage(pc, message));
        }
    }

    public void replyInDm(String message, Consumer<Message> success) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.reply(message, success);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> this.sendMessage(pc, message, success));
        }
    }

    public void replyInDm(String message, Consumer<Message> success, Consumer<Throwable> failure) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.reply(message, success, failure);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> this.sendMessage(pc, message, success, failure), failure);
        }
    }

    public void replyInDm(MessageEmbed embed) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.reply(embed);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(embed).queue());
        }
    }

    public void replyInDm(MessageEmbed embed, Consumer<Message> success) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.getPrivateChannel().sendMessage(embed).queue(success);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(embed).queue(success));
        }
    }

    public void replyInDm(MessageEmbed embed, Consumer<Message> success, Consumer<Throwable> failure) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.getPrivateChannel().sendMessage(embed).queue(success, failure);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(embed).queue(success, failure), failure);
        }
    }

    public void replyInDm(Message message) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.reply(message);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(message).queue());
        }
    }

    public void replyInDm(Message message, Consumer<Message> success) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.getPrivateChannel().sendMessage(message).queue(success);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(message).queue(success));
        }
    }

    public void replyInDm(Message message, Consumer<Message> success, Consumer<Throwable> failure) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.getPrivateChannel().sendMessage(message).queue(success, failure);
        } else {
            this.event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(message).queue(success, failure), failure);
        }
    }

    public void replyInDm(String message, File file, String filename) {
        if (this.event.isFromType(ChannelType.PRIVATE)) {
            this.reply(message, file, filename);
        } else {
            Message msg = message == null ? null : new MessageBuilder().append(CommandEvent.splitMessage(message).get(0)).build();
            this.event.getAuthor().openPrivateChannel().queue(pc -> pc.sendFile(file, filename, msg).queue());
        }
    }

    public void replySuccess(String message) {
        this.reply(this.client.getSuccess() + " " + message);
    }

    public void replySuccess(String message, Consumer<Message> queue) {
        this.reply(this.client.getSuccess() + " " + message, queue);
    }

    public void replyWarning(String message) {
        this.reply(this.client.getWarning() + " " + message);
    }

    public void replyWarning(String message, Consumer<Message> queue) {
        this.reply(this.client.getWarning() + " " + message, queue);
    }

    public void replyError(String message) {
        this.reply(this.client.getError() + " " + message);
    }

    public void replyError(String message, Consumer<Message> queue) {
        this.reply(this.client.getError() + " " + message, queue);
    }

    public void reactSuccess() {
        this.react(this.client.getSuccess());
    }

    public void reactWarning() {
        this.react(this.client.getWarning());
    }

    public void reactError() {
        this.react(this.client.getError());
    }

    public void async(Runnable runnable) {
        Checks.notNull(runnable, "Runnable");
        this.client.getScheduleExecutor().submit(runnable);
    }

    private void react(String reaction) {
        if (reaction == null || reaction.isEmpty()) {
            return;
        }
        try {
            long id = SafeIdUtil.safeConvert(reaction.replaceAll("<a?:.+:(\\d+)>", "$1"));
            Emote emote = this.event.getJDA().getEmoteById(id);
            if (emote == null) {
                this.event.getMessage().addReaction(reaction).queue();
            } else {
                this.event.getMessage().addReaction(emote).queue();
            }
        } catch (PermissionException id) {
            // empty catch block
        }
    }

    private void sendMessage(MessageChannel chan, String message) {
        ArrayList<String> messages = CommandEvent.splitMessage(message);
        for (int i = 0; i < MAX_MESSAGES && i < messages.size(); ++i) {
            chan.sendMessage(messages.get(i)).queue(m -> {
                if (this.event.isFromType(ChannelType.TEXT)) {
                    this.linkId(m);
                }
            });
        }
    }

    private void sendMessage(MessageChannel chan, String message, Consumer<Message> success) {
        ArrayList<String> messages = CommandEvent.splitMessage(message);
        for (int i = 0; i < MAX_MESSAGES && i < messages.size(); ++i) {
            block4:
            {
                block3:
                {
                    if (i + 1 == MAX_MESSAGES) break block3;
                    if (i + 1 != messages.size()) break block4;
                }
                chan.sendMessage(messages.get(i)).queue(m -> {
                    if (this.event.isFromType(ChannelType.TEXT)) {
                        this.linkId(m);
                    }
                    success.accept(m);
                });
                continue;
            }
            chan.sendMessage(messages.get(i)).queue(m -> {
                if (this.event.isFromType(ChannelType.TEXT)) {
                    this.linkId(m);
                }
            });
        }
    }

    private void sendMessage(MessageChannel chan, String message, Consumer<Message> success, Consumer<Throwable> failure) {
        ArrayList<String> messages = CommandEvent.splitMessage(message);
        for (int i = 0; i < MAX_MESSAGES && i < messages.size(); ++i) {
            block4:
            {
                block3:
                {
                    if (i + 1 == MAX_MESSAGES) break block3;
                    if (i + 1 != messages.size()) break block4;
                }
                chan.sendMessage(messages.get(i)).queue(m -> {
                    if (this.event.isFromType(ChannelType.TEXT)) {
                        this.linkId(m);
                    }
                    success.accept(m);
                }, failure);
                continue;
            }
            chan.sendMessage(messages.get(i)).queue(m -> {
                if (this.event.isFromType(ChannelType.TEXT)) {
                    this.linkId(m);
                }
            });
        }
    }

    public SelfUser getSelfUser() {
        return this.event.getJDA().getSelfUser();
    }

    public Member getSelfMember() {
        return this.event.getGuild() == null ? null : this.event.getGuild().getSelfMember();
    }

    public boolean isOwner() {
        if (this.event.getAuthor().getId().equals(this.getClient().getOwnerId())) {
            return true;
        }
        if (this.getClient().getCoOwnerIds() == null) {
            return false;
        }
        for (String id : this.getClient().getCoOwnerIds()) {
            if (!id.equals(this.event.getAuthor().getId())) continue;
            return true;
        }
        return false;
    }

    public User getAuthor() {
        return this.event.getAuthor();
    }

    public MessageChannel getChannel() {
        return this.event.getChannel();
    }

    public ChannelType getChannelType() {
        return this.event.getChannelType();
    }

    public Group getGroup() {
        return this.event.getGroup();
    }

    public Guild getGuild() {
        return this.event.getGuild();
    }

    public JDA getJDA() {
        return this.event.getJDA();
    }

    public Member getMember() {
        return this.event.getMember();
    }

    public Message getMessage() {
        return this.event.getMessage();
    }

    public PrivateChannel getPrivateChannel() {
        return this.event.getPrivateChannel();
    }

    public long getResponseNumber() {
        return this.event.getResponseNumber();
    }

    public TextChannel getTextChannel() {
        return this.event.getTextChannel();
    }

    public boolean isFromType(ChannelType channelType) {
        return this.event.isFromType(channelType);
    }
}

