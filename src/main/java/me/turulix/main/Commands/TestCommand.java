/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 22.01.19 00:20.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.model.Filters;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;
import org.bson.BsonDateTime;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

@DankCommand
public class TestCommand extends Command {
    public TestCommand() {
        this.name = "test";
        this.category = new Category("Debug");
        this.hidden = true;
        this.ownerCommand = true;
        this.help = "Just a test command to test the new Command System";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.autoTest = false;
        collectionName = "UserDatabase";
    }

    //TODO: Remove again.
    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        DiscordBot.instance.registerStuff.database.userManager.removeUser(event.getAuthor().getIdLong());

        update(Filters.eq("_id", event.getAuthor().getIdLong()), "voteDate", new BsonDateTime(new Date().getTime()));

        Date timestamp = getDocument(Filters.eq("_id", event.getAuthor().getIdLong())).getDate("voteDate");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendar.add(Calendar.DATE, 33);


        event.reply(calendar.getTime().toGMTString());
    }
}
