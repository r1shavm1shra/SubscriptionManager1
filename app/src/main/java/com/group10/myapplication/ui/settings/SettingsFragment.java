package com.group10.myapplication.ui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.PreferenceManager;
import androidx.room.Delete;
import androidx.room.Query;

import com.group10.myapplication.R;
import com.group10.myapplication.data.UserAccountViewModel;
import com.group10.myapplication.data.model.UserAccount;

import java.util.List;


public class SettingsFragment extends Fragment implements View.OnClickListener{

    private UserAccountViewModel mUserAccountViewModel;
    private UserAccount currentUser = getCurrentUser();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Activity activity = requireActivity();
        mUserAccountViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserAccountViewModel.class);
        // Here's a dummy observer object that indicates when the UserAccounts change in the database.
        mUserAccountViewModel.getAllUserAccounts().observe((LifecycleOwner) activity, new Observer<List<UserAccount>>() {
            @Override
            public void onChanged(List<UserAccount> userAccounts) {
                Log.d("SettingsFragment", "The list of UserAccounts just changed; it has %s elements" + userAccounts.size());
            }
        });


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        Activity activity = requireActivity();
        v = inflater.inflate(R.layout.fragment_settings, container, false);

        final Button updateBudgetButton = v.findViewById(R.id.update_budget_button);
        if (updateBudgetButton != null) {
            updateBudgetButton.setOnClickListener(this);
        }
        final Button updatePasswordButton = v.findViewById(R.id.update_password_button);
        if (updatePasswordButton != null) {
            updatePasswordButton.setOnClickListener(this);
        }
        final Button deleteAccountButton = v.findViewById(R.id.delete_account_button);
        if (deleteAccountButton != null) {
            deleteAccountButton.setOnClickListener(this);
        }

        return v;
    }

    private UserAccount getCurrentUser(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String name = preferences.getString("name", "Joe");
        //If it passes all of these checks, then we can update the password to the new password
        return mUserAccountViewModel.getCurrentUser(name).getValue();
    }

    @Override
    public void onClick(View v) {
        final Activity activity = requireActivity();
        final int viewId = v.getId();

        if (viewId == R.id.update_budget_button) {
            updateBudget();
        } else if (viewId == R.id.update_password_button) {
            updatePassword();
        } else if (viewId == R.id.delete_account_button) {
            deleteAccount();
        }

    }

    private void updatePassword() {
        //Show screen to enter old password. Then enter new password twice
        AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        View dialogView = getLayoutInflater().inflate(R.layout.update_password, null);
        EditText oldPassword = dialogView.findViewById(R.id.old_password);
        EditText newPassword1 = dialogView.findViewById(R.id.new_password_1);
        EditText newPassword2 = dialogView.findViewById(R.id.new_password_2);
        //Cancel button:
        TextView cancelButton = dialogView.findViewById(R.id.cancel_update_password);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                dialogBuilder.dismiss();
            }
        });
        //Submit button:
        TextView submitButton = dialogView.findViewById(R.id.submit_update_password);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String SoldPassword = oldPassword.getText().toString();
                final String SnewPassword1 = newPassword1.getText().toString();
                final String SnewPassword2 = newPassword2.getText().toString();

                //Check if any fields are null/empty
                if(TextUtils.isEmpty(SoldPassword) || TextUtils.isEmpty(SnewPassword1) || TextUtils.isEmpty((SnewPassword2))){
                    Toast.makeText(getActivity(), "Fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!SoldPassword.equals(currentUser.mPassword)){//Check if old password matches currentUser's old password
                    Toast.makeText(getActivity(), "Incorrect old password", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!SnewPassword1.equals(SnewPassword2)){ //Check if the new passwords match
                    Toast.makeText(getActivity(), "New passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    String name = preferences.getString("name", "Joe");
                    //If it passes all of these checks, then we can update the password to the new password
                    mUserAccountViewModel.updatePassword(name, SnewPassword1);

                }
            }
        });

    }

    private void updateBudget() {
        //todo need a budget for the useraccounts first

    }

    private void deleteAccount(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String name = preferences.getString("name", "Joe");
        //If it passes all of these checks, then we can update the password to the new password
        mUserAccountViewModel.deleteAccount(currentUser);

    }
}
