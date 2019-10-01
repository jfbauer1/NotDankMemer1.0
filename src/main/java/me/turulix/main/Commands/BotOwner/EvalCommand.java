/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:52.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.BotOwner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import groovy.lang.GroovyShell;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

@DankCommand
public class EvalCommand extends Command {
    public EvalCommand() {
        this.name = "eval";
        this.ownerCommand = true;
        this.category = new Category("BotOwner");
        this.autoTest = false;
    }

    @Override

    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        @NotNull String[] imports = new String[]{"me.turulix.main.UtilClasses.TextUtilities", "me.turulix.main.UtilClasses.FormatUtil"};
        event.async(() -> {
            @NotNull GroovyShell groovyShell = new GroovyShell();
            @NotNull Thread thread = new Thread(() -> {
                String args = event.getArgs();
                @NotNull StringBuilder importString = new StringBuilder();
                for (String anImport : imports) {
                    importString.append("import ").append(anImport).append("\n");
                }
                if (args.startsWith("```") && args.endsWith("```")) {
                    args = args.replaceAll("```(.*)\\n", "").replaceAll("\\n?```", "");
                }
                args = importString.toString() + args;
                groovyShell.setVariable("message", event.getMessage());
                groovyShell.setVariable("channel", event.getChannel());
                groovyShell.setVariable("guild", event.getGuild());
                groovyShell.setVariable("member", event.getMember());
                groovyShell.setVariable("author", event.getAuthor());
                groovyShell.setVariable("jda", event.getJDA());
                groovyShell.setVariable("shardManager", DiscordBot.instance.registerStuff.shardManager);
                groovyShell.setVariable("event", event);
                groovyShell.setVariable("rs", DiscordBot.instance.registerStuff);
                try {
                    groovyShell.evaluate(args);

                    event.reactSuccess();
                } catch (Exception ex) {
                    @NotNull StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    event.reply("```Java\n" + exceptionAsString + "\n```");
                    event.reactError();
                }
            });
            try {
                thread.start();
                thread.join(20 * 1000);
                if (thread.isAlive()) {
                    event.reply("Your script run for more than 20 Seconds canceling it");

                    thread.stop();
                }
            } catch (Exception e) {/*Ignore this*/}
        });
    }
}
