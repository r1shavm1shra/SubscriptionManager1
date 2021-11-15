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
	private final LiveData<List<Subscription>> mAllSubscriptions;

	public SubscriptionViewModel(@NonNull Application application) {
		super(application);
		mRepository = new SubscriptionRepository(application);
		mAllSubscriptions = mRepository.getAllSubscriptions();
	}

	public boolean containsSubscription(Subscription Subscription) {
		boolean accountInList = false;

		LiveData<Subscription> SubscriptionLiveData = mRepository.findSubscriptionByName(Subscription);
		Subscription theSubscription = SubscriptionLiveData.getValue();
		if (Objects.requireNonNull(theSubscription).getName().equals(Subscription.getName()) &&
				Objects.requireNonNull(theSubscription).getCost().equals(Subscription.getCost())) {
			accountInList = true;
		}

		return accountInList;
	}

	public LiveData<Subscription> getSubscription(Subscription Subscription) {
		return mRepository.findSubscriptionByName(Subscription);
	}

	public LiveData<List<Subscription>> getAllSubscriptions() { return mAllSubscriptions; }

	public void insert(Subscription Subscription) {
		mRepository.insert(Subscription);
	}
	public void delete(Subscription Subscription) {
		mRepository.delete(Subscription);
	}
}
