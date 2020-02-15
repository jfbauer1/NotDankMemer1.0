/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command;

import com.jagrosh.jdautilities.command.impl.AnnotatedModuleCompilerImpl;
import com.jagrosh.jdautilities.command.impl.CommandClientImpl;
import net.dv8tion.jda.api.OnlineStatus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class CommandClientBuilder {
    private final LinkedList<Command> commands = new LinkedList();
    private OnlineStatus status = OnlineStatus.ONLINE;
    private String ownerId;
    private String[] coOwnerIds;
    private String prefix;
    private String altprefix;
    private String serverInvite;
    private String success;
    private String warning;
    private String error;
    private String carbonKey;
    private String botsKey;
    private String botsOrgKey;
    private CommandListener listener;
    private boolean useHelp = true;
    private Consumer<CommandEvent> helpConsumer;
    private String helpWord;
    private ScheduledExecutorService executor;
    private int linkedCacheSize = 0;
    private AnnotatedModuleCompiler compiler = new AnnotatedModuleCompilerImpl();
    private GuildSettingsManager manager = null;

    public CommandClient build() {
        CommandClientImpl client = new CommandClientImpl(this.ownerId, this.coOwnerIds, this.prefix, this.altprefix, this.status, this.serverInvite, this.success, this.warning, this.error, this.carbonKey, this.botsKey, this.botsOrgKey, new ArrayList<Command>(this.commands), this.useHelp, this.helpConsumer, this.helpWord, this.executor, this.linkedCacheSize, this.compiler, this.manager);
        if (this.listener != null) {
            client.setListener(this.listener);
        }
        return client;
    }

    public CommandClientBuilder setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public /* varargs */ CommandClientBuilder setCoOwnerIds(String... coOwnerIds) {
        this.coOwnerIds = coOwnerIds;
        return this;
    }

    public CommandClientBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CommandClientBuilder setAlternativePrefix(String prefix) {
        this.altprefix = prefix;
        return this;
    }

    public CommandClientBuilder useHelpBuilder(boolean useHelp) {
        this.useHelp = useHelp;
        return this;
    }

    public CommandClientBuilder setHelpConsumer(Consumer<CommandEvent> helpConsumer) {
        this.helpConsumer = helpConsumer;
        return this;
    }

    public CommandClientBuilder setHelpWord(String helpWord) {
        this.helpWord = helpWord;
        return this;
    }

    public CommandClientBuilder setServerInvite(String serverInvite) {
        this.serverInvite = serverInvite;
        return this;
    }

    public CommandClientBuilder setEmojis(String success, String warning, String error) {
        this.success = success;
        this.warning = warning;
        this.error = error;
        return this;
    }

    public CommandClientBuilder setStatus(OnlineStatus status) {
        this.status = status;
        return this;
    }

    public CommandClientBuilder addCommand(Command command) {
        this.commands.add(command);
        return this;
    }

    public /* varargs */ CommandClientBuilder addCommands(Command... commands) {
        for (Command command : commands) {
            this.addCommand(command);
        }
        return this;
    }

    public CommandClientBuilder addAnnotatedModule(Object module) {
        this.commands.addAll(this.compiler.compile(module));
        return this;
    }

    public /* varargs */ CommandClientBuilder addAnnotatedModules(Object... modules) {
        for (Object command : modules) {
            this.addAnnotatedModule(command);
        }
        return this;
    }

    public CommandClientBuilder setAnnotatedCompiler(AnnotatedModuleCompiler compiler) {
        this.compiler = compiler;
        return this;
    }

    public CommandClientBuilder setCarbonitexKey(String key) {
        this.carbonKey = key;
        return this;
    }

    public CommandClientBuilder setDiscordBotsKey(String key) {
        this.botsKey = key;
        return this;
    }

    public CommandClientBuilder setDiscordBotListKey(String key) {
        this.botsOrgKey = key;
        return this;
    }

    public CommandClientBuilder setListener(CommandListener listener) {
        this.listener = listener;
        return this;
    }

    public CommandClientBuilder setScheduleExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public CommandClientBuilder setLinkedCacheSize(int linkedCacheSize) {
        this.linkedCacheSize = linkedCacheSize;
        return this;
    }

    public CommandClientBuilder setGuildSettingsManager(GuildSettingsManager manager) {
        this.manager = manager;
        return this;
    }
}

