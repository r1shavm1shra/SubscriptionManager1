package com.group10.myapplication.ui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.group10.myapplication.StringUtils;
import com.group10.myapplication.data.UserAccountViewModel;
import com.group10.myapplication.data.model.UserAccount;
import com.group10.myapplication.databinding.FragmentSettingsBinding;
import com.group10.myapplication.ui.login.LoginActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;


public class SettingsFragment extends Fragment implements View.OnClickListener{

    private FragmentSettingsBinding binding;
    private UserAccountViewModel mUserAccountViewModel;
    private UserAccount currentUser;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtConfirm;
    private EditText mEtBudget;
    private AutoCompleteTextView mEtCurrency;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Activity activity = requireActivity();
        mUserAccountViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserAccountViewModel.class);
        // Here's a dummy observer object that indicates when the UserAccounts change in the database.
        getCurrentUser();


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        Activity activity = requireActivity();
        final Button updateBudgetButton = v.findViewById(R.id.update_password_button);
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
        mEtUsername = v.findViewById(R.id.username);
        mEtPassword = v.findViewById(R.id.password);
        mEtConfirm = v.findViewById(R.id.password_confirm);
        mEtBudget = v.findViewById(R.id.budget);
        mEtCurrency = (AutoCompleteTextView) v.findViewById(R.id.currency);
        Set<Currency> currencies =  Currency.getAvailableCurrencies();
        List<String> currencyList = new ArrayList<>();
        for(Currency c:currencies){
            currencyList.add(c.getDisplayName() +"-"+ c.getSymbol());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,android.R.layout.select_dialog_item, currencyList);
        mEtCurrency.setAdapter(adapter);

        return v;
    }

    private void getCurrentUser(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String name = preferences.getString("name", "Joe");
        Activity activity = requireActivity();
        mUserAccountViewModel.getCurrentUser(name).observe((LifecycleOwner)activity , new Observer<UserAccount>() {
            @Override
            public void onChanged(UserAccount UserAccount) {
                currentUser = UserAccount;
                mEtBudget.setText(currentUser.getBudget());
                mEtCurrency.setText(currentUser.getCurrency());
            }
        });
        //If it passes all of these checks, then we can update the password to the new password
    }

    @Override
    public void onClick(View v) {
        final Activity activity = requireActivity();
        final int viewId = v.getId();
        if (viewId == R.id.update_password_button) {
            updateAccount(v);
        } else if (viewId == R.id.delete_account_button) {
            deleteAccount();
        }

    }

    private void updateAccount(View v) {
        //Show screen to enter old password. Then enter new password twice
        //Cancel button:
        Activity activity = getActivity();
        final String oldpassword = mEtUsername.getText().toString();
        final String newpassword = mEtPassword.getText().toString();
        final String confirmpassword = mEtConfirm.getText().toString();
        final String budget = mEtBudget.getText().toString();
        final String currency = mEtCurrency.getText().toString();
        String sha256HashStr = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256HashBytes = digest.digest(oldpassword.getBytes(StandardCharsets.UTF_8));
            sha256HashStr = StringUtils.bytesToHex(sha256HashBytes);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String name = preferences.getString("name", "Joe");


            //Check if any fields are null/empty
               if((TextUtils.isEmpty(oldpassword) && TextUtils.isEmpty(newpassword) && TextUtils.isEmpty(confirmpassword)) && !(TextUtils.isEmpty(currency) || TextUtils.isEmpty(budget))){
                   UserAccount userAccount = new UserAccount(name,currentUser.mPassword,budget,currency);
                   //If it passes all of these checks, then we can update the password to the new password
                   mUserAccountViewModel.updateAccount(userAccount);
                   Toast.makeText(getActivity(), "Account Budget and Currency Updated", Toast.LENGTH_SHORT).show();

               }
               else if(TextUtils.isEmpty(oldpassword) || TextUtils.isEmpty(newpassword) || TextUtils.isEmpty(confirmpassword) || TextUtils.isEmpty(currency) || TextUtils.isEmpty(budget)){
                    Toast.makeText(getActivity(), "Fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!sha256HashStr.equals(currentUser.mPassword)){//Check if old password matches currentUser's old password
                    Toast.makeText(getActivity(), "Incorrect old password", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!newpassword.equals(confirmpassword)){ //Check if the new passwords match
                    Toast.makeText(getActivity(), "New passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    sha256HashBytes = digest.digest(newpassword.getBytes(StandardCharsets.UTF_8));
                    sha256HashStr = StringUtils.bytesToHex(sha256HashBytes);
                    UserAccount userAccount = new UserAccount(name,sha256HashStr,budget,currency);
                    //If it passes all of these checks, then we can update the password to the new password
                    mUserAccountViewModel.updateAccount(userAccount);
                    Toast.makeText(getActivity(), "Account Updated", Toast.LENGTH_SHORT).show();
                }
        }
        catch (NoSuchAlgorithmException e) {
            Toast.makeText(activity, "Error: No SHA-256 algorithm found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updateBudget() {
        //todo need a budget for the useraccounts first

    }

    private void deleteAccount(){
        Activity activity = getActivity();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String name = preferences.getString("name", "Joe");
        //If it passes all of these checks, then we can update the password to the new password
        mUserAccountViewModel.deleteAccount(currentUser);
        startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
