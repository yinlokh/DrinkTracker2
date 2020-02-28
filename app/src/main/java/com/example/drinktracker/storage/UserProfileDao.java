package com.example.drinktracker.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.drinktracker.models.UserProfile;

@Dao
public interface UserProfileDao {

    @Query("SELECT * FROM userprofile LIMIT 1")
    UserProfile getUserProfile();

    @Insert
    void setUserProfile(UserProfile userProfile);
}
