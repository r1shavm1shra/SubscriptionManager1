package com.group10.myapplication.data;

import android.app.Application;
import com.group10.myapplication.data.model.Subscription;

import androidx.lifecycle.LiveData;

import java.util.List;


/**
 * Single point of accessing Subscription data in the app.
 *
 * Source: https://developer.android.com/codelabs/android-room-with-a-view
 */
public class SubscriptionRepository {

	private SubscriptionDao mSubscriptionDao;

	private final String TAG = getClass().getSimpleName();

	SubscriptionRepository(Application application) {
		SubscriptionDatabase db = SubscriptionDatabase.getDatabase(application);
		mSubscriptionDao = db.getSubscriptionDao();
	}

	LiveData<List<Subscription>> getAllSubscriptions(String name) {
		return mSubscriptionDao.getAllSubscriptions(name);
	}

	LiveData<Subscription> findSubscriptionById(int id) {
		LiveData<Subscription> theSubscription = mSubscriptionDao.findById(id);

		return theSubscription;
	}

	// You MUST call this on a non-UI thread or the app will throw an exception.
	// I'm passing a Runnable object to the database.
	void insert(Subscription Subscription) {
		SubscriptionDatabase.databaseWriteExecutor.execute(() ->
				mSubscriptionDao.insert(Subscription));
	}

	void update(int id, Subscription Subscription) {
		SubscriptionDatabase.databaseWriteExecutor.execute(() ->
				mSubscriptionDao.updateSubscription(id,Subscription.getName(),Subscription.getCost(),Subscription.getDueDate()));
	}

	void delete(Subscription Subscription) {
		SubscriptionDatabase.databaseWriteExecutor.execute(() ->
				mSubscriptionDao.delete(Subscription));
	}
}
