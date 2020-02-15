/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Miscellaneous;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.FormatUtil;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.awt.*;

@DankCommand
public class Urban extends Command {
    public Urban() {
        this.name = "urban";
        this.category = new Category("Misc");
        this.arguments = "<Term>";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.help = "Searches a term in the urban dictionary";
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        @NotNull String term = event.getArgs().replace(" ", "%20").trim();
        if (!event.getMessage().getContentRaw().equalsIgnoreCase("")) {
            @Nullable String message = Utils.getUrl("http://api.urbandictionary.com/v0/define?term=" + term);
            @NotNull JSONObject obj = new JSONObject(message);
            if (obj.getJSONArray("list").length() == 0) {
                event.reply("Nothing found under the term: " + event.getArgs());
                return;
            }
            @NotNull JSONObject firstObject = (JSONObject) obj.getJSONArray("list").get(0);
            @NotNull EmbedBuilder eb = new EmbedBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[Definition for the word: " + FormatUtil.decodeUTF8(firstObject.getString("word")).trim().substring(0, Math.min(FormatUtil.decodeUTF8(firstObject.getString("word")).trim().length(), 60)) + "]" + "(" + firstObject.getString("permalink") + ")");
            //eb.setAuthor(firstObject.getString("author"));
            eb.setColor(new Color(42, 255, 140));


            stringBuilder.append("\n__**Definition:**__\n" + FormatUtil.decodeUTF8(firstObject.getString("definition")));
            stringBuilder.append("\n\n__**Example:**__\n" + FormatUtil.decodeUTF8(firstObject.getString("example")));
            eb.setDescription(stringBuilder.toString().substring(0, Math.min(stringBuilder.toString().length(), 2045)));
            if (eb.length() == 2045) eb.appendDescription("...");
            event.reply(eb.build());
        }
    }
}
