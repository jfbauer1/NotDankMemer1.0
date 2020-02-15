/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:52.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.BotOwner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.FormatUtil;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@DankCommand
public class DebugCommand extends Command {

    public DebugCommand() {
        this.name = "debug";
        this.help = "shows some debug stats";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.hidden = true;
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        long totalMb = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long usedMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        @NotNull StringBuilder sb = new StringBuilder("**" + event.getSelfUser().getName() + "** statistics:" + "\nLast Startup: " + FormatUtil.secondsToTime(event.getClient().getStartTime().until(OffsetDateTime.now(), ChronoUnit.SECONDS)) + " ago" + "\nGuilds: **" + DiscordBot.instance.registerStuff.shardManager.getGuildCache().size() + "**" + "\nMemory: **" + usedMb + "**Mb / **" + totalMb + "**Mb" + "\nAverage Ping: **" + DiscordBot.instance.registerStuff.shardManager.getAverageGatewayPing() + "**ms" + "\nShard Total: **" + DiscordBot.instance.registerStuff.shardManager.getShardsTotal() + "**" + "\nShard Connectivity: ```diff");
        DiscordBot.instance.registerStuff.shardManager.getShards().forEach(jda -> sb.append("\n").append(jda.getStatus() == JDA.Status.CONNECTED ? "+ " : "- ").append(jda.getShardInfo().getShardId() < 10 ? "0" : "").append(jda.getShardInfo().getShardId()).append(": ").append(jda.getStatus()).append(" ~ ").append(jda.getGuildCache().size()).append(" guilds"));
        sb.append("\n```");
        event.reply(sb.toString().trim());
    }
}