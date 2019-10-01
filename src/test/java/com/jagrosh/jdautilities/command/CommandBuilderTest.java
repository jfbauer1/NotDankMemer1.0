/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 22.01.19 00:20.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.command;

import me.turulix.main.Commands.TestCommand;
import me.turulix.main.Listeners.TestFailListener;
import net.dv8tion.jda.core.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestFailListener.class)
class CommandBuilderTest {
    private CommandBuilder commandBuilder;
    private Command command;

    @BeforeEach
    void setUp() {
        commandBuilder = new CommandBuilder();
    }

    private void buildCommand() {
        command = commandBuilder.build(event -> event.getAuthor());
    }

    @Test
    void setName() {
        commandBuilder.setName("Test2");
        buildCommand();
        assertEquals("Test2", command.name);
    }

    @Test
    void setHelp() {
        commandBuilder.setHelp("HelpText");
        buildCommand();
        assertEquals("HelpText", command.help);
    }

    @Test
    void setCategory() {
        Command.Category category = new Command.Category("TestCat");
        commandBuilder.setCategory(category);
        buildCommand();
        assertEquals(category, command.category);
    }

    @Test
    void setArguments() {
        commandBuilder.setArguments("TestArgs");
        buildCommand();
        assertEquals("TestArgs", command.arguments);
    }

    @Test
    void setGuildOnly() {
        commandBuilder.setGuildOnly(false);
        buildCommand();
        assertFalse(command.guildOnly);
    }

    @Test
    void setRequiredRole() {
        commandBuilder.setRequiredRole("Tester");
        buildCommand();
        assertEquals("Tester", command.requiredRole);
    }

    @Test
    void setOwnerCommand() {
        commandBuilder.setOwnerCommand(true);
        buildCommand();
        assertTrue(command.ownerCommand);
    }

    @Test
    void setCooldown() {
        commandBuilder.setCooldown(60);
        buildCommand();
        assertEquals(60, command.cooldown);
    }

    @Test
    void setUserPermissions() {
        Permission[] permissions = new Permission[]{Permission.ADMINISTRATOR};
        commandBuilder.setUserPermissions(permissions);
        buildCommand();
        assertEquals(permissions, command.userPermissions);
    }

    @Test
    void setBotPermissions() {
        Permission[] permissions = new Permission[]{Permission.ADMINISTRATOR};
        commandBuilder.setBotPermissions(permissions);
        buildCommand();
        assertEquals(permissions, command.botPermissions);
    }

    @Test
    void addChild() {
        TestCommand testCommand = new TestCommand();
        commandBuilder.addChild(testCommand);
        buildCommand();
        assertEquals(1, command.children.length);
        assertEquals(testCommand, command.children[0]);

    }

    @Test
    void addChildren() {
        TestCommand testCommand = new TestCommand();
        TestCommand testCommand1 = new TestCommand();
        Command[] commands = new Command[]{testCommand, testCommand1};
        commandBuilder.addChildren(commands);
        buildCommand();
        assertEquals(commands.length, command.children.length);
    }

    @Test
    void setChildren() {
        TestCommand testCommand = new TestCommand();
        TestCommand testCommand1 = new TestCommand();
        Command[] commands = new Command[]{testCommand, testCommand1};
        commandBuilder.setChildren(commands);
        buildCommand();
        assertEquals(commands.length, command.children.length);
    }

    @Test
    void setChildren1() {
        TestCommand testCommand = new TestCommand();
        TestCommand testCommand1 = new TestCommand();
        Collection commands = new ArrayList();
        commands.add(testCommand);
        commands.add(testCommand1);
        commandBuilder.setChildren(commands);
        buildCommand();
        assertEquals(commands.size(), command.children.length);
    }

    @Test
    void setHelpBiConsumer() {
        BiConsumer<CommandEvent, Command> consumer = (event, command1) -> command1.name = "Test";
        commandBuilder.setHelpBiConsumer(consumer);
        buildCommand();
        assertEquals(consumer, command.helpBiConsumer);
    }

    @Test
    void setUsesTopicTags() {
        commandBuilder.setUsesTopicTags(false);
        buildCommand();
        assertFalse(command.usesTopicTags);
    }

    @Test
    void setCooldownScope() {
        commandBuilder.setCooldownScope(Command.CooldownScope.USER);
        buildCommand();
        assertEquals(Command.CooldownScope.USER, command.cooldownScope);
    }

    @Test
    void setHidden() {
        commandBuilder.setHidden(true);
        buildCommand();
        assertTrue(command.hidden);
    }

    @Test
    void build() {
        assertNotNull(commandBuilder.build((command1, event) -> {
            command1.execute(null, null);
        }));
    }

    @Test
    void execute() {
        assertTrue(true);
    }

    @Test
    void build1() {
        assertNotNull(commandBuilder.build(event -> event.getAuthor()));
    }
}