/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:55.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Utils;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

@DankCommand
public class SuggestionCommand extends Command {
    public SuggestionCommand() {
        this.name = "suggest";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.help = "Sends you the \"Github\" link to open issues.";
        this.category = new Category("Miscellaneous");
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        event.reply("(This is a temporary message cause i didnt design a embed for now)\n\nUse this link: https://gitea.turulix.de/turulix/NotDankMemer/issues/new");
    }
}
