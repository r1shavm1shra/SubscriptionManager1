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
	private LiveData<List<Subscription>> mAllSubscriptions;

	private final String TAG = getClass().getSimpleName();

	SubscriptionRepository(Application application) {
		SubscriptionDatabase db = SubscriptionDatabase.getDatabase(application);
		mSubscriptionDao = db.getSubscriptionDao();
		mAllSubscriptions = mSubscriptionDao.getAllSubscriptions();
	}

	LiveData<List<Subscription>> getAllSubscriptions() {
		return mAllSubscriptions;
	}

	LiveData<Subscription> findSubscriptionByName(Subscription Subscription) {
		LiveData<Subscription> theSubscription = mSubscriptionDao.findByName(Subscription.getName());

		return theSubscription;
	}

	// You MUST call this on a non-UI thread or the app will throw an exception.
	// I'm passing a Runnable object to the database.
	void insert(Subscription Subscription) {
		SubscriptionDatabase.databaseWriteExecutor.execute(() ->
				mSubscriptionDao.insert(Subscription));
	}

	void update(Subscription Subscription) {
		SubscriptionDatabase.databaseWriteExecutor.execute(() ->
				mSubscriptionDao.updateSubscription(Subscription));
	}

	void delete(Subscription Subscription) {
		SubscriptionDatabase.databaseWriteExecutor.execute(() ->
				mSubscriptionDao.delete(Subscription));
	}
}
