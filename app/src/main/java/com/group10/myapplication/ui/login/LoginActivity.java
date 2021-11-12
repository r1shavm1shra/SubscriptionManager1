package com.group10.myapplication.ui.login;

import androidx.fragment.app.Fragment;

/**
 * Activity for user login.
 *

 */

public class LoginActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}
