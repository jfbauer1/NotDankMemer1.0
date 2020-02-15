/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:52.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

@DankCommand
public class ChuckCommand extends Command {
    public ChuckCommand() {
        this.name = "chuck";
        this.category = new Command.Category("Fun");
        this.hidden = false;
        this.help = "I cant tell you what it does would be dead before";
        this.arguments = "";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        @Nullable String msg = Utils.getUrl("http://api.icndb.com/jokes/random");
        if (msg == null) throw new NullPointerException("Chuck joke is null.");
        @Nullable JSONObject obj = new JSONObject(msg);
        String Quote = obj.getJSONObject("value").getString("joke");
        event.reply(Quote.replaceAll("&quot;", "**"));
    }
}
