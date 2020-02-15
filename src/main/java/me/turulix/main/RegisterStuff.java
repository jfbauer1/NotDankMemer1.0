/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.jagrosh.jdautilities.examples.command.GuildlistCommand;
import me.turulix.main.Commands.BotOwner.EmotesListCommand;
import me.turulix.main.Commands.Memes.GenericMeme;
import me.turulix.main.Commands.Music.Managers.MusicManager;
import me.turulix.main.Database.Database;
import me.turulix.main.UtilClasses.Anotations.DankCommand;
import me.turulix.main.UtilClasses.SubClasses.SubClasses;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.SessionController;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.fail;

public class RegisterStuff {
    @NotNull
    public final List<String> trumpPictures;
    @NotNull
    public final Map<String, String> StaticGifs;
    public List<SubClasses.RedditMeme> cachedMemes;
    @NotNull
    public String NotDankMemerServerID = "497769262749057026";
    @NotNull
    public String NotDankMemerErrorChannelID = "502782188908052490";
    @NotNull
    public String NotDankMemerReportChannelID = "510827570711887872";
    public Map<String, List<Command>> helpMap;
    @SuppressWarnings("CanBeFinal")
    @NotNull
    public String filePath;
    public Database database;
    public ShardManager shardManager;
    public SessionController sessionController;
    public CommandClient commandClient;
    public EventWaiter eventWaiter;
    public MusicManager musicManager;

    RegisterStuff() {
        cachedMemes = new ArrayList<>();
        filePath = System.getProperty("user.dir");
        trumpPictures = new ArrayList<>();
        StaticGifs = new HashMap<>();
        helpMap = new HashMap<>();
        eventWaiter = new EventWaiter();
        musicManager = new MusicManager();
    }

    public void RunMethods() {
        registerOther();
        addTrumpPictures();
    }

