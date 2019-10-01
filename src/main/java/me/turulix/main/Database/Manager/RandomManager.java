/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Database.Manager;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import me.turulix.main.UtilClasses.SubClasses.DatabaseInterface;
import me.turulix.main.UtilClasses.SubClasses.SubClasses;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class RandomManager extends DatabaseInterface {

    @NotNull
    public static ArrayList<SubClasses.RandomClass> cache = new ArrayList<>();

    public RandomManager() {
        collectionName = "RandomList";
    }

    @NotNull
    public ArrayList<SubClasses.RandomClass> getRandoms() {
        if (cache.isEmpty()) {
            FindIterable<Document> cursor = getDocument();
            cursor.forEach((Block<? super Document>) dbObject -> cache.add(new SubClasses.RandomClass(UUID.fromString((String) dbObject.get("_id")), (String) dbObject.get("msg"), (String) dbObject.get("creatorTag"), (Long) dbObject.get("userID"))));
        }
        return cache;
    }

    public void invalidateCache() {
        cache.clear();
    }

    public UUID addRandom(String msg, String creatorTag, Long userID) {
        UUID id = UUID.randomUUID();
        Document document = new Document();
        document.append("_id", id.toString());
        document.append("msg", msg);
        document.append("creatorTag", creatorTag);
        document.append("userID", userID);
        insert(document);
        return id;
    }

    public void removeRandom(UUID id) {
        remove(Filters.eq("_id", id.toString()));
    }

}