/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.menu;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.Checks;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SelectionDialog extends Menu {
    public static final String UP = "\ud83d\udd3c";
    public static final String DOWN = "\ud83d\udd3d";
    public static final String SELECT = "\u2705";
    public static final String CANCEL = "\u274e";
    private final List<String> choices;
    private final String leftEnd;
    private final String rightEnd;
    private final String defaultLeft;
    private final String defaultRight;
    private final Function<Integer, Color> color;
    private final boolean loop;
    private final Function<Integer, String> text;
    private final BiConsumer<Message, Integer> success;
    private final Consumer<Message> cancel;

    SelectionDialog(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit, List<String> choices, String leftEnd, String rightEnd, String defaultLeft, String defaultRight, Function<Integer, Color> color, boolean loop, BiConsumer<Message, Integer> success, Consumer<Message> cancel, Function<Integer, String> text) {
        super(waiter, users, roles, timeout, unit);
        this.choices = choices;
        this.leftEnd = leftEnd;
        this.rightEnd = rightEnd;
        this.defaultLeft = defaultLeft;
        this.defaultRight = defaultRight;
        this.color = color;
        this.loop = loop;
        this.success = success;
        this.cancel = cancel;
        this.text = text;
    }

    @Override
    public void display(MessageChannel channel) {
        this.showDialog(channel, 1);
    }

    @Override
    public void display(Message message) {
        this.showDialog(message, 1);
    }

    public void showDialog(MessageChannel channel, int selection) {
        if (selection < 1) {
            selection = 1;
        } else if (selection > this.choices.size()) {
            selection = this.choices.size();
        }
        Message msg = this.render(selection);
        this.initialize(channel.sendMessage(msg), selection);
    }

    public void showDialog(Message message, int selection) {
        if (selection < 1) {
            selection = 1;
        } else if (selection > this.choices.size()) {
            selection = this.choices.size();
        }
        Message msg = this.render(selection);
        this.initialize(message.editMessage(msg), selection);
    }

    private void initialize(RestAction<Message> action, int selection) {
        action.queue(m -> {
            if (this.choices.size() > 1) {
                m.addReaction(UP).queue();
                m.addReaction(SELECT).queue();
                m.addReaction(CANCEL).queue();
                m.addReaction(DOWN).queue(v -> this.selectionDialog(m, selection), v -> this.selectionDialog(m, selection));
            } else {
                m.addReaction(SELECT).queue();
                m.addReaction(CANCEL).queue(v -> this.selectionDialog(m, selection), v -> this.selectionDialog(m, selection));
            }
        });
    }

    private void selectionDialog(Message message, int selection) {
        this.waiter.waitForEvent(MessageReactionAddEvent.class, event -> {
            if (!event.getMessageId().equals(message.getId())) {
                return false;
            }
            if (!(UP.equals(event.getReaction().getReactionEmote().getName()) || DOWN.equals(event.getReaction().getReactionEmote().getName()) || CANCEL.equals(event.getReaction().getReactionEmote().getName()) || SELECT.equals(event.getReaction().getReactionEmote().getName()))) {
                return false;
            }
            return this.isValidUser(event.getUser(), event.getGuild());
        }, event -> {
            int newSelection = selection;
            switch (event.getReaction().getReactionEmote().getName()) {
                case "\ud83d\udd3c": {
                    if (newSelection > 1) {
                        --newSelection;
                        break;
                    }
                    if (!this.loop) break;
                    newSelection = this.choices.size();
                    break;
                }
                case "\ud83d\udd3d": {
                    if (newSelection < this.choices.size()) {
                        ++newSelection;
                        break;
                    }
                    if (!this.loop) break;
                    newSelection = 1;
                    break;
                }
                case "\u2705": {
                    this.success.accept(message, selection);
                    break;
                }
                case "\u274e": {
                    this.cancel.accept(message);
                    return;
                }
            }
            try {
                event.getReaction().removeReaction(event.getUser()).queue();
            } catch (PermissionException permissionException) {
                // empty catch block
            }
            int n = newSelection;
            message.editMessage(this.render(n)).queue(m -> this.selectionDialog(m, n));
        }, this.timeout, this.unit, () -> this.cancel.accept(message));
    }

    private Message render(int selection) {
        StringBuilder sbuilder = new StringBuilder();
        for (int i = 0; i < this.choices.size(); ++i) {
            if (i + 1 == selection) {
                sbuilder.append("\n").append(this.leftEnd).append(this.choices.get(i)).append(this.rightEnd);
                continue;
            }
            sbuilder.append("\n").append(this.defaultLeft).append(this.choices.get(i)).append(this.defaultRight);
        }
        MessageBuilder mbuilder = new MessageBuilder();
        String content = this.text.apply(selection);
        if (content != null) {
            mbuilder.append(content);
        }
        return mbuilder.setEmbed(new EmbedBuilder().setColor(this.color.apply(selection)).setDescription(sbuilder.toString()).build()).build();
    }

    public static class Builder extends Menu.Builder<Builder, SelectionDialog> {
        private final List<String> choices = new LinkedList<String>();
        private String leftEnd = "";
        private String rightEnd = "";
        private String defaultLeft = "";
        private String defaultRight = "";
        private Function<Integer, Color> color = i -> null;
        private boolean loop = true;
        private Function<Integer, String> text = i -> null;
        private BiConsumer<Message, Integer> selection;
        private Consumer<Message> cancel = m -> {
        };

        @Override
        public SelectionDialog build() {
            Checks.check(this.waiter != null, "Must set an EventWaiter");
            Checks.check(!this.choices.isEmpty(), "Must have at least one choice");
            Checks.check(this.selection != null, "Must provide a selection consumer");
            return new SelectionDialog(this.waiter, this.users, this.roles, this.timeout, this.unit, this.choices, this.leftEnd, this.rightEnd, this.defaultLeft, this.defaultRight, this.color, this.loop, this.selection, this.cancel, this.text);
        }

        public Builder setColor(Color color) {
            this.color = i -> color;
            return this;
        }

        public Builder setColor(Function<Integer, Color> color) {
            this.color = color;
            return this;
        }

        public Builder setText(String text) {
            this.text = i -> text;
            return this;
        }

        public Builder setText(Function<Integer, String> text) {
            this.text = text;
            return this;
        }

        public Builder setSelectedEnds(String left, String right) {
            this.leftEnd = left;
            this.rightEnd = right;
            return this;
        }

        public Builder setDefaultEnds(String left, String right) {
            this.defaultLeft = left;
            this.defaultRight = right;
            return this;
        }

        public Builder useLooping(boolean loop) {
            this.loop = loop;
            return this;
        }

        public Builder setSelectionConsumer(BiConsumer<Message, Integer> selection) {
            this.selection = selection;
            return this;
        }

        public Builder setCanceled(Consumer<Message> cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder clearChoices() {
            this.choices.clear();
            return this;
        }

        public /* varargs */ Builder setChoices(String... choices) {
            this.choices.clear();
            this.choices.addAll(Arrays.asList(choices));
            return this;
        }

        public /* varargs */ Builder addChoices(String... choices) {
            this.choices.addAll(Arrays.asList(choices));
            return this;
        }
    }

}

