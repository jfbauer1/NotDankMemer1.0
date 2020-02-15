/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:55.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

@DankCommand
public class PewDiePieCommand extends Command {
    public PewDiePieCommand() {
        this.name = "pewdiepie";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = false;
        this.help = "SUPPORT US! DONT LET T-SERIES WIN!!!!";
        this.usesTopicTags = false;
        this.autoTest = false;
    }


    @Override
    protected void execute(CommandEvent event, I18nContext context) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**__Subscribe to PewDiePie!__**");
        eb.setDescription("[Click me to subscribe to PewDiePie now! To Save the world!](https://www.youtube.com/user/PewDiePie?sub_confirmation=1)");
        eb.setImage("https://cdn.vox-cdn.com/thumbor/crL0ZNWfFohWp5W0-rP2GK02msc=/0x0:847x431/1200x800/filters:focal(413x158:547x292)/cdn.vox-cdn.com/uploads/chorus_image/image/57166397/Screen_Shot_2017_10_16_at_11.14.48_AM.0.png");
        event.reply(eb.build());
    }
}
