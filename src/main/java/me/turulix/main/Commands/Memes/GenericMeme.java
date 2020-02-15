/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:54.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Memes;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.DiscordBot;
import me.turulix.main.Logger;
import me.turulix.main.RegisterStuff;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.i18n.I18nContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;

/**
 * MemeType: <br> 0 Text <br> 1 Avatar <br> 2 Two Avatars <br> 3 Text Avatars <br> 4 Text Avatars Nickname <br>
 */
public class GenericMeme extends Command {
    private int memeType;
    private String fileName;


    public GenericMeme(String name, String help, String arguments, String fileName, int memeType) {
        this.fileName = fileName;
        this.memeType = memeType;
        this.name = name;
        this.arguments = arguments;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.help = help;
        this.category = new Category("Images");
        this.autoTest = false;
    }

    @Override
    protected void execute(@NotNull CommandEvent event, I18nContext context) {
        switch (this.memeType) {
            case 0:
                text(event);
                break; //Text
            case 1:
                avatar(event);
                break; //Avatar
            case 2:
                twoAvatars(event);
                break; //Two Avatars
            case 3:
                textAvatar(event);
                break; //Text Avatar
            case 4:
                textNickAvatar(event);
                break; //textNickAvatar
            default:
                throw new IndexOutOfBoundsException("MemeType: " + memeType + " not found.");
        }
    }

    private void text(CommandEvent event) {
        try {
            @NotNull String[] args = event.getArgs().split(" ");
            @NotNull RegisterStuff registerStuff = DiscordBot.instance.registerStuff;
            int argsAmount;
            if (this.arguments.split(" ").length == 0) {
                argsAmount = 1;
            } else {
                argsAmount = this.arguments.split(" ").length;
            }
            if (args.length >= argsAmount && !event.getArgs().equalsIgnoreCase("")) {
                String msg = event.getArgs();
                msg = TextUtilities.replaceMentionedUsersWithName(event, msg);
                @Nullable InputStream response = Utils.getMeme("http://turulix.de:5000/api/" + this.name, msg, "", "", "", "", DiscordBot.instance.tomlManager.getToml().tokens.imageServerToken);
                if (response == null) throw new NullPointerException("InputStream is Null");
                TextUtilities.sendEmbedLocalFile(response, event, this.fileName);
            } else {
                TextUtilities.SendUsage(event, this);
            }
        } catch (Exception ex) {
            Logger.error(event, ex);
        }
    }

    private void avatar(CommandEvent event) {
        try {
            @NotNull RegisterStuff registerStuff = DiscordBot.instance.registerStuff;
            if (event.getMessage().getMentionedUsers().size() != 0) {
                if (event.getMessage().getMentionedUsers().get(0).getAvatarUrl() == null) {
                    event.reply("User \"" + event.getMessage().getMentionedUsers().get(0).getAsMention() + " has no avatar :/");
                    return;
                }
                @Nullable InputStream response = Utils.getMeme("http://turulix.de:5000/api/" + this.name, "", event.getMessage().getMentionedUsers().get(0).getAvatarUrl(), "", "", "", DiscordBot.instance.tomlManager.getToml().tokens.imageServerToken);
                if (response == null) throw new NullPointerException("InputStream is Null");
                TextUtilities.sendEmbedLocalFile(response, event, this.fileName);
            } else {
                if (event.getMessage().getAuthor().getAvatarUrl() != null) {
                    @Nullable InputStream response = Utils.getMeme("http://turulix.de:5000/api/" + this.name, "", event.getMessage().getAuthor().getAvatarUrl(), "", "", "", DiscordBot.instance.tomlManager.getToml().tokens.imageServerToken);
                    if (response == null) throw new NullPointerException("InputStream is Null");
                    TextUtilities.sendEmbedLocalFile(response, event, this.fileName);
                } else {
                    event.reply("User have no avatar :/");
                }
            }
        } catch (Exception ex) {
            Logger.error(event, ex);
        }
    }

