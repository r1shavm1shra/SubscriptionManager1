package com.group10.myapplication.ui.settings;

import androidx.fragment.app.Fragment;

import com.group10.myapplication.ui.login.LoginFragment;
import com.group10.myapplication.ui.login.SingleFragmentActivity;

public class SettingsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }
}
