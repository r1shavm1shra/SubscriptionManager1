package com.group10.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.group10.myapplication.data.model.UserAccount;

import java.util.List;

@Dao
public interface UserAccountDao {
	@Query("SELECT * FROM useraccount")
	public LiveData<List<UserAccount>> getAllUserAccounts();

	@Query("SELECT rowid, name, password FROM useraccount WHERE name LIKE :name AND password LIKE :password LIMIT 1")
	public LiveData<UserAccount> findByName(String name, String password);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	public void insert(UserAccount userAccount);

	@Update
	public void updateUserAccount(UserAccount userAccount);

	@Delete
	public void delete(UserAccount userAccount);

	@Query("UPDATE useraccount SET password = :password WHERE name LIKE :name")
	public void updatePassword(String name, String password);

	@Query("SELECT rowid, name, password FROM useraccount WHERE name LIKE :name LIMIT 1")
	public LiveData<UserAccount> getCurrentUser(String name);
}
