/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command;

import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nullable;

public interface GuildSettingsManager<T> {
    @Nullable
    T getSettings(Guild var1);

    default void init() {
    }

    default void shutdown() {
    }
}

