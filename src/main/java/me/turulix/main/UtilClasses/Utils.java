/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses;


import me.turulix.main.DiscordBot;
import me.turulix.main.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    @Deprecated
    public static String OldgetUrl(@NotNull String URLString) {
        try {
            @NotNull URL url = new URL(URLString);
            URLConnection hc = url.openConnection();
            hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
            @NotNull BufferedReader reader = new BufferedReader(new InputStreamReader(hc.getInputStream(), StandardCharsets.UTF_8));
            String s;
            @NotNull StringBuilder msg = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                msg.append(s);
            }
            reader.close();
            return msg.toString();
        } catch (Exception ex) {
            Logger.error(ex);
        }
        return null;

    }

    public static String getUrl(String URLString) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URLString).get().build();
        Response response;
        try {
            response = client.newCall(request).execute();
            return new String(response.body().bytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.error(e);
        }
        return null;
    }

    public static String getUrl(String URLString, String AuthToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URLString).header("Authorization", AuthToken).get().build();
        Response response;
        try {
            response = client.newCall(request).execute();
            return new String(response.body().bytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.error(e);
        }
        return null;
    }

    public static InputStream getMeme(String domain, String text, String avatar1, String avatar2, String username1, String username2, String Token) {
        try {
            JSONObject jsonObject = new JSONObject("{}");
            jsonObject.put("text", text);
            jsonObject.put("avatar1", avatar1);
            jsonObject.put("avatar2", avatar2);
            jsonObject.put("username1", username1);
            jsonObject.put("username2", username2);
            String json = jsonObject.toString();

            URL url = new URL(domain);
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            hc.setDoOutput(true);
            hc.setRequestMethod("POST");
            hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
            hc.setRequestProperty("authorization", Token);
            hc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            hc.setRequestProperty("Content-Length", String.valueOf(json.getBytes(StandardCharsets.UTF_8).length));
            hc.setConnectTimeout(10000);
            //hc.setFixedLengthStreamingMode(json.getBytes(StandardCharsets.UTF_8).length);

            hc.connect();
            OutputStream os = hc.getOutputStream();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();
            return hc.getInputStream();
        } catch (Exception ex) {
            Logger.error(ex);
            return null;
        }
    }

    /**
     * Function to get the response of an API.
     *
     * @return Response or null if error.
     */
    public static InputStream getMashapeApi(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().header("X-Mashape-Key", DiscordBot.instance.tomlManager.getToml().tokens.mashapeToken).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body().byteStream();
    }

    @NotNull
    public static List<String> extractUrls(@NotNull String text) {
        @NotNull List<String> containedUrls = new ArrayList<>();
        @NotNull String urlRegex = "((https?|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?+-=\\\\\\.&]*)";
        @NotNull Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        @NotNull Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }

        return containedUrls;
    }

}
