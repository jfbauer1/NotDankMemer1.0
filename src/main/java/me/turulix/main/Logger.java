/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 22.01.19 00:04.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main;

import com.jagrosh.jdautilities.command.CommandEvent;
import me.turulix.main.UtilClasses.ConsoleColors;
import me.turulix.main.UtilClasses.FormatUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Logger implements Thread.UncaughtExceptionHandler {

    private static void makeDirectory(File directory) {
        if (!directory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            directory.mkdirs();
        }
    }


    private static void sendToConsole(String message) {
        System.out.println("[" + FormatUtil.getCurrentTimeAsString() + "] " + message);
    }

    private static void sendToConsole(Throwable ex) {
        ex.printStackTrace();
    }

    private static void sendToConsole(String message, Throwable ex) {
        System.out.println("[" + FormatUtil.getCurrentTimeAsString() + "] " + message);
        ex.printStackTrace();
    }


    public static void sendToDiscord(String message) {
        try {
            if (sendErrorToDiscord(message)) return;
        } catch (Exception ex) {
            webHook("Could not send to discord.", message, "Overwatch");
        }
    }

    private static boolean sendErrorToDiscord(String message) {
        Guild SupportGuild = DiscordBot.instance.registerStuff.shardManager.getGuildById(DiscordBot.instance.registerStuff.NotDankMemerServerID);
        TextChannel errorChannel = SupportGuild.getTextChannelById(DiscordBot.instance.registerStuff.NotDankMemerErrorChannelID);
        if (DiscordBot.instance.tomlManager.getToml().testMode) {
            errorChannel.sendMessage("```Java\n" + message + "\n```").queue();
            return true;
        }
        errorChannel.sendMessage("```Java\n" + message + "\n```").queue();
        return false;
    }

    public static void sendToDiscord(String message, Throwable throwable) {
        try {
            @NotNull StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();

            Guild SupportGuild = DiscordBot.instance.registerStuff.shardManager.getGuildById(DiscordBot.instance.registerStuff.NotDankMemerServerID);
            TextChannel errorChannel = SupportGuild.getTextChannelById(DiscordBot.instance.registerStuff.NotDankMemerErrorChannelID);
            if (DiscordBot.instance.tomlManager.getToml().testMode) {
                errorChannel.sendMessage(message + "\n```Java\n" + exceptionAsString + "\n```").queue();
                return;
            }
            errorChannel.sendMessage(message + "\n```Java\n" + exceptionAsString + "\n```").queue();
        } catch (Exception ex) {
            @NotNull StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            webHook(message, exceptionAsString, "Overwatch");
        }
    }

    public static void sendToDiscord(Throwable throwable) {
        StringWriter sw;
        try {
            sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();

            if (sendErrorToDiscord(exceptionAsString)) return;
        } catch (Exception ex) {
            sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            webHook("Could not send to discord.", exceptionAsString, "Overwatch");
        }
    }


    private static void logToFile(String fileName, String message) {
        @NotNull final String fileLocation = "/Settings/Logs";
        @NotNull final File directory = new File(DiscordBot.instance.registerStuff.filePath + fileLocation);
        @NotNull final File MSGLog = new File(directory.getPath() + fileName);
        makeDirectory(directory);
        List<String> messages = new ArrayList<>();
        messages.add(message.trim());
        try {
            Files.write(MSGLog.toPath(), messages, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            //I don't rly care
        }
    }

    public static void error(String message) {
        sendToConsole(ConsoleColors.RED + message + ConsoleColors.RESET);
        sendToDiscord(message);
    }

    public static void error(Throwable throwable) {
        sendToConsole(throwable);
        sendToDiscord(throwable);
    }

    public static void error(String message, Throwable throwable) {
        sendToDiscord(message, throwable);
        sendToConsole(ConsoleColors.RED + message + ConsoleColors.RESET, throwable);
    }

    public static void error(CommandEvent event, Throwable throwable) {
        sendToConsole(throwable);
        sendToDiscord(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + ": " + event.getMessage().getContentRaw(), throwable);
    }


    public static void info(String message) {
        sendToConsole(ConsoleColors.WHITE + "[Info] " + message + ConsoleColors.RESET);
    }


    public static void warn(String message) {
        sendToConsole(ConsoleColors.RED_BRIGHT + "[WARNING] " + message + ConsoleColors.RESET);
    }


    public static void webHook(String title, String message, String name) {
        if (DiscordBot.instance.tomlManager.getToml().testMode) return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("wait", false);
        jsonObject.put("content", title + "\n```Java\n" + message.substring(0, Math.min(message.length(), 1900)) + "\n```");
        jsonObject.put("username", name);
        jsonObject.put("avatar_url", "https://lh3.googleusercontent.com/iAAi10x_VAthDyioYzFUrpAI_TTfrpDouT_y0K8ChCCsDpE-JDl2ZsrdHN89oVdMlA");
        jsonObject.put("tts", false);
        OkHttpClient client = new OkHttpClient();
        String url = "https://discordapp.com/api/webhooks/531077203459440640/nmLniKuTa_a0QFfzZeZ5QJmHrJlrcuCrX0MSAg9Zbt6o7hXn6euaY5MLzpt3X7w820Bs";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            //System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override

    public void uncaughtException(Thread t, @NotNull Throwable e) {
        error(e);
    }
}
