/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Nsfw;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.Logger;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.SubClasses.SubClasses;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 11.01.2019 18:12
 */
@DankCommand
public class PornhubCommand extends Command {
    //<ul id="videoSearchResult" class="videos search-video-thumbs">([^]*)<\/ul>

    //https://de.pornhub.com/video/search?search=test&page=2
    public PornhubCommand() {
        this.name = "pornhub";
        this.autoTest = false;
        this.category = new Category("NSFW");
        this.help = "Searches pornhub.";
        this.arguments = "<Term>";
        this.nsfw = true;
        this.requireVote = true;
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        //+pornhub Cutie
        if (event.getArgs().isBlank()) {
            TextUtilities.SendUsage(event, this);
            return;
        }
        String term = "";
        Document doc = null;
        ArrayList<SubClasses.PornhubClass> videoArray = new ArrayList<>();
        try {
            URIBuilder uriBuilder = new URIBuilder("https://pornhub.com/video/search");
            uriBuilder.addParameter("search", event.getArgs());
            term = uriBuilder.toString();
        } catch (URISyntaxException e) {
            Logger.error(event.getArgs(), e);
        }
        try {
            doc = Jsoup.connect(term).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();
        } catch (IOException e) {
            Logger.error(e);
        }
        Element mainFrame = null;
        if (doc.getElementById("videoSearchResult") != null) {
            mainFrame = doc.getElementById("videoSearchResult");
        } else if (doc.getElementById("videoCategory") != null) {
            mainFrame = doc.getElementById("videoCategory");
        }

        Elements videos = mainFrame.getElementById("videoSearchResult").getElementsByClass(" js-pop videoblock full-height videoBox");
        for (Element video : videos) {
            String videoString = video.toString().replace("\n", "".trim());

            String title = videoString.replaceFirst(".*?title=\"(.*?)\".*", "$1");
            String id = videoString.replaceFirst(".*?id=\"(.*?)\".*", "$1");
            String thumbUrl = videoString.replaceFirst(".*?data-thumb_url=\"(.*?)\".*", "$1");
            String viewKey = videoString.replaceFirst(".*?_vkey=\"(.*?)\".*", "$1");
            String duration = videoString.replaceFirst(".*?class=\"duration\">(.*?)</var>.*", "$1");
            String views = videoString.replaceFirst(".*?<span class=\"views\"><var>(.*?)</var>.*", "$1");
            String rating = videoString.replaceFirst(".*?<div class=\"value\">(.*?)</div>.*", "$1");
            String added = videoString.replaceFirst(".*?<var class=\"added\">(.*?)</var>.*", "$1");
            videoArray.add(new SubClasses.PornhubClass(title, id, thumbUrl, viewKey, duration, views, rating, added));
        }
        Random random = new Random();
        SubClasses.PornhubClass porn = videoArray.get(random.nextInt(videoArray.size()));
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("[" + porn.getTitle() + "](https://www.pornhub.com/view_video.php?viewkey=" + porn.getViewKey() + ") ");
        embedBuilder.setImage(porn.getThumbUrl());
        embedBuilder.setColor(new Color(94, 56, 255));
        embedBuilder.setFooter("Length: " + porn.getDuration() + " | Added: " + porn.getAdded() + " | Rating " + porn.getRating() + " | Views " + porn.getViews(), null);
        embedBuilder.setAuthor("Pornhub", null, "http://d3g9pb5nvr3u7.cloudfront.net/sites/5581af0d89da01090dcdd147/1007024084/256.jpg");
        event.reply(embedBuilder.build());
    }
}
