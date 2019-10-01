/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import me.turulix.main.Database.Manager.*;
import me.turulix.main.UtilClasses.SubClasses.DatabaseInterface;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 12.01.2019 18:31
 */

public class Database extends DatabaseInterface {
    public MongoClient mongoClient;

    public GuildSettingsDataManager guildSettingsDataManager;
    public KillManager killManager;
    public RandomManager randomManager;
    public RoastManager roastManager;
    public UserManager userManager;

    public Database(String location, String user, String password) {
        String url = MessageFormat.format("mongodb://{0}:{1}@{2}/?authSource={3}&authMechanism=SCRAM-SHA-256", user, password, location.split("/")[0], location.split("/")[1]);
        //url = MessageFormat.format("mongodb://{0}:{1}@{2}/?authSource={3}", user, password, location.split("/")[0], location.split("/")[1]);

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoCredential credential = MongoCredential.createScramSha256Credential(user, location.split("/")[1], password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(codecRegistry).credential(credential).applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(location.split("/")[0], 27017)))).build();
        mongoClient = MongoClients.create(settings);

        guildSettingsDataManager = new GuildSettingsDataManager();
        killManager = new KillManager();
        randomManager = new RandomManager();
        roastManager = new RoastManager();
        userManager = new UserManager();


    }
}
