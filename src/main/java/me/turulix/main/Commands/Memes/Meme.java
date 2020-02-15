/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Memes;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.DiscordBot;
import me.turulix.main.RegisterStuff;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.SubClasses.SubClasses;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@DankCommand
public class Meme extends Command {
    private final static String SHORTENED_URL = "https://redd.it/";

    public Meme() {
        this.name = "meme";
        this.category = new Category("Meme");
        this.help = "Gets you a hot random meme.";
        this.arguments = "";
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        @NotNull RegisterStuff registerStuff = DiscordBot.instance.registerStuff;
        if (registerStuff.cachedMemes.size() != 0) {
            @NotNull EmbedBuilder eb = new EmbedBuilder();
            @NotNull Random random = new Random();
            SubClasses.RedditMeme meme = registerStuff.cachedMemes.get(random.nextInt(registerStuff.cachedMemes.size()));
            eb.setImage(meme.getImageURL());
            eb.setDescription("[" + meme.getTitle().trim().substring(0, Math.min(meme.getTitle().length(), 100)) + "]" + "(" + SHORTENED_URL + meme.getId() + ")");
            eb.setFooter("\uD83D\uDC4D " + meme.getThumbsUp() + " | " + "\uD83D\uDCAC " + meme.getCommentAmount(), null);
            event.reply(eb.build());
        } else {
            event.reply("Strange the memes are hiding :c");
        }
    }
}
