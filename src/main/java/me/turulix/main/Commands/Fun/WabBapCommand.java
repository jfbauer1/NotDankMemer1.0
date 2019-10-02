/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.awt.*;

@DankCommand
public class WabBapCommand extends Command {
    public WabBapCommand() {
        this.name = "wabbap";
        this.category = new Command.Category("Fun");
        this.hidden = false;
        this.help = "Stats of wabbap";
        this.arguments = "";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        try {
            @Nullable String msg = Utils.getUrl("https://www.googleapis.com/youtube/v3/videos?key=" + DiscordBot.instance.tomlManager.getToml().tokens.youtubeToken + "&part=statistics&id=4gSOMba1UdM");
            assert msg == null;
            @Nullable JSONObject obj = new JSONObject(msg);
            Object arr = obj.get("items");
            @NotNull String arr2 = arr.toString().replace("[", "").replace("]", "");
            @NotNull JSONObject obj2 = new JSONObject(arr2);
            String viewCount = obj2.getJSONObject("statistics").getString("viewCount");
            String likeCount = obj2.getJSONObject("statistics").getString("likeCount");
            String dislikeCount = obj2.getJSONObject("statistics").getString("dislikeCount");
            String commentCount = obj2.getJSONObject("statistics").getString("commentCount");

            @NotNull EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.green);
            builder.setDescription("Bibi H - How it is Stats!").addField("Views: ", viewCount, true).addField("Likes:", likeCount, true).addField("Dislikes:", dislikeCount, true).addField("Comments", commentCount, true);
            builder.setThumbnail("https://i.ytimg.com/vi/4gSOMba1UdM/hqdefault.jpg");

            event.reply(builder.build());
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
}
