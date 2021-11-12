package com.group10.myapplication.ui.add;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.group10.myapplication.R;
import com.group10.myapplication.StringUtils;
import com.group10.myapplication.data.UserAccountViewModel;
import com.group10.myapplication.data.model.UserAccount;
import com.group10.myapplication.databinding.FragmentAddBinding;
import com.group10.myapplication.ui.login.AccountErrorDialogFragment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import timber.log.Timber;

public class AddFragment extends Fragment implements View.OnClickListener {
    private EditText mEtSubscriptionName;
    private EditText mEtCost;
    private EditText mEtNextDueDate;
    private AddViewModel addViewModel;
    private FragmentAddBinding binding;
    DatePickerDialog.OnDateSetListener date;

    private UserAccountViewModel mUserAccountViewModel;

    private final String TAG = getClass().getSimpleName();
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = requireActivity();
        mUserAccountViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserAccountViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setSubtitle(getResources().getString(R.string.account));
            }
        }
        catch (NullPointerException npe) {
            Timber.e("Could not set subtitle");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.done_button) {
            createAccount();
        } else if (viewId == R.id.cancel_button) {
            mEtSubscriptionName.setText("");
            mEtCost.setText("");
            mEtNextDueDate.setText("");
        } else if (viewId == R.id.go_to_applications) {
            FragmentActivity activity = requireActivity();
            activity.getSupportFragmentManager().popBackStack();
        } else if(viewId == R.id.next_due_date)
        {
            FragmentActivity activity = requireActivity();
            new DatePickerDialog(activity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
        else {
            Timber.e("Invalid button click");
        }
    }

    private void createAccount() {
        FragmentActivity activity = requireActivity();
        final String username = mEtSubscriptionName.getText().toString();
        final String password = mEtCost.getText().toString();
        final String confirm = mEtNextDueDate.getText().toString();
        if (password.equals(confirm) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {

            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] sha256HashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                String sha256HashStr = StringUtils.bytesToHex(sha256HashBytes);

                // New way: create new UserAccount, then add it to ViewModel
                UserAccount userAccount = new UserAccount(username, sha256HashStr);
                mUserAccountViewModel.insert(userAccount);
                Toast.makeText(activity.getApplicationContext(), "New UserAccount added", Toast.LENGTH_SHORT).show();

            } catch (NoSuchAlgorithmException e) {
                Toast.makeText(activity, "Error: No SHA-256 algorithm found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if ((username.equals("")) || (password.equals("")) || (confirm.equals(""))) {
            Toast.makeText(activity.getApplicationContext(), "Missing entry", Toast.LENGTH_SHORT).show();
        } else {
            Timber.e("An unknown account creation error occurred.");
            FragmentManager manager = getParentFragmentManager();
            AccountErrorDialogFragment fragment = new AccountErrorDialogFragment();
            fragment.show(manager, "account_error");
        }
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel =
                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        Activity activity = requireActivity();
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        mEtSubscriptionName = v.findViewById(R.id.subscription_name);
        mEtCost = v.findViewById(R.id.subscription_cost);
        mEtNextDueDate = v.findViewById(R.id.next_due_date);
        Button btnAdd = v.findViewById(R.id.done_button);
        btnAdd.setOnClickListener(this);
        Button btnCancel = v.findViewById(R.id.cancel_button);
        btnCancel.setOnClickListener(this);
        mEtNextDueDate.setOnClickListener(this);
        Button btnExit = v.findViewById(R.id.go_to_applications);
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            btnExit.setOnClickListener(this);
        }
        else {
            btnExit.setVisibility(View.GONE);
            btnExit.invalidate();
        }



        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        return v;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEtNextDueDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}