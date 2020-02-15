/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:52.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.Logger;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;

@DankCommand
public class FMLCommand extends Command {
    public FMLCommand() {
        this.name = "fml";
        this.category = new Command.Category("Fun");
        this.hidden = false;
        this.help = "Fuck my life :/";
        this.arguments = "";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        //TODO: Change this to use @EmbedBuilder
        try {

            Document doc = Jsoup.connect("http://fmylife.com/random").userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();
            String[] authorInfo = doc.select("#content > div > div.col-sm-8 > div > article:nth-child(2) > div > div.article-topbar").text().split(" -");
            String author = authorInfo[0].substring(3);
            String date = authorInfo[1];
            String state = null;
            if (authorInfo.length >= 3)
                state = authorInfo[2];
            String message = doc.select("#content > div > div.col-sm-8 > div > article:nth-child(2) > div > div.article-contents > a").text();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("FML By: " + author + " on" + date + (state != null ? " from" + state : ""));
            eb.setColor(new Color(91, 255, 72));
            eb.setDescription(message);
            event.reply(eb.build());
        } catch (Exception ex) {
            Logger.error(event, ex);
        }
    }
}
