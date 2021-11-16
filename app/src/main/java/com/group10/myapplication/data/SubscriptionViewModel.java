package com.group10.myapplication.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;
import com.group10.myapplication.data.model.Subscription;

/**
 * ViewModel for the user account storage.
 */
public class SubscriptionViewModel extends AndroidViewModel {

	private SubscriptionRepository mRepository;

	public SubscriptionViewModel(@NonNull Application application) {
		super(application);
		mRepository = new SubscriptionRepository(application);
	}


	public LiveData<Subscription> getSubscription(int id) {
		return mRepository.findSubscriptionById(id);
	}

	public LiveData<List<Subscription>> getAllSubscriptions(String username) { return mRepository.getAllSubscriptions(username); }

	public void insert(Subscription Subscription) {
		mRepository.insert(Subscription);
	}
	public void delete(Subscription Subscription) {
		mRepository.delete(Subscription);
	}
	public void update(int id, Subscription Subscription) {
		mRepository.update(id, Subscription);
	}
}
