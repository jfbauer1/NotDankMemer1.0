package me.turulix.main.Webserver;

import com.mongodb.client.model.Filters;
import me.turulix.main.DiscordBot;
import me.turulix.main.UtilClasses.SubClasses.DatabaseInterface;
import org.bson.BsonDateTime;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static spark.Spark.port;
import static spark.Spark.post;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 22.03.2019 22:05
 */
public class ApiWebServer extends DatabaseInterface {
    public ApiWebServer() {
        collectionName = "UserDatabase";
        port(28138);
        post("/vote", ((request, response) -> {
            JSONObject body = null;
            try {
                body = new JSONObject(request.body());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            if (request.headers("Authorization").equals(DiscordBot.instance.tomlManager.getToml().auth.webHookSecret)) {
                if (body != null && body.getString("bot").equals("277608782123630593")) {
                    if (body.getString("type").equals("upvote")) {
                        Long userID = body.getLong("user");
                        if (getDocument(Filters.eq("_id", userID)) != null) {
                            update(Filters.eq("_id", userID), "voteDate", new BsonDateTime(new Date().getTime()));
                            return "Accepted";
                        } else {
                            insert(new Document().append("_id", userID));
                            update(Filters.eq("_id", userID), "voteDate", new BsonDateTime(new Date().getTime()));
                            return "Accepted";
                        }
                    }
                }
                return "Declined.";
            } else {
                return "Declined.";
            }
        }));
    }
}
