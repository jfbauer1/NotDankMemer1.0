/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command.impl;

import com.jagrosh.jdautilities.command.AnnotatedModuleCompiler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.annotation.JDACommand;
import me.turulix.main.Logger;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AnnotatedModuleCompilerImpl implements AnnotatedModuleCompiler {
    @SafeVarargs
    private static /* varargs */ <T> List<T> collect(Predicate<T> filter, T... entities) {
        ArrayList<T> list = new ArrayList<T>();
        for (T entity : entities) {
            if (!filter.test(entity)) continue;
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<Command> compile(Object o) {
        JDACommand.Module module = o.getClass().getAnnotation(JDACommand.Module.class);
        if (module == null) {
            throw new IllegalArgumentException("Object provided is not annotated with JDACommand.Module!");
        }
        if (module.value().length < 1) {
            throw new IllegalArgumentException("Object provided is annotated with an empty command module!");
        }
        List<Method> commands = AnnotatedModuleCompilerImpl.collect(method -> {
            for (String name : module.value()) {
                if (!name.equalsIgnoreCase(method.getName())) continue;
                return true;
            }
            return false;
        }, o.getClass().getMethods());
        ArrayList<Command> list = new ArrayList<Command>();
        commands.forEach(method -> {
            try {
                list.add(this.compileMethod(o, method));
            } catch (MalformedParametersException e) {
                Logger.error(e.getMessage());
            }
        });
        return list;
    }

    private Command compileMethod(Object o, Method method) throws MalformedParametersException {
        Class<?>[] parameters;
        JDACommand properties = method.getAnnotation(JDACommand.class);
        if (properties == null) {
            throw new IllegalArgumentException("Method named " + method.getName() + " is not annotated with JDACommand!");
        }
        CommandBuilder builder = new CommandBuilder();
        String[] names = properties.name();
        builder.setName(names.length < 1 ? "null" : names[0]);
        if (names.length > 1) {
            for (int i = 1; i < names.length; ++i) {
                builder.addAlias(names[i]);
            }
        }
        builder.setHelp(properties.help());
        builder.setArguments(properties.arguments().trim().isEmpty() ? null : properties.arguments().trim());
        if (!properties.category().location().equals(JDACommand.Category.class)) {
            JDACommand.Category category = properties.category();
            for (Field field : category.location().getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) || !field.getType().equals(Command.Category.class) || !category.name().equalsIgnoreCase(field.getName()))
                    continue;
                try {
                    builder.setCategory((Command.Category) field.get(null));
                } catch (IllegalAccessException e) {
                    Logger.error("Encountered Exception ", e);
                }
            }
        }
        builder.setGuildOnly(properties.guildOnly());
        builder.setRequiredRole(properties.requiredRole().trim().isEmpty() ? null : properties.requiredRole().trim());
        builder.setOwnerCommand(properties.ownerCommand());
        builder.setCooldown(properties.cooldown().value());
        builder.setCooldownScope(properties.cooldown().scope());
        builder.setBotPermissions(properties.botPermissions());
        builder.setUserPermissions(properties.userPermissions());
        builder.setUsesTopicTags(properties.useTopicTags());
        builder.setHidden(properties.isHidden());
        if (properties.children().length > 0) {
            AnnotatedModuleCompilerImpl.collect(m -> {
                for (String cName : properties.children()) {
                    if (!cName.equalsIgnoreCase(m.getName())) continue;
                    return true;
                }
                return false;
            }, o.getClass().getMethods()).forEach(cm -> {
                try {
                    builder.addChild(this.compileMethod(o, cm));
                } catch (MalformedParametersException e) {
                    Logger.error("Encountered Exception ", e);
                }
            });
        }
        if ((parameters = method.getParameterTypes())[0] == Command.class && parameters[1] == CommandEvent.class) {
            return builder.build((command, event) -> {
                try {
                    method.invoke(o, command, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Logger.error("Encountered Exception ", e);
                }
            });
        }
        if (parameters[0] == CommandEvent.class) {
            if (parameters.length == 1) {
                return builder.build(event -> {
                    try {
                        method.invoke(o, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        Logger.error("Encountered Exception ", e);
                    }
                });
            }
            if (parameters[1] == Command.class) {
                return builder.build((command, event) -> {
                    try {
                        method.invoke(o, event, command);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        Logger.error("Encountered Exception ", e);
                    }
                });
            }
        }
        throw new MalformedParametersException("Method named " + method.getName() + " was not compiled due to improper parameter types!");
    }
}

