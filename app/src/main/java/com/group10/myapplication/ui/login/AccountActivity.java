package com.group10.myapplication.ui.login;

import com.group10.myapplication.R;

import androidx.fragment.app.Fragment;

/**
 * Activity for managing user accounts.
 *
 
 */

public class AccountActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AccountFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
}
