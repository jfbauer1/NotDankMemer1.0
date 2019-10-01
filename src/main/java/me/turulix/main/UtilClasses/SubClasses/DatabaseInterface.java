package me.turulix.main.UtilClasses.SubClasses;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import me.turulix.main.DiscordBot;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 22.03.2019 17:16
 */
public class DatabaseInterface {
    public String collectionName = "";

    private MongoDatabase gMD() {
        return getMongoClient().getDatabase(DiscordBot.instance.tomlManager.getToml().sql.SQLLocation.split("/")[1]);
    }

    private MongoClient getMongoClient() {
        return DiscordBot.instance.registerStuff.database.mongoClient;
    }

    public Document getDocument(Bson filters) {
        return gMD().getCollection(collectionName).find(filters).first();
    }

    public Document getDocument(Bson filters, String name) {
        return gMD().getCollection(name).find(filters).first();
    }

    public FindIterable<Document> getDocument() {
        return gMD().getCollection(collectionName).find();
    }

    public FindIterable<Document> getDocument(String name) {
        return gMD().getCollection(name).find();
    }

    public void insert(Document document) {
        gMD().getCollection(collectionName).insertOne(document);
    }

    public long remove(Bson filters) {
        return gMD().getCollection(collectionName).deleteMany(filters).getDeletedCount();
    }

    public long update(Bson filters, String key, Object value) {
        return gMD().getCollection(collectionName).updateOne(filters, Updates.set(key, value)).getMatchedCount();
    }

    public void renameField(Bson filters, String oldName, String newName) {
        if (getDocument(filters).containsKey(oldName)) {
            gMD().getCollection(collectionName).updateMany(filters, Updates.rename(oldName, newName));
        }
    }


}
