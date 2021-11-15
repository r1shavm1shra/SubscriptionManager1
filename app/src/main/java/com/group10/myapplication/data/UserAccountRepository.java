package com.group10.myapplication.data;

import android.app.Application;
import com.group10.myapplication.data.model.UserAccount;

import androidx.lifecycle.LiveData;

import java.util.List;


/**
 * Single point of accessing UserAccount data in the app.
 *
 * Source: https://developer.android.com/codelabs/android-room-with-a-view
 */
public class UserAccountRepository {

	private UserAccountDao mUserAccountDao;
	private LiveData<List<UserAccount>> mAllUserAccounts;

	private final String TAG = getClass().getSimpleName();

	UserAccountRepository(Application application) {
		UserAccountDatabase db = UserAccountDatabase.getDatabase(application);
		mUserAccountDao = db.getUserAccountDao();
		mAllUserAccounts = mUserAccountDao.getAllUserAccounts();
	}

	LiveData<List<UserAccount>> getAllUserAccounts() {
		return mAllUserAccounts;
	}

	LiveData<UserAccount> findUserAccountByName(UserAccount userAccount) {
		LiveData<UserAccount> theUserAccount = mUserAccountDao.findByName(userAccount.getName(), userAccount.getPassword());

		return theUserAccount;
	}

	// You MUST call this on a non-UI thread or the app will throw an exception.
	// I'm passing a Runnable object to the database.
	void insert(UserAccount userAccount) {
		UserAccountDatabase.databaseWriteExecutor.execute(() ->
				mUserAccountDao.insert(userAccount));
	}

	void update(UserAccount userAccount) {
		UserAccountDatabase.databaseWriteExecutor.execute(() ->
				mUserAccountDao.updateUserAccount(userAccount));
	}

	void updatePassword(String name, String password){
		UserAccountDatabase.databaseWriteExecutor.execute(() ->
				mUserAccountDao.updatePassword(name, password));
	}
	LiveData<UserAccount> getCurrentUser(String name){
		return mUserAccountDao.getCurrentUser(name);
	}

	void delete(UserAccount userAccount) {
		UserAccountDatabase.databaseWriteExecutor.execute(() ->
				mUserAccountDao.delete(userAccount));
	}


}
