package com.group10.myapplication.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.group10.myapplication.data.model.Subscription;

/**
 * Database class for Subscription processing with Room.
 *
 * Source: https://developer.android.com/codelabs/android-room-with-a-view
 *
 */
@Database(entities = {Subscription.class}, version = 1, exportSchema = false)
public abstract class SubscriptionDatabase extends RoomDatabase  {
	public abstract SubscriptionDao getSubscriptionDao();

	private static volatile SubscriptionDatabase sInstance;
	private static final int sNumberOfThreads = 2;
	static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(sNumberOfThreads);

	static SubscriptionDatabase getDatabase(final Context context) {
		if (sInstance == null) {
			synchronized (SubscriptionDatabase.class) {
				if (sInstance == null) {
					sInstance = Room.databaseBuilder(context.getApplicationContext(),
							SubscriptionDatabase.class, "subscription_database")
							.build();
				}
			}
		}

		return sInstance;
	}
}
