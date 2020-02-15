/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command;

import net.dv8tion.jda.api.entities.Guild;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

public interface CommandClient {
    String getPrefix();

    String getAltPrefix();

    String getTextualPrefix();

    void addCommand(Command var1);

    void addCommand(Command var1, int var2);

    void removeCommand(String var1);

    void addAnnotatedModule(Object var1);

    void addAnnotatedModule(Object var1, Function<Command, Integer> var2);

    CommandListener getListener();

    void setListener(CommandListener var1);

    List<Command> getCommands();

    OffsetDateTime getStartTime();

    OffsetDateTime getCooldown(String var1);

    int getRemainingCooldown(String var1);

    void applyCooldown(String var1, int var2);

    void cleanCooldowns();

    int getCommandUses(Command var1);

    int getCommandUses(String var1);

    String getOwnerId();

    long getOwnerIdLong();

    String[] getCoOwnerIds();

    long[] getCoOwnerIdsLong();

    String getSuccess();

    String getWarning();

    String getError();

    ScheduledExecutorService getScheduleExecutor();

    String getServerInvite();

    int getTotalGuilds();

    String getHelpWord();

    boolean usesLinkedDeletion();

    <S> S getSettingsFor(Guild var1);

    <M extends GuildSettingsManager> M getSettingsManager();
}

