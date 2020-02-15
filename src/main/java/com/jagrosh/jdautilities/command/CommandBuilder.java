/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 23:36.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command;

import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommandBuilder {
    private final LinkedList<String> aliases = new LinkedList();
    private final LinkedList<Command> children = new LinkedList();
    private String name = "null";
    private String help = "no help available";
    private Command.Category category = null;
    private String arguments = null;
    private boolean guildOnly = true;
    private String requiredRole = null;
    private boolean ownerCommand = false;
    private int cooldown = 0;
    private Permission[] userPermissions = new Permission[0];
    private Permission[] botPermissions = new Permission[0];
    private BiConsumer<CommandEvent, Command> helpBiConsumer = null;
    private boolean usesTopicTags = true;
    private Command.CooldownScope cooldownScope = Command.CooldownScope.USER;
    private boolean hidden = false;

    public CommandBuilder setName(String name) {
        this.name = name == null ? "null" : name;
        return this;
    }

    public CommandBuilder setHelp(String help) {
        this.help = help == null ? "no help available" : help;
        return this;
    }

    public CommandBuilder setCategory(Command.Category category) {
        this.category = category;
        return this;
    }

    public CommandBuilder setArguments(String arguments) {
        this.arguments = arguments;
        return this;
    }

    public CommandBuilder setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
        return this;
    }

    public CommandBuilder setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
        return this;
    }

    public CommandBuilder setOwnerCommand(boolean ownerCommand) {
        this.ownerCommand = ownerCommand;
        return this;
    }

    public CommandBuilder setCooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public /* varargs */ CommandBuilder setUserPermissions(Permission... userPermissions) {
        this.userPermissions = userPermissions == null ? new Permission[0] : userPermissions;
        return this;
    }

    public /* varargs */ CommandBuilder setBotPermissions(Permission... botPermissions) {
        this.botPermissions = botPermissions == null ? new Permission[0] : botPermissions;
        return this;
    }

    public CommandBuilder addAlias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public /* varargs */ CommandBuilder addAliases(String... aliases) {
        for (String alias : aliases) {
            this.addAlias(alias);
        }
        return this;
    }

    public /* varargs */ CommandBuilder setAliases(String... aliases) {
        this.aliases.clear();
        if (aliases != null) {
            for (String alias : aliases) {
                this.addAlias(alias);
            }
        }
        return this;
    }

    public CommandBuilder setAliases(Collection<String> aliases) {
        this.aliases.clear();
        if (aliases != null) {
            this.aliases.addAll(aliases);
        }
        return this;
    }

    public CommandBuilder addChild(Command child) {
        this.children.add(child);
        return this;
    }

    public /* varargs */ CommandBuilder addChildren(Command... children) {
        for (Command child : children) {
            this.addChild(child);
        }
        return this;
    }

    public /* varargs */ CommandBuilder setChildren(Command... children) {
        this.children.clear();
        if (children != null) {
            for (Command child : children) {
                this.addChild(child);
            }
        }
        return this;
    }

    public CommandBuilder setChildren(Collection<Command> children) {
        this.children.clear();
        if (children != null) {
            this.children.addAll(children);
        }
        return this;
    }

    public CommandBuilder setHelpBiConsumer(BiConsumer<CommandEvent, Command> helpBiConsumer) {
        this.helpBiConsumer = helpBiConsumer;
        return this;
    }

    public CommandBuilder setUsesTopicTags(boolean usesTopicTags) {
        this.usesTopicTags = usesTopicTags;
        return this;
    }

    public CommandBuilder setCooldownScope(Command.CooldownScope cooldownScope) {
        this.cooldownScope = cooldownScope == null ? Command.CooldownScope.USER : cooldownScope;
        return this;
    }

    public CommandBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public Command build(Consumer<CommandEvent> execution) {
        return this.build((c, e) -> execution.accept(e));
    }

    public Command build(final BiConsumer<Command, CommandEvent> execution) {
        return new BlankCommand(this.name, this.help, this.category, this.arguments, this.guildOnly, this.requiredRole, this.ownerCommand, this.cooldown, this.userPermissions, this.botPermissions, this.aliases.toArray(new String[this.aliases.size()]), this.children.toArray(new Command[this.children.size()]), this.helpBiConsumer, this.usesTopicTags, this.cooldownScope, this.hidden) {

            @Override
            protected void execute(CommandEvent event, I18nContext context) {
                execution.accept(this, event);
            }
        };
    }

    public abstract class BlankCommand extends Command {
        BlankCommand(String name, String help, Command.Category category, String arguments, boolean guildOnly, String requiredRole, boolean ownerCommand, int cooldown, Permission[] userPermissions, Permission[] botPermissions, String[] aliases, Command[] children, BiConsumer<CommandEvent, Command> helpBiConsumer, boolean usesTopicTags, Command.CooldownScope cooldownScope, boolean hidden) {
            this.name = name;
            this.help = help;
            this.category = category;
            this.arguments = arguments;
            this.guildOnly = guildOnly;
            this.requiredRole = requiredRole;
            this.ownerCommand = ownerCommand;
            this.cooldown = cooldown;
            this.userPermissions = userPermissions;
            this.botPermissions = botPermissions;
            this.children = children;
            this.helpBiConsumer = helpBiConsumer;
            this.usesTopicTags = usesTopicTags;
            this.cooldownScope = cooldownScope;
            this.hidden = hidden;
        }
    }

}

