package com.example.drinktracker.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Stores the user's profile
 */
@Entity
public class UserProfile {

    @PrimaryKey private final int id;
    private final boolean isMale;
    private final int weightLbs;

    public UserProfile(int id,boolean isMale, int weightLbs) {
        this.id = id;
        this.isMale = isMale;
        this.weightLbs = weightLbs;
    }

    public int getId() {
        return id;
    }

    public boolean isMale() {
        return isMale;
    }

    public int getWeightLbs() {
        return weightLbs;
    }
}
