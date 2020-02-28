package com.example.drinktracker.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.drinktracker.models.UserProfile;

import java.util.Optional;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * DrinkTrackerDatabase
 */
@Database(entities = {UserProfile.class}, version = 1)
public abstract class DrinkTrackerDatabase extends RoomDatabase {

    private static final String DB_NAME = "drink_tracker.db";

    private static DrinkTrackerDatabase INSTANCE;

    private static final Object lock = new Object();

    public abstract UserProfileDao userProfileDao();

    public static DrinkTrackerDatabase getInstance(Context context) {
        synchronized (lock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        DrinkTrackerDatabase.class, DB_NAME)
                        .build();
            }
            return INSTANCE;
        }
    }

    public Observable<Optional<UserProfile>> userProfile() {
        return Observable.fromCallable(
                new Callable<Optional<UserProfile>>() {
                    @Override
                    public Optional<UserProfile> call() throws Exception {
                        UserProfile userProfile =
                                DrinkTrackerDatabase.this.userProfileDao().getUserProfile();
                        return Optional.ofNullable(userProfile);
                    }
                }
        )
        .subscribeOn(Schedulers.io());
    }
}
