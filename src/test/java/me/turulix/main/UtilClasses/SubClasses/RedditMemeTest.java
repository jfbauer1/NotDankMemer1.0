/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses.SubClasses;

import me.turulix.main.Listeners.TestFailListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestFailListener.class)
class RedditMemeTest {
    private String imageURL = "Test";
    private String permLink = "http://PermTestURL";
    private String title = "Best Meme On The Planet";
    private String id = "ksjfjisdahufi";
    private int Comments = 12837;
    private int ThumpsUp = 921389;
    private SubClasses.RedditMeme meme = new SubClasses.RedditMeme(imageURL, permLink, Comments, ThumpsUp, title, id);

    @Test
    void getId() {
        assertEquals(id, meme.getId());
    }

    @Test
    void getImageURL() {
        assertEquals(imageURL, meme.getImageURL());
    }

    @Test
    void getPermLink() {
        assertEquals(permLink, meme.getPermLink());
    }

    @Test
    void getCommentAmount() {
        assertEquals(Comments, meme.getCommentAmount());
    }

    @Test
    void getThumbsUp() {
        assertEquals(ThumpsUp, meme.getThumbsUp());
    }

    @Test
    void getTitle() {
        assertEquals(title, meme.getTitle());
    }
}