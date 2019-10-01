/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 22:30.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses.SubClasses;

import java.util.UUID;

public class SubClasses {
    public static class RedditMeme {
        String imageURL;
        String permLink;
        String title;
        int commentAmount;
        int thumbsUp;
        String id;

        public RedditMeme(String imageURL, String permLink, int commentAmount, int thumbsUp, String title, String id) {
            this.imageURL = imageURL;
            this.permLink = permLink;
            this.commentAmount = commentAmount;
            this.thumbsUp = thumbsUp;
            this.title = title;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getPermLink() {
            return permLink;
        }

        public int getCommentAmount() {
            return commentAmount;
        }

        public int getThumbsUp() {
            return thumbsUp;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class RandomClass {
        private UUID ID;
        private String Random;
        private String CREATOR_TAG;
        private long USER_ID;

        public RandomClass(UUID ID, String Random, String CREATOR_TAG, Long USER_ID) {
            this.ID = ID;
            this.Random = Random;
            this.CREATOR_TAG = CREATOR_TAG;
            this.USER_ID = USER_ID;
        }

        public UUID getID() {
            return ID;
        }

        public String getRandom() {
            return Random;
        }

        public String getCREATOR_TAG() {
            return CREATOR_TAG;
        }

        public long getUSER_ID() {
            return USER_ID;
        }
    }

    public static class PornhubClass {
        private String title;
        private String id;
        private String thumbUrl;
        private String viewKey;
        private String duration;
        private String views;
        private String rating;
        private String added;

        public PornhubClass(String title, String id, String thumbUrl, String viewKey, String duration, String views, String rating, String added) {
            this.title = title;
            this.id = id;
            this.thumbUrl = thumbUrl;
            this.viewKey = viewKey;
            this.duration = duration;
            this.views = views;
            this.rating = rating;
            this.added = added;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public String getViewKey() {
            return viewKey;
        }

        public void setViewKey(String viewKey) {
            this.viewKey = viewKey;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getAdded() {
            return added;
        }

        public void setAdded(String added) {
            this.added = added;
        }
    }

}
