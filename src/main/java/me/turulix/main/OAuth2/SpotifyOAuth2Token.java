package me.turulix.main.OAuth2;

import me.turulix.main.DiscordBot;
import me.turulix.main.Logger;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

public class SpotifyOAuth2Token {
    private static String accessToken;
    private static Date expireDate;

    public static String getAccessToken() {
        if (expireDate == null || new Date().after(expireDate) || accessToken == null) {
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder body = new FormBody.Builder();
            body.add("client_id", DiscordBot.instance.tomlManager.getToml().tokens.spotifyClientID);
            body.add("client_secret", DiscordBot.instance.tomlManager.getToml().tokens.spotifySecret);
            body.add("grant_type", "client_credentials");
            try {
                String tokenURL = "https://accounts.spotify.com/api/token";
                Response response = client.newCall(new Request.Builder().url(tokenURL).post(body.build()).build()).execute();
                JSONObject object = new JSONObject(new String(response.body().bytes(), StandardCharsets.UTF_8));
                accessToken = object.getString("access_token");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, object.getInt("expires_in"));
                calendar.add(Calendar.MINUTE, -1);
                expireDate = calendar.getTime();
            } catch (Exception e) {
                Logger.error("Could not get Spotify AccessToken", e);
            }
        }
        return accessToken;
    }
}
