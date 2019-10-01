/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.menu;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.utils.Checks;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OrderedMenu extends Menu {
    public static final String[] NUMBERS = new String[]{"1\u20e3", "2\u20e3", "3\u20e3", "4\u20e3", "5\u20e3", "6\u20e3", "7\u20e3", "8\u20e3", "9\u20e3", "\ud83d\udd1f"};
    public static final String[] LETTERS = new String[]{"\ud83c\udde6", "\ud83c\udde7", "\ud83c\udde8", "\ud83c\udde9", "\ud83c\uddea", "\ud83c\uddeb", "\ud83c\uddec", "\ud83c\udded", "\ud83c\uddee", "\ud83c\uddef"};
    public static final String CANCEL = "\u274c";
    private final Color color;
    private final String text;
    private final String description;
    private final List<String> choices;
    private final BiConsumer<Message, Integer> action;
    private final Consumer<Message> cancel;
    private final boolean useLetters;
    private final boolean allowTypedInput;
    private final boolean useCancel;

    OrderedMenu(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit, Color color, String text, String description, List<String> choices, BiConsumer<Message, Integer> action, Consumer<Message> cancel, boolean useLetters, boolean allowTypedInput, boolean useCancel) {
        super(waiter, users, roles, timeout, unit);
        this.color = color;
        this.text = text;
        this.description = description;
        this.choices = choices;
        this.action = action;
        this.cancel = cancel;
        this.useLetters = useLetters;
        this.allowTypedInput = allowTypedInput;
        this.useCancel = useCancel;
    }

    @Override
    public void display(MessageChannel channel) {
        if (channel.getType() == ChannelType.TEXT && !this.allowTypedInput && !PermissionUtil.checkPermission((TextChannel) channel, ((TextChannel) channel).getGuild().getSelfMember(), Permission.MESSAGE_ADD_REACTION)) {
            throw new PermissionException("Must be able to add reactions if not allowing typed input!");
        }
        this.initialize(channel.sendMessage(this.getMessage()));
    }

    @Override
    public void display(Message message) {
        if (message.getChannelType() == ChannelType.TEXT && !this.allowTypedInput && !PermissionUtil.checkPermission(message.getTextChannel(), message.getGuild().getSelfMember(), Permission.MESSAGE_ADD_REACTION)) {
            throw new PermissionException("Must be able to add reactions if not allowing typed input!");
        }
        this.initialize(message.editMessage(this.getMessage()));
    }

    private void initialize(RestAction<Message> ra) {
        ra.queue(m -> {
            try {
                for (int i = 0; i < this.choices.size(); ++i) {
                    if (i < this.choices.size() - 1) {
                        m.addReaction(this.getEmoji(i)).queue();
                        continue;
                    }
                    RestAction<Void> re = m.addReaction(this.getEmoji(i));
                    if (this.useCancel) {
                        re.queue();
                        re = m.addReaction(CANCEL);
                    }
                    re.queue(v -> {
                        if (this.allowTypedInput) {
                            this.waitGeneric(m);
                        } else {
                            this.waitReactionOnly(m);
                        }
                    });
                }
            } catch (PermissionException ex) {
                if (this.allowTypedInput) {
                    this.waitGeneric(m);
                }
                this.waitReactionOnly(m);
            }
        });
    }

    private void waitGeneric(Message m) {
        this.waiter.waitForEvent(GenericMessageEvent.class, e -> {
            if (e instanceof MessageReactionAddEvent) {
                return this.isValidReaction(m, (MessageReactionAddEvent) e);
            }
            if (e instanceof MessageReceivedEvent) {
                return this.isValidMessage(m, (MessageReceivedEvent) e);
            }
            return false;
        }, e -> {
            m.delete().queue();
            if (e instanceof MessageReactionAddEvent) {
                MessageReactionAddEvent event = (MessageReactionAddEvent) e;
                if (event.getReaction().getReactionEmote().getName().equals(CANCEL)) {
                    this.cancel.accept(m);
                } else {
                    this.action.accept(m, this.getNumber(event.getReaction().getReactionEmote().getName()));
                }
            } else if (e instanceof MessageReceivedEvent) {
                MessageReceivedEvent event = (MessageReceivedEvent) e;
                int num = this.getMessageNumber(event.getMessage().getContentRaw());
                if (num < 0 || num > this.choices.size()) {
                    this.cancel.accept(m);
                } else {
                    this.action.accept(m, num);
                }
            }
        }, this.timeout, this.unit, () -> this.cancel.accept(m));
    }

    private void waitReactionOnly(Message m) {
        this.waiter.waitForEvent(MessageReactionAddEvent.class, e -> this.isValidReaction(m, e), e -> {
            m.delete().queue();
            if (e.getReaction().getReactionEmote().getName().equals(CANCEL)) {
                this.cancel.accept(m);
            } else {
                this.action.accept(m, this.getNumber(e.getReaction().getReactionEmote().getName()));
            }
        }, this.timeout, this.unit, () -> this.cancel.accept(m));
    }

    private Message getMessage() {
        MessageBuilder mbuilder = new MessageBuilder();
        if (this.text != null) {
            mbuilder.append(this.text);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.choices.size(); ++i) {
            sb.append("\n").append(this.getEmoji(i)).append(" ").append(this.choices.get(i));
        }
        mbuilder.setEmbed(new EmbedBuilder().setColor(this.color).setDescription(this.description == null ? sb.toString() : this.description + sb.toString()).build());
        return mbuilder.build();
    }

    private boolean isValidReaction(Message m, MessageReactionAddEvent e) {
        if (!e.getMessageId().equals(m.getId())) {
            return false;
        }
        if (!this.isValidUser(e.getUser(), e.getGuild())) {
            return false;
        }
        if (e.getReaction().getReactionEmote().getName().equals(CANCEL)) {
            return true;
        }
        int num = this.getNumber(e.getReaction().getReactionEmote().getName());
        return num >= 0 && num <= this.choices.size();
    }

    private boolean isValidMessage(Message m, MessageReceivedEvent e) {
        if (!e.getChannel().equals(m.getChannel())) {
            return false;
        }
        return this.isValidUser(e.getAuthor(), e.getGuild());
    }

    private String getEmoji(int number) {
        return this.useLetters ? LETTERS[number] : NUMBERS[number];
    }

    private int getNumber(String emoji) {
        String[] array = this.useLetters ? LETTERS : NUMBERS;
        for (int i = 0; i < array.length; ++i) {
            if (!array[i].equals(emoji)) continue;
            return i + 1;
        }
        return -1;
    }

    private int getMessageNumber(String message) {
        if (this.useLetters) {
            return message.length() == 1 ? " abcdefghij".indexOf(message.toLowerCase()) : -1;
        }
        if (message.length() == 1) {
            return " 123456789".indexOf(message);
        }
        return message.equals("10") ? 10 : -1;
    }

    public static class Builder extends Menu.Builder<Builder, OrderedMenu> {
        private final List<String> choices = new LinkedList<String>();
        private Color color;
        private String text;
        private String description;
        private BiConsumer<Message, Integer> selection;
        private Consumer<Message> cancel = m -> {
        };
        private boolean useLetters = false;
        private boolean allowTypedInput = true;
        private boolean addCancel = false;

        @Override
        public OrderedMenu build() {
            Checks.check(this.waiter != null, "Must set an EventWaiter");
            Checks.check(!this.choices.isEmpty(), "Must have at least one choice");
            Checks.check(this.choices.size() <= 10, "Must have no more than ten choices");
            Checks.check(this.selection != null, "Must provide an selection consumer");
            Checks.check(this.text != null || this.description != null, "Either text or description must be set");
            return new OrderedMenu(this.waiter, this.users, this.roles, this.timeout, this.unit, this.color, this.text, this.description, this.choices, this.selection, this.cancel, this.useLetters, this.allowTypedInput, this.addCancel);
        }

        public Builder setColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder useLetters() {
            this.useLetters = true;
            return this;
        }

        public Builder useNumbers() {
            this.useLetters = false;
            return this;
        }

        public Builder allowTextInput(boolean allow) {
            this.allowTypedInput = allow;
            return this;
        }

        public Builder useCancelButton(boolean use) {
            this.addCancel = use;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setSelection(BiConsumer<Message, Integer> selection) {
            this.selection = selection;
            return this;
        }

        public Builder setCancel(Consumer<Message> cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder addChoice(String choice) {
            Checks.check(this.choices.size() < 10, "Cannot set more than 10 choices");
            this.choices.add(choice);
            return this;
        }

        public /* varargs */ Builder addChoices(String... choices) {
            for (String choice : choices) {
                this.addChoice(choice);
            }
            return this;
        }

        public /* varargs */ Builder setChoices(String... choices) {
            this.clearChoices();
            return this.addChoices(choices);
        }

        public Builder clearChoices() {
            this.choices.clear();
            return this;
        }
    }

}

