/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:56.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.examples.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import me.turulix.main.i18n.I18nContext;

/**
 * @author John Grosh (jagrosh)
 */
@CommandInfo(name = "Shutdown", description = "Safely shuts down the bot.")
@Author("John Grosh (jagrosh)")
public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        this.name = "shutdown";
        this.help = "safely shuts off the bot";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        event.reactWarning();
        event.getJDA().shutdown();
    }

}