    void addCommands(@NotNull CommandClientBuilder ccb) {
        Logger.info("Adding Commands");

        Reflections reflections = new Reflections("me.turulix.main");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(DankCommand.class);
        AtomicInteger atomicInteger = new AtomicInteger();
        annotated.forEach(aClass -> {
            Class<Command> command = (Class<Command>) aClass;
            Constructor<Command> commandConstructor = (Constructor<Command>) command.getConstructors()[0];
            try {
                Command finalCommand = commandConstructor.newInstance();
                Logger.info(finalCommand.getName() + " Registered.");
                ccb.addCommand(finalCommand);
                atomicInteger.getAndAdd(1);
            } catch (Exception e) {
                Logger.webHook("Command: " + command.getSimpleName() + " Failed to initialise.", e.toString(), "Overwatch");
                fail("Error initialising commands.", e);
            }
        });

        Logger.info(atomicInteger.get() + " Commands added.");

        //ccb.addCommand(new TestCommand());

        ccb.addCommand(new GenericMeme("abandon", "Creates a abandon Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("armor", "Creates a armor Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("balloon", "Creates a balloon Meme :D", "<Text>, <Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("boo", "boo", "<Text>, <Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("brain", "Creates a brain Meme :D", "<Text>, <Text>, <Text>, <Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("changemymind", "Creates a changemymind Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("cry", "Creates a cry Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("facts", "Creates a facts Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("humansgood", "Creates a humansgood Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("knowyourlocation", "Creates a knowyourlocation Meme :D", "<Text>, <Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("note", "Creates a note Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("ohno", "Creates a ohno Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("plan", "Creates a plan Meme :D", "<Text>, <Text>, <Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("savehumanity", "Creates a savehumanity Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("shit", "Creates a shit Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("thesearch", "Creates a thesearch Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("tweet", "Creates a tweet Meme :D", "<Text>", "Image.png", 0));
        ccb.addCommand(new GenericMeme("laid", "Creates a laid Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("brazzers", "Creates a brazzers Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("cancer", "Creates a cancer Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("dab", "Creates a dab Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("dank", "Creates a dank Meme :D", "<@User>", "Image.gif", 1));
        ccb.addCommand(new GenericMeme("deepfry", "Creates a deepfry Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("delete", "Creates a delete Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("disability", "Creates a disability Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("door", "Creates a door Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("egg", "Creates a egg Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("failure", "Creates a failure Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("fakenews", "Creates a fakenews Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("fedora", "Creates a fedora Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("gay", "Creates a gay Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("hitler", "Creates a hitler Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("invert", "Creates a invert Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("jail", "Creates a jail Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("magik", "Creates a magik Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("rip", "Creates a rip Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("roblox", "Creates a roblox Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("salty", "Creates a salty Meme :D", "<@User>", "Image.gif", 1));
        ccb.addCommand(new GenericMeme("satan", "Creates a satan Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("ban", "Creates a sickfilth Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("trash", "Creates a trash Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("trigger", "Creates a trigger Meme :D", "<@User>", "Image.gif", 1));
        ccb.addCommand(new GenericMeme("ugly", "Creates a ugly Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("wanted", "Creates a wanted Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("whodidthis", "Creates a whodidthis Meme :D", "<@User>", "Image.png", 1));
        ccb.addCommand(new GenericMeme("bed", "Creates a bed Meme :D", "<@User>, <@User>", "Image.png", 2));
        ccb.addCommand(new GenericMeme("madethis", "Creates a madethis Meme :D", "<@User>, <@User>", "Image.png", 2));
        ccb.addCommand(new GenericMeme("screams", "Creates a screams Meme :D", "<@User>, <@User>", "Image.png", 2));
        ccb.addCommand(new GenericMeme("slap", "Creates a slap Meme :D", "<@User>, <@User>", "Image.png", 2));
        ccb.addCommand(new GenericMeme("spank", "Creates a spank Meme :D", "<@User>, <@User>", "Image.png", 2));
        ccb.addCommand(new GenericMeme("floor", "Creates a floor Meme :D", "<@User>, <Text>", "Image.png", 3));
        ccb.addCommand(new GenericMeme("byemom", "Creates a byemom Meme :D", "<@User>, <Text>", "Image.png", 4));
        ccb.addCommand(new GenericMeme("quote", "Creates a floor Meme :D", "<@User>, <Text>", "Image.png", 4));

/*        ccb.addCommand(new LoveCommand());
        ccb.addCommand(new AnnounceCommand());
        ccb.addCommand(new ConfigCommand());
        ccb.addCommand(new PewDiePieCommand());
        ccb.addCommand(new Rule34Command());
        ccb.addCommand(new LeaveCommand());
        ccb.addCommand(new EvalCommand());
        ccb.addCommand(new TrumpCommand());
        ccb.addCommand(new ChuckCommand());
        ccb.addCommand(new InfoCommand());
        ccb.addCommand(new PlayCommand());
        ccb.addCommand(new SkipCommand());
        ccb.addCommand(new CatCommand());
        ccb.addCommand(new FMLCommand());
        ccb.addCommand(new SuggestionCommand());
        ccb.addCommand(new NikoCommand());
        ccb.addCommand(new VolumeCommand());
        ccb.addCommand(new RandomCommand());
        ccb.addCommand(new CurrentsongCommand());
        ccb.addCommand(new NekoCommand());
        ccb.addCommand(new RoastCommand());
        ccb.addCommand(new DebugCommand());
        ccb.addCommand(new PenisCommand());
        ccb.addCommand(new WabBapCommand());
        ccb.addCommand(new Urban());
        ccb.addCommand(new Meme());
        ccb.addCommand(new SayCommand());
        ccb.addCommand(new KillCommand());*/
        //ccb.addCommand(new EmotesListCommand(eventWaiter));
        ccb.addCommands(new EmotesListCommand(eventWaiter));
        ccb.addCommand(new GuildlistCommand(eventWaiter));
        ccb.addCommand(new AboutCommand(Color.GREEN, "not Dank memer :D", new String[]{"Memes", "Nsfw ;)", "Administration Commands"}, Permission.ADMINISTRATOR));
    }

    private void addTrumpPictures() {
        at("https://goo.gl/wrvxYT");
        at("https://goo.gl/VswDFJ");
        at("https://goo.gl/8zWaI7");
        at("https://goo.gl/3ej1lJ");
        at("https://goo.gl/MnN4TZ");
        at("https://goo.gl/wWy44v");
        at("https://goo.gl/gY7EpE");
        at("https://goo.gl/CI7jBq");
        at("https://goo.gl/nanvBL");
    }

    private void registerOther() {
        StaticGifs.put("nsfw", "https://i.imgur.com/oe4iK5i.gif");
        StaticGifs.put("externalEmojis", "https://i.imgur.com/JR5iwfy.gif");
        StaticGifs.put("attachFiles", "https://i.imgur.com/71C8BZY.gif");
        StaticGifs.put("embedLinks", "https://i.imgur.com/NVFcl9b.gif");
        StaticGifs.put("manageMessages", "https://i.imgur.com/DLEby8x.gif");
        StaticGifs.put("addReactions", "https://i.imgur.com/wuwSSwE.gif");
        StaticGifs.put("voiceConnect", "https://i.imgur.com/BJIwSaj.gif");
        StaticGifs.put("voiceSpeak", "https://i.imgur.com/wiNvMfs.gif");
        StaticGifs.put("voiceUseVAD", "https://i.imgur.com/ZEddIyP.gif");
        StaticGifs.put("readMessages", "https://i.imgur.com/lbBdKFT.gif");
        StaticGifs.put("sendMessages", "https://i.imgur.com/WawAYW4.gif");
        StaticGifs.put("readMessageHistory", "https://i.imgur.com/oG5a09j.gif");
        StaticGifs.put("noPerm", "https://i.imgur.com/8drAz8a.gif");
    }

    private void at(String link) {
        trumpPictures.add(link);
    }

}
