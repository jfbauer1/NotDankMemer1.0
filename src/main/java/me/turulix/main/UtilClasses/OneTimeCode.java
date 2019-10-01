package me.turulix.main.UtilClasses;

import me.turulix.main.Logger;
import me.turulix.main.UtilClasses.SubClasses.DatabaseInterface;
import org.bson.Document;
import org.json.JSONObject;

import java.io.*;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 22.03.2019 21:24
 */
public class OneTimeCode {
    private DatabaseInterface db = new DatabaseInterface();

    public void fixFields() {
        db.collectionName = "KillList";
        File file = new File("F:\\Bibliotheken\\Desktop\\GitBackup\\NotDankMemer\\kill.json");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String st;
            while ((st = reader.readLine()) != null) {
                builder.append(st);
            }
            System.out.println(builder.toString());
            JSONObject object = new JSONObject(builder.toString());
            for (int i = 0; i < object.getJSONArray("kill").length(); i++) {
                db.insert(new Document().append("_id", i + 1).append("msg", object.getJSONArray("kill").getString(i)));
            }
        } catch (FileNotFoundException e) {
            Logger.error(e);
        } catch (IOException e) {
            Logger.error(e);
        }

        db.collectionName = "RoastList";
        file = new File("F:\\Bibliotheken\\Desktop\\GitBackup\\NotDankMemer\\roasts.json");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String st;
            while ((st = reader.readLine()) != null) {
                builder.append(st);
            }
            System.out.println(builder.toString());
            JSONObject object = new JSONObject(builder.toString());
            for (int i = 0; i < object.getJSONArray("roast").length(); i++) {
                db.insert(new Document().append("_id", i + 1).append("msg", object.getJSONArray("roast").getString(i)));
            }
        } catch (FileNotFoundException e) {
            Logger.error(e);
        } catch (IOException e) {
            Logger.error(e);
        }
    }
}
