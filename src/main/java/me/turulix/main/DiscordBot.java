/*
 * Developed by Turulix on 20.01.19 23:44.
 * Last modified 20.01.19 23:43.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.neovisionaries.ws.client.WebSocketFactory;
import me.turulix.main.Database.Database;
import me.turulix.main.Database.Manager.GuildSettingsDataManager;
import me.turulix.main.Files.TomlManager;
import me.turulix.main.Listeners.CMDListener;
import me.turulix.main.UtilClasses.OneTimeCode;
import me.turulix.main.UtilClasses.SubClasses.SubClasses;
import me.turulix.main.UtilClasses.Utils;
import me.turulix.main.Webserver.ApiWebServer;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class DiscordBot implements Runnable {
    public static DiscordBot instance;
    private static boolean started = false;
    @NotNull
    public final RegisterStuff registerStuff;
    public TomlManager tomlManager;
    private Scanner scanner;
    private boolean running;
    private Logger logger;

    public DiscordBot(Boolean connectToDiscord, Boolean initialiseCommandHandler) {
        @NotNull final ScheduledExecutorService threadpool;
        threadpool = Executors.newScheduledThreadPool(50);
        instance = this;
        registerStuff = new RegisterStuff();
        tomlManager = new TomlManager();
        Locale.setDefault(new Locale("en", "US"));
        System.out.println("FilePath: " + registerStuff.filePath);
        if (registerStuff.filePath.equalsIgnoreCase("/root/NotDankMemer/AutoUpdate/notdankmemer")) {
            registerStuff.filePath = "/root/NotDankMemer/Bot/";
            tomlManager.getToml().testMode = true;
        }

        registerStuff.RunMethods();

        if (tomlManager.getToml().testMode) {
            tomlManager.getToml().sql.SQLLocation = tomlManager.getToml().sql.SQLLocation + "Test";
            tomlManager.getToml().sql.SQLUsername = tomlManager.getToml().sql.SQLLocation.split("/")[1];
        }
        try {
            registerStuff.database = new Database(tomlManager.getToml().sql.SQLLocation, tomlManager.getToml().sql.SQLUsername, tomlManager.getToml().sql.SQLPassword);
            if (tomlManager.getToml().runOneTimeCode) {
                new OneTimeCode().fixFields();
            }
            tomlManager.save();
        } catch (Exception ex) {
            Logger.error(ex);
        }

        if (initialiseCommandHandler) {
            @NotNull CommandClientBuilder builder = new CommandClientBuilder();
            builder.setOwnerId("262702226693160970");
            builder.setCoOwnerIds("141268459991334912", "307980221422764032");
            builder.setPrefix("+");
            builder.useHelpBuilder(true);

            builder.setHelpConsumer(event -> {
                @NotNull StringBuilder categoryCommands = new StringBuilder();
                registerStuff.helpMap.forEach((s, commands) -> {
                    boolean addHeader = false;
                    for (@NotNull Command command : commands) {
                        if (command.isHidden() && !event.isOwner()) continue;
                        if (command.isOwnerCommand() && !event.isOwner()) continue;
                        addHeader = true;
                    }
                    if (addHeader) categoryCommands.append("__**").append(s).append(":**__\n");

                    for (@NotNull Command command : commands) {
                        if (command.isHidden() && !event.isOwner()) continue;
                        if (command.isOwnerCommand() && !event.isOwner()) continue;

                        categoryCommands.append("``").append(registerStuff.commandClient.getPrefix()).append(command.getName()).append(" ").append(command.getArguments() == null ? "" : command.getArguments()).append("``").append(command.getHelp() == null ? "" : "- " + command.getHelp()).append("\n");
                    }
                    if (addHeader) categoryCommands.append("\n");

                });
                categoryCommands.append("For more help visit ").append(registerStuff.commandClient.getServerInvite()).append(" \n -Turulix");
                //categoryCommands.append("\nIf you want to support the bot check out DonateBot on the support server\n Or by useing Patreon (https://www.patreon.com/NotDankMemer)");
                for (String s : CommandEvent.splitMessage(categoryCommands.toString())) {
                    AtomicBoolean error = new AtomicBoolean(false);
                    event.replyInDm(s, success -> {
                        if (event.isFromType(ChannelType.TEXT)) {
                            event.reactSuccess();
                        }
                    }, throwable -> {
                        event.replyWarning("Help cannot be sent because you are blocking Direct Messages.");
                        error.set(true);
                    });
                    if (error.get()) {
                        break;
                    }
                }
            });
            builder.setListener(new CMDListener());
            if (!tomlManager.getToml().testMode) {
                builder.setDiscordBotListKey(tomlManager.getToml().tokens.DiscordBotsToken);
            }
            //builder.setDiscordBotsKey(registerStuff.tokens.get(4));
            builder.setEmojis("✅", "⚠", "⛔");
            builder.setLinkedCacheSize(0);
            builder.setGuildSettingsManager(new GuildSettingsDataManager());
            //builder.setShutdownAutomatically(false);
            builder.setServerInvite("https://discord.gg/CYVjCvV");
            builder.setScheduleExecutor(threadpool);
            registerStuff.addCommands(builder);
            registerStuff.commandClient = builder.build();
        }

        String tokenToUse = tomlManager.getToml().tokens.token;
        if (tomlManager.getToml().testMode) {
            tokenToUse = tomlManager.getToml().tokens.testToken;
        }

        registerStuff.sessionController = new SessionControllerAdapter();
        if (connectToDiscord) {
            @NotNull DefaultShardManagerBuilder shardManagerBuilder = new DefaultShardManagerBuilder();
            shardManagerBuilder.setToken(tokenToUse);

            shardManagerBuilder.addEventListeners(new CMDListener(), registerStuff.commandClient);
            shardManagerBuilder.setAutoReconnect(true);
            shardManagerBuilder.addEventListeners(registerStuff.eventWaiter);
            shardManagerBuilder.setSessionController(registerStuff.sessionController);
            if (tomlManager.getToml().testMode) {
                shardManagerBuilder.setShardsTotal(1);
            } else {
                shardManagerBuilder.setShardsTotal(1);
            }
            shardManagerBuilder.setGame(Game.playing("Stating bot."));
            shardManagerBuilder.setWebsocketFactory(new WebSocketFactory().setVerifyHostname(false));
            try {
                registerStuff.shardManager = shardManagerBuilder.build();
                for (@NotNull JDA shard : registerStuff.shardManager.getShards()) {
                    shard.awaitReady();
                }
            } catch (Exception ex) {
                Logger.error(ex);
            }
            Logger.info(registerStuff.shardManager.getShards().size() + " Shards started!");
        }
        started = true;
        if (initialiseCommandHandler) registerStuff.commandClient.getSettingsManager().init();

        scanner = new Scanner(System.in);
        if (initialiseCommandHandler) {
            registerStuff.commandClient.getCommands().forEach(command -> {
                if (command.getCategory() != null) {
                    if (registerStuff.helpMap.get(command.getCategory().getName()) == null) {
                        Logger.info(command.getCategory().getName() + " Is Null creating");
                        registerStuff.helpMap.put(command.getCategory().getName(), new ArrayList<>());
                    }
                    registerStuff.helpMap.get(command.getCategory().getName()).add(command);
                } else {
                    if (registerStuff.helpMap.get("No Category") == null) {
                        Logger.info("No Category Is Null creating");
                        registerStuff.helpMap.put("No Category", new ArrayList<>());
                    }
                    registerStuff.helpMap.get("No Category").add(command);
                }
            });
        }

        for (@NotNull Thread thread : Thread.getAllStackTraces().keySet()) {
            thread.setUncaughtExceptionHandler(instance.logger);
        }
    }

    public static void main(String[] args) {
        //@NotNull DiscordBot discordBot = new DiscordBot();
        //Thread bot = new Thread(discordBot, "bot");
        //bot.start();
        @NotNull Logger logger = new Logger();
        Thread.setDefaultUncaughtExceptionHandler(logger);

        new ApiWebServer();

        new DiscordBot(true, true);
        instance.logger = logger;

        @NotNull Timer MinUpdate = new Timer();
        @NotNull Timer HourUpdate = new Timer();
        Timer DayUpdate = new Timer();
        MinUpdate.schedule(new TimerTask() {
            public void run() {
                if (started) {
                    instance.registerStuff.shardManager.setGame(Game.of(Game.GameType.DEFAULT, "on " + instance.registerStuff.shardManager.getGuilds().size() + " servers! | " + instance.registerStuff.commandClient.getPrefix() + "help"));
                    instance.registerStuff.musicManager.LeaveEmpty();
                }
            }
        }, 0, 60 * 1000);

        HourUpdate.schedule(new TimerTask() {
            public void run() {
                try {
                    Logger.info("Updating Meme Cache");
                    String after = "null";
                    DiscordBot.instance.registerStuff.cachedMemes.clear();
                    for (int i = 0; i < 3; i++) {
                        @Nullable String memeString = Utils.getUrl("https://old.reddit.com/u/turulix/m/notdankmemer/top/.json?sort=top&t=day&limit=100&after=" + after);
                        @NotNull JSONObject obj = new JSONObject(memeString);
                        JSONArray arr = obj.getJSONObject("data").getJSONArray("children");
                        try {
                            after = obj.getJSONObject("data").getString("after");
                        } catch (JSONException ex) {
                            after = "null";
                        }
                        for (Object o : arr) {
                            JSONObject meme = (JSONObject) o;
                            meme = meme.getJSONObject("data");
                            String url = meme.getString("url");
                            @NotNull String permURL = "https://www.reddit.com" + meme.getString("permalink");
                            String title = meme.getString("title");
                            int commentAmount = meme.getInt("num_comments");
                            int upVotes = meme.getInt("ups");
                            String id = meme.getString("id");
                            DiscordBot.instance.registerStuff.cachedMemes.add(new SubClasses.RedditMeme(url, permURL, commentAmount, upVotes, title, id));
                        }
                        if (after.equalsIgnoreCase("null")) {
                            break;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.error(ex);
                }
            }
        }, 0, ((60 * 1000) * 60));

        DayUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                instance.registerStuff.database.userManager.clearCache();
            }
        }, 0, (60 * 1000) * 60 * 24);

    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {
        this.running = true;
        while (this.running) {
            //noinspection StatementWithEmptyBody
            if (this.scanner.hasNextLine()) {

            }
        }

        registerStuff.commandClient.getSettingsManager().shutdown();
        this.scanner.close();
        System.out.println("Bot stopped.");

        System.exit(0);
    }
}

