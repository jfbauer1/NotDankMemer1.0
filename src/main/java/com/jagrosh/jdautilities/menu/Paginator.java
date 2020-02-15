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
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Paginator extends Menu {
    public static final String BIG_LEFT = "\u23ea";
    public static final String LEFT = "\u25c0";
    public static final String STOP = "\u23f9";
    public static final String RIGHT = "\u25b6";
    public static final String BIG_RIGHT = "\u23e9";
    private final BiFunction<Integer, Integer, Color> color;
    private final BiFunction<Integer, Integer, String> text;
    private final int columns;
    private final int itemsPerPage;
    private final boolean showPageNumbers;
    private final boolean numberItems;
    private final List<String> strings;
    private final int pages;
    private final Consumer<Message> finalAction;
    private final boolean waitOnSinglePage;
    private final int bulkSkipNumber;
    private final boolean wrapPageEnds;
    private final String leftText;
    private final String rightText;
    private final boolean allowTextInput;

    Paginator(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit, BiFunction<Integer, Integer, Color> color, BiFunction<Integer, Integer, String> text, Consumer<Message> finalAction, int columns, int itemsPerPage, boolean showPageNumbers, boolean numberItems, List<String> items, boolean waitOnSinglePage, int bulkSkipNumber, boolean wrapPageEnds, String leftText, String rightText, boolean allowTextInput) {
        super(waiter, users, roles, timeout, unit);
        this.color = color;
        this.text = text;
        this.columns = columns;
        this.itemsPerPage = itemsPerPage;
        this.showPageNumbers = showPageNumbers;
        this.numberItems = numberItems;
        this.strings = items;
        this.pages = (int) Math.ceil((double) this.strings.size() / (double) itemsPerPage);
        this.finalAction = finalAction;
        this.waitOnSinglePage = waitOnSinglePage;
        this.bulkSkipNumber = bulkSkipNumber;
        this.wrapPageEnds = wrapPageEnds;
        this.leftText = leftText;
        this.rightText = rightText;
        this.allowTextInput = allowTextInput;
    }

    @Override
    public void display(MessageChannel channel) {
        this.paginate(channel, 1);
    }

    @Override
    public void display(Message message) {
        this.paginate(message, 1);
    }

    public void paginate(MessageChannel channel, int pageNum) {
        if (pageNum < 1) {
            pageNum = 1;
        } else if (pageNum > this.pages) {
            pageNum = this.pages;
        }
        Message msg = this.renderPage(pageNum);
        this.initialize(channel.sendMessage(msg), pageNum);
    }

    public void paginate(Message message, int pageNum) {
        if (pageNum < 1) {
            pageNum = 1;
        } else if (pageNum > this.pages) {
            pageNum = this.pages;
        }
        Message msg = this.renderPage(pageNum);
        this.initialize(message.editMessage(msg), pageNum);
    }

    private void initialize(RestAction<Message> action, int pageNum) {
        action.queue(m -> {
            if (this.pages > 1) {
                if (this.bulkSkipNumber > 1) {
                    m.addReaction(BIG_LEFT).queue();
                }
                m.addReaction(LEFT).queue();
                m.addReaction(STOP).queue();
                if (this.bulkSkipNumber > 1) {
                    m.addReaction(RIGHT).queue();
                }
                m.addReaction(this.bulkSkipNumber > 1 ? BIG_RIGHT : RIGHT).queue(v -> this.pagination(m, pageNum), t -> this.pagination(m, pageNum));
            } else if (this.waitOnSinglePage) {
                m.addReaction(STOP).queue(v -> this.paginationWithoutTextInput(m, pageNum), t -> this.paginationWithoutTextInput(m, pageNum));
            } else {
                this.finalAction.accept(m);
            }
        });
    }

    private void pagination(Message message, int pageNum) {
        if (this.allowTextInput || this.leftText != null && this.rightText != null) {
            this.paginationWithTextInput(message, pageNum);
        } else {
            this.paginationWithoutTextInput(message, pageNum);
        }
    }

    private void paginationWithTextInput(Message message, int pageNum) {
        this.waiter.waitForEvent(GenericMessageEvent.class, event -> {
            if (event instanceof MessageReactionAddEvent) {
                return this.checkReaction((MessageReactionAddEvent) event, message.getIdLong());
            }
            if (event instanceof MessageReceivedEvent) {
                MessageReceivedEvent mre = (MessageReceivedEvent) event;
                if (!mre.getChannel().equals(message.getChannel())) {
                    return false;
                }
                String rawContent = mre.getMessage().getContentRaw().trim();
                if (this.leftText != null && this.rightText != null && (rawContent.equalsIgnoreCase(this.leftText) || rawContent.equalsIgnoreCase(this.rightText))) {
                    return this.isValidUser(mre.getAuthor(), mre.getGuild());
                }
                if (this.allowTextInput) {
                    try {
                        int i = Integer.parseInt(rawContent);
                        if (1 <= i && i <= this.pages && i != pageNum) {
                            return this.isValidUser(mre.getAuthor(), mre.getGuild());
                        }
                    } catch (NumberFormatException i) {
                        // empty catch block
                    }
                }
            }
            return false;
        }, event -> {
            if (event instanceof MessageReactionAddEvent) {
                this.handleMessageReactionAddAction((MessageReactionAddEvent) event, message, pageNum);
            } else {
                MessageReceivedEvent mre = (MessageReceivedEvent) event;
                String rawContent = mre.getMessage().getContentRaw().trim();
                int targetPage = this.leftText != null && rawContent.equalsIgnoreCase(this.leftText) && (1 < pageNum || this.wrapPageEnds) ? (pageNum - 1 < 1 && this.wrapPageEnds ? this.pages : pageNum - 1) : (this.rightText != null && rawContent.equalsIgnoreCase(this.rightText) && (pageNum < this.pages || this.wrapPageEnds) ? (pageNum + 1 > this.pages && this.wrapPageEnds ? 1 : pageNum + 1) : Integer.parseInt(rawContent));
                message.editMessage(this.renderPage(targetPage)).queue(m -> this.pagination(m, targetPage));
                mre.getMessage().delete().queue(v -> {
                }, t -> {
                });
            }
        }, this.timeout, this.unit, () -> this.finalAction.accept(message));
    }

    private void paginationWithoutTextInput(Message message, int pageNum) {
        this.waiter.waitForEvent(MessageReactionAddEvent.class, event -> this.checkReaction(event, message.getIdLong()), event -> this.handleMessageReactionAddAction(event, message, pageNum), this.timeout, this.unit, () -> this.finalAction.accept(message));
    }

    private boolean checkReaction(MessageReactionAddEvent event, long messageId) {
        if (event.getMessageIdLong() != messageId) {
            return false;
        }
        switch (event.getReactionEmote().getName()) {
            case "\u25c0":
            case "\u23f9":
            case "\u25b6": {
                return this.isValidUser(event.getUser(), event.getGuild());
            }
            case "\u23ea":
            case "\u23e9": {
                return this.bulkSkipNumber > 1 && this.isValidUser(event.getUser(), event.getGuild());
            }
        }
        return false;
    }

    private void handleMessageReactionAddAction(MessageReactionAddEvent event, Message message, int pageNum) {
        int newPageNum = pageNum;
        switch (event.getReaction().getReactionEmote().getName()) {
            case "\u25c0": {
                if (newPageNum == 1 && this.wrapPageEnds) {
                    newPageNum = this.pages + 1;
                }
                if (newPageNum <= 1) break;
                --newPageNum;
                break;
            }
            case "\u25b6": {
                if (newPageNum == this.pages && this.wrapPageEnds) {
                    newPageNum = 0;
                }
                if (newPageNum >= this.pages) break;
                ++newPageNum;
                break;
            }
            case "\u23ea": {
                if (newPageNum <= 1 && !this.wrapPageEnds) break;
                for (int i = 1; (newPageNum > 1 || this.wrapPageEnds) && i < this.bulkSkipNumber; --newPageNum, ++i) {
                    if (newPageNum != 1 || !this.wrapPageEnds) continue;
                    newPageNum = this.pages + 1;
                }
                break;
            }
            case "\u23e9": {
                if (newPageNum >= this.pages && !this.wrapPageEnds) break;
                for (int i = 1; (newPageNum < this.pages || this.wrapPageEnds) && i < this.bulkSkipNumber; ++newPageNum, ++i) {
                    if (newPageNum != this.pages || !this.wrapPageEnds) continue;
                    newPageNum = 0;
                }
                break;
            }
            case "\u23f9": {
                this.finalAction.accept(message);
                return;
            }
        }
        try {
            event.getReaction().removeReaction(event.getUser()).queue();
        } catch (PermissionException permissionException) {
            // empty catch block
        }
        int n = newPageNum;
        message.editMessage(this.renderPage(newPageNum)).queue(m -> this.pagination(m, n));
    }

    private Message renderPage(int pageNum) {
        int end;
        MessageBuilder mbuilder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder();
        int start = (pageNum - 1) * this.itemsPerPage;
        int n = end = this.strings.size() < pageNum * this.itemsPerPage ? this.strings.size() : pageNum * this.itemsPerPage;
        if (this.columns == 1) {
            StringBuilder sbuilder = new StringBuilder();
            for (int i = start; i < end; ++i) {
                sbuilder.append("\n").append(this.numberItems ? "`" + (i + 1) + ".` " : "").append(this.strings.get(i));
            }
            ebuilder.setDescription(sbuilder.toString());
        } else {
            int per = (int) Math.ceil((double) (end - start) / (double) this.columns);
            for (int k = 0; k < this.columns; ++k) {
                StringBuilder strbuilder = new StringBuilder();
                for (int i = start + k * per; i < end && i < start + (k + 1) * per; ++i) {
                    strbuilder.append("\n").append(this.numberItems ? "" + (i + 1) + ". " : "").append(this.strings.get(i));
                }
                ebuilder.addField("", strbuilder.toString(), true);
            }
        }
        ebuilder.setColor(this.color.apply(pageNum, this.pages));
        if (this.showPageNumbers) {
            ebuilder.setFooter("Page " + pageNum + "/" + this.pages, null);
        }
        mbuilder.setEmbed(ebuilder.build());
        if (this.text != null) {
            mbuilder.append(this.text.apply(pageNum, this.pages));
        }
        return mbuilder.build();
    }

    public static class Builder extends Menu.Builder<Builder, Paginator> {
        private final List<String> strings = new LinkedList<String>();
        private BiFunction<Integer, Integer, Color> color = (page, pages) -> null;
        private BiFunction<Integer, Integer, String> text = (page, pages) -> null;
        private Consumer<Message> finalAction = m -> m.delete().queue();
        private int columns = 1;
        private int itemsPerPage = 12;
        private boolean showPageNumbers = true;
        private boolean numberItems = false;
        private boolean waitOnSinglePage = false;
        private int bulkSkipNumber = 1;
        private boolean wrapPageEnds = false;
        private String textToLeft = null;
        private String textToRight = null;
        private boolean allowTextInput = false;

        @Override
        public Paginator build() {
            Checks.check(this.waiter != null, "Must set an EventWaiter");
            Checks.check(!this.strings.isEmpty(), "Must include at least one item to paginate");
            return new Paginator(this.waiter, this.users, this.roles, this.timeout, this.unit, this.color, this.text, this.finalAction, this.columns, this.itemsPerPage, this.showPageNumbers, this.numberItems, this.strings, this.waitOnSinglePage, this.bulkSkipNumber, this.wrapPageEnds, this.textToLeft, this.textToRight, this.allowTextInput);
        }

        public Builder setColor(Color color) {
            this.color = (i0, i1) -> color;
            return this;
        }

        public Builder setColor(BiFunction<Integer, Integer, Color> colorBiFunction) {
            this.color = colorBiFunction;
            return this;
        }

        public Builder setText(String text) {
            this.text = (i0, i1) -> text;
            return this;
        }

        public Builder setText(BiFunction<Integer, Integer, String> textBiFunction) {
            this.text = textBiFunction;
            return this;
        }

        public Builder setFinalAction(Consumer<Message> finalAction) {
            this.finalAction = finalAction;
            return this;
        }

        public Builder setColumns(int columns) {
            if (columns < 1 || columns > 3) {
                throw new IllegalArgumentException("Only 1, 2, or 3 columns are supported");
            }
            this.columns = columns;
            return this;
        }

        public Builder setItemsPerPage(int num) {
            if (num < 1) {
                throw new IllegalArgumentException("There must be at least one item per page");
            }
            this.itemsPerPage = num;
            return this;
        }

        public Builder showPageNumbers(boolean show) {
            this.showPageNumbers = show;
            return this;
        }

        public Builder useNumberedItems(boolean number) {
            this.numberItems = number;
            return this;
        }

        public Builder waitOnSinglePage(boolean wait) {
            this.waitOnSinglePage = wait;
            return this;
        }

        public Builder clearItems() {
            this.strings.clear();
            return this;
        }

        public /* varargs */ Builder addItems(String... items) {
            this.strings.addAll(Arrays.asList(items));
            return this;
        }

        public /* varargs */ Builder setItems(String... items) {
            this.strings.clear();
            this.strings.addAll(Arrays.asList(items));
            return this;
        }

        public Builder setBulkSkipNumber(int bulkSkipNumber) {
            this.bulkSkipNumber = Math.max(bulkSkipNumber, 1);
            return this;
        }

        public Builder wrapPageEnds(boolean wrapPageEnds) {
            this.wrapPageEnds = wrapPageEnds;
            return this;
        }

        public Builder allowTextInput(boolean allowTextInput) {
            this.allowTextInput = allowTextInput;
            return this;
        }

        public Builder setLeftRightText(String left, String right) {
            if (left == null || right == null) {
                this.textToLeft = null;
                this.textToRight = null;
            } else {
                this.textToLeft = left;
                this.textToRight = right;
            }
            return this;
        }
    }

}

