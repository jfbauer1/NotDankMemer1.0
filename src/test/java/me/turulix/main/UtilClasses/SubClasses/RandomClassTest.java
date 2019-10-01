/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses.SubClasses;

import me.turulix.main.Listeners.TestFailListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestFailListener.class)
class RandomClassTest {
    UUID id = UUID.randomUUID();
    String Random = "Test 1";
    String CreatorTag = "Turulix";
    Long UserID = 941823L;
    SubClasses.RandomClass random = new SubClasses.RandomClass(id, Random, CreatorTag, UserID);

    @Test
    void getID() {
        assertEquals(id, random.getID());
    }

    @Test
    void getRandom() {
        assertEquals(Random, random.getRandom());
    }

    @Test
    void getCREATOR_TAG() {
        assertEquals(CreatorTag, random.getCREATOR_TAG());
    }

    @Test
    void getUSER_ID() {
        assertEquals(UserID, (Long) random.getUSER_ID());
    }
}