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

public class KillManager extends DatabaseInterface {
    @NotNull
    public static ArrayList<Kill> cache = new ArrayList<>();

    @NotNull
    public ArrayList<Kill> getKills() {
        if (cache.isEmpty()) {
            FindIterable<Document> cursor = getDocument("KillList");
            cursor.forEach((Block<? super Document>) dbObject -> cache.add(new Kill((int) dbObject.get("_id"), (String) dbObject.get("msg"))));
        }
        return cache;
    }


    public class Kill {
        private int ID;
        private String msg;

        public Kill(int ID, String msg) {
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