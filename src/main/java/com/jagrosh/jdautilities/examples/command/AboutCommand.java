/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:56.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.examples.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import me.turulix.main.DiscordBot;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * @author John Grosh (jagrosh)
 */
@CommandInfo(name = "About", description = "Gets information about the bot.")
@Author("John Grosh (jagrosh)")
public class AboutCommand extends Command {
    private final Color color;
    private final String description;
    private final Permission[] perms;
    private final String[] features;
    private boolean IS_AUTHOR = true;
    private String REPLACEMENT_ICON = "+";
    private String oauthLink;

    public AboutCommand(Color color, String description, String[] features, Permission... perms) {
        this.color = color;
        this.description = description;
        this.features = features;
        this.autoTest = false;
        this.name = "about";
        this.help = "shows info about the bot";
        this.guildOnly = false;
        this.perms = perms;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }

    public void setIsAuthor(boolean value) {
        this.IS_AUTHOR = value;
    }

    public void setReplacementCharacter(String value) {
        this.REPLACEMENT_ICON = value;
    }

    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        if (oauthLink == null) {
            try {
                ApplicationInfo info = event.getJDA().retrieveApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L, perms) : "";
            } catch (Exception e) {
                Logger log = LoggerFactory.getLogger("OAuth2");
                log.error("Could not generate invite link ", e);
                oauthLink = "";
            }
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild() == null ? color : event.getGuild().getSelfMember().getColor());
        builder.setAuthor("All about " + event.getSelfUser().getName() + "!", null, event.getSelfUser().getAvatarUrl());
        boolean join = !(event.getClient().getServerInvite() == null || event.getClient().getServerInvite().isEmpty());
        boolean inv = !oauthLink.isEmpty();
        String invline = "\n" + (join ? "Join my server [`here`](" + event.getClient().getServerInvite() + ")" : (inv ? "Please " : "")) + (inv ? (join ? ", or " : "") + "[`invite`](" + oauthLink + ") me to your server" : "") + "!";
        StringBuilder descr = new StringBuilder("Hello! I am **" + event.getSelfUser().getName() + "**, " + description + "\nI " + (IS_AUTHOR ? "was written in Java" : "am owned") + " by **turulix** using " + JDAUtilitiesInfo.AUTHOR + "'s [Commands Extension](" + JDAUtilitiesInfo.GITHUB + ") (" + JDAUtilitiesInfo.VERSION + ") and the " + "[JDA library](https://github.com/DV8FromTheWorld/JDA) (" + JDAInfo.VERSION + ")" + "\nType `" + event.getClient().getTextualPrefix() + event.getClient().getHelpWord() + "` to see my commands!" + (join || inv ? invline : "") + "\n\nSome of my features include: ```css");
        for (String feature : features)
            descr.append("\n").append(event.getClient().getSuccess().startsWith("<") ? REPLACEMENT_ICON : event.getClient().getSuccess()).append(" ").append(feature);
        descr.append(" ```");
        builder.setDescription(descr);
        if (event.getJDA().getShardInfo() == null) {
            builder.addField("Stats", event.getJDA().getGuilds().size() + " servers\n1 shard", true);
            builder.addField("Users", event.getJDA().getUsers().size() + " unique\n" + event.getJDA().getGuilds().stream().mapToInt(g -> g.getMembers().size()).sum() + " total", true);
            builder.addField("Channels", event.getJDA().getTextChannels().size() + " Text\n" + event.getJDA().getVoiceChannels().size() + " Voice", true);
        } else {
            builder.addField("Stats", (DiscordBot.instance.registerStuff.shardManager.getGuilds().size() + " Servers\nShard " + (event.getJDA().getShardInfo().getShardId() + 1) + "/" + event.getJDA().getShardInfo().getShardTotal()), true);
            builder.addField("This shard", event.getJDA().getUsers().size() + " Users\n" + event.getJDA().getGuilds().size() + " Servers", true);
            builder.addField("", event.getJDA().getTextChannels().size() + " Text Channels\n" + event.getJDA().getVoiceChannels().size() + " Voice Channels", true);
        }
        builder.setFooter("Last restart", null);
        builder.setTimestamp(event.getClient().getStartTime());
        event.reply(builder.build());
    }

}
