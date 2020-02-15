/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 23:36.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.examples.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import me.turulix.main.i18n.I18nContext;

import java.time.temporal.ChronoUnit;

/**
 * @author John Grosh (jagrosh)
 */
@CommandInfo(name = {"Ping", "Pong"}, description = "Checks the bot's latency")
@Author("John Grosh (jagrosh)")
public class PingCommand extends Command {

    public PingCommand() {
        this.name = "ping";
        this.help = "checks the bot's latency";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        event.reply("Ping: ...", m -> {
            long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
            m.editMessage("Ping: " + ping + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue();
        });
    }

}
