/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses.SubClasses;

import me.turulix.main.DiscordBot;
import org.junit.jupiter.api.TestInstance;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 05.01.2019 12:42
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public interface DiscordBotInterface {
    DiscordBot discordBot = new DiscordBot(false, false);
}
