package com.group10.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.group10.myapplication.data.model.Subscription;

import java.util.List;

@Dao
public interface SubscriptionDao {
	@Query("SELECT * FROM Subscription")
	public LiveData<List<Subscription>> getAllSubscriptions();

	@Query("SELECT rowid, name, cost, duedate FROM Subscription WHERE name LIKE :name LIMIT 1")
	public LiveData<Subscription> findByName(String name);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	public void insert(Subscription Subscription);

	@Update
	public void updateSubscription(Subscription Subscription);

	@Delete
	public void delete(Subscription Subscription);
}
