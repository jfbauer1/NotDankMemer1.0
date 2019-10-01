/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command.annotation;

import com.jagrosh.jdautilities.command.Command;
import net.dv8tion.jda.core.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface JDACommand {
    String[] name() default {"null"};

    String help() default "no help available";

    boolean guildOnly() default true;

    String requiredRole() default "";

    boolean ownerCommand() default false;

    String arguments() default "";

    Cooldown cooldown() default @Cooldown(value = 0);

    Permission[] botPermissions() default {};

    Permission[] userPermissions() default {};

    boolean useTopicTags() default true;

    String[] children() default {};

    boolean isHidden() default false;

    Category category() default @Category(name = "null", location = Category.class);

    @Target(value = {ElementType.TYPE})
    @Retention(value = RetentionPolicy.RUNTIME)
    @interface Category {
        String name();

        Class<?> location();
    }

    @Target(value = {ElementType.TYPE})
    @Retention(value = RetentionPolicy.RUNTIME)
    @interface Cooldown {
        int value();

        Command.CooldownScope scope() default Command.CooldownScope.USER;
    }

    @Target(value = {ElementType.TYPE})
    @Retention(value = RetentionPolicy.RUNTIME)
    @interface Module {
        String[] value();
    }

}

