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

	@Query("SELECT rowid, name, cost, duedate FROM Subscription WHERE rowid = :id LIMIT 1")
	public LiveData<Subscription> findById(int id);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	public void insert(Subscription Subscription);

	@Query("Update Subscription set name=:name, cost=:cost, duedate=:duedate WHERE rowid = :id")
	public void updateSubscription(int id, String name, String cost, String duedate);

	@Delete
	public void delete(Subscription Subscription);

}
