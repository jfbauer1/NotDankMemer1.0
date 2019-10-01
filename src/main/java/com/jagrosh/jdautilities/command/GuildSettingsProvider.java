/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command;

import javax.annotation.Nullable;
import java.util.Collection;

public interface GuildSettingsProvider {
    @Nullable
    default Collection<String> getPrefixes() {
        return null;
    }
}