    private void twoAvatars(CommandEvent event) {
        try {
            @NotNull RegisterStuff registerStuff = DiscordBot.instance.registerStuff;
            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            User author = event.getAuthor();
            if (author.getAvatarUrl() == null) {
                event.reply("You havent got a avatar :/");
            }
            if (mentionedUsers.size() == 1) {
                if (mentionedUsers.get(0).getAvatarUrl() == null) {
                    event.reply("You havent got a avatar :/");
                    return;
                }
                @Nullable InputStream response = Utils.getMeme("http://turulix.de:5000/api/" + this.name, "", author.getAvatarUrl(), mentionedUsers.get(0).getAvatarUrl(), "", "", DiscordBot.instance.tomlManager.getToml().tokens.imageServerToken);
                if (response == null) throw new NullPointerException("InputStream is Null");
                TextUtilities.sendEmbedLocalFile(response, event, this.fileName);
            } else if (mentionedUsers.size() == 2) {
                if (mentionedUsers.get(0).getAvatarUrl() == null || mentionedUsers.get(0).getAvatarUrl() == null) {
                    event.reply("You havent got a avatar :/");
                    return;
                }
                @Nullable InputStream response = Utils.getMeme("http://turulix.de:5000/api/" + this.name, "", mentionedUsers.get(0).getAvatarUrl(), mentionedUsers.get(1).getAvatarUrl(), "", "", DiscordBot.instance.tomlManager.getToml().tokens.imageServerToken);
                if (response == null) throw new NullPointerException("InputStream is Null");
                TextUtilities.sendEmbedLocalFile(response, event, this.fileName);
            } else {
                TextUtilities.SendUsage(event, this);
            }
        } catch (Exception ex) {
            Logger.error(event, ex);
        }
    }

    private void textAvatar(CommandEvent event) {
        try {
            @NotNull RegisterStuff registerStuff = DiscordBot.instance.registerStuff;
            User user = (event.getMessage().getMentionedUsers().size() != 0) ? event.getMessage().getMentionedUsers().get(0) : event.getAuthor();
            if (user.getAvatarUrl() == null) {
                event.reply("The user has no avatar :/");
                return;
            }
            if (event.getArgs().split(" ").length >= 1 && !event.getArgs().equalsIgnoreCase("")) {
                String msg = event.getArgs().replace(user.getAsMention(), "");
                msg = TextUtilities.replaceMentionedUsersWithName(event, msg);
                if (event.getMessage().getMentionedUsers().size() == 0) {
                    getImageInputstream(event, registerStuff, user, msg);
                } else {
                    getImageInputstream(event, registerStuff, user, msg);
                }
            } else {
                TextUtilities.SendUsage(event, this);
            }
        } catch (Exception ex) {
            Logger.error(event, ex);
        }
    }


    private void textNickAvatar(CommandEvent event) {
        try {
            String message = event.getMessage().getContentRaw();
            @NotNull String[] args = message.split(" ");
            User user = (event.getMessage().getMentionedUsers().size() != 0) ? event.getMessage().getMentionedUsers().get(0) : event.getAuthor();
            String nickname = event.getGuild().getMember(user).getEffectiveName();
            @NotNull RegisterStuff registerStuff = DiscordBot.instance.registerStuff;
            if (user.getAvatarUrl() == null) {
                event.reply("The user has no avatar :/");
                return;
            }
            if (args.length >= 1) {
                String msg = event.getArgs().replace(user.getAsMention(), "").replace("<@!" + event.getAuthor().getIdLong() + ">", "");
                msg = TextUtilities.replaceMentionedUsersWithName(event, msg);
                if (event.getMessage().getMentionedUsers().size() == 0) {
                    getImageInputstream(event, user, nickname, registerStuff, msg);
                } else {
                    getImageInputstream(event, user, nickname, registerStuff, msg);
                }
            } else {
                TextUtilities.SendUsage(event, this);
            }
        } catch (Exception ex) {
            Logger.error(event, ex);
        }
    }

    private void getImageInputstream(CommandEvent event, User user, String nickname, RegisterStuff registerStuff, String msg) {
        @Nullable InputStream response = Utils.getMeme("http://turulix.de:5000/api/" + this.name, msg, user.getAvatarUrl(), "", nickname, "", DiscordBot.instance.tomlManager.getToml().tokens.imageServerToken);
        if (response == null)
            throw new NullPointerException("Imagestream is null of " + event.getMessage().getContentRaw().trim().split(" ")[0]);
        TextUtilities.sendEmbedLocalFile(response, event, this.fileName);
    }

    private void getImageInputstream(CommandEvent event, RegisterStuff registerStuff, User user, String msg) {
        @Nullable InputStream response = Utils.getMeme("http://turulix.de:5000/api/" + this.name, msg, user.getAvatarUrl(), "", "", "", DiscordBot.instance.tomlManager.getToml().tokens.imageServerToken);
        if (response == null)
            throw new NullPointerException("Imagestream is null of " + event.getMessage().getContentRaw().trim().split(" ")[0]);
        TextUtilities.sendEmbedLocalFile(response, event, this.fileName);
    }
}
