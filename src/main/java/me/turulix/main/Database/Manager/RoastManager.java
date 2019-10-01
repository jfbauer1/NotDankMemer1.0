/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Database.Manager;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import me.turulix.main.UtilClasses.SubClasses.DatabaseInterface;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RoastManager extends DatabaseInterface {

    public static ArrayList<Roast> cache = new ArrayList<>();

    public RoastManager() {
        collectionName = "RoastList";
    }

    @NotNull
    public ArrayList<Roast> getRoasts() {
        if (cache.isEmpty()) {
            FindIterable<Document> cursor = getDocument();
            cursor.forEach((Block<? super Document>) document -> cache.add(new Roast(document.getInteger("_id"), document.getString("msg"))));
        }
        return cache;
    }

    public class Roast {
        private int ID;
        private String msg;

        public Roast(int ID, String msg) {
            this.ID = ID;
            this.msg = msg;
        }

        public int getID() {
            return ID;
        }

        public String getMsg() {
            return msg;
        }

    }

}