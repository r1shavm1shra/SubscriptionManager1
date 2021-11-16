package com.group10.myapplication.ui.add;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.group10.myapplication.R;
import com.group10.myapplication.StringUtils;
import com.group10.myapplication.data.SubscriptionViewModel;
import com.group10.myapplication.data.model.Subscription;
import com.group10.myapplication.databinding.FragmentAddBinding;
import com.group10.myapplication.ui.gallery.GalleryFragment;
import com.group10.myapplication.ui.login.AccountErrorDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import timber.log.Timber;

public class AddFragment extends Fragment implements View.OnClickListener {
    private EditText mEtSubscriptionName;
    private EditText mEtCost;
    private EditText mEtNextDueDate;
    private int uid;
    private FragmentAddBinding binding;
    DatePickerDialog.OnDateSetListener date;

    private SubscriptionViewModel mSubscriptionViewModel;

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
        mSubscriptionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(SubscriptionViewModel.class);
        mSubscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        Subscription subscription = new Subscription("", "", "");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        uid = preferences.getInt("uid", -1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("uid");
        editor.apply();
        if (uid != -1) {
            mSubscriptionViewModel.getSubscription(uid).observe((LifecycleOwner) activity, new Observer<Subscription>() {
                @Override
                public void onChanged(Subscription subscription) {
                    mEtSubscriptionName.setText(subscription.mName);
                    mEtCost.setText(subscription.mCost);
                    mEtNextDueDate.setText(subscription.mDueDate);
                }
            });
        }
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
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_navigation);
            navController.navigate(R.id.nav_gallery);

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
        final String cost = mEtCost.getText().toString();
        final String dueDate = mEtNextDueDate.getText().toString();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(cost) && !TextUtils.isEmpty(dueDate)) {

                if(uid!=-1){
                    Subscription Subscription = new Subscription(username, cost, dueDate);
                    mSubscriptionViewModel.update(uid,Subscription);
                    Toast.makeText(activity.getApplicationContext(), "New Subscription added", Toast.LENGTH_SHORT).show();
                }
                else {
                    // New way: create new Subscription, then add it to ViewModel
                    Subscription Subscription = new Subscription(username, cost, dueDate);
                    mSubscriptionViewModel.insert(Subscription);
                    Toast.makeText(activity.getApplicationContext(), "New Subscription added", Toast.LENGTH_SHORT).show();
                }

        } else if ((username.equals("")) || (cost.equals("")) || (dueDate.equals(""))) {
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

        setOnFocusChangeListener(mEtSubscriptionName,"Enter Subscription Name");
        setOnFocusChangeListener(mEtCost,"Enter Cost");
        setOnFocusChangeListener(mEtNextDueDate,"mm/dd/yy");

        AutoCompleteTextView dropdown = v.findViewById(R.id.every);
        String[] items = new String[]{"month", "year"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setText("month");

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
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

        mEtNextDueDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setOnFocusChangeListener(EditText editText, String name){
        if(editText.getId() == R.id.next_due_date){
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        FragmentActivity activity = requireActivity();
                        new DatePickerDialog(activity, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    } else {
                        editText.setHint("");
                    }
                }
            });

        }
        else {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        editText.setHint(name);
                    } else {
                        editText.setHint("");
                    }
                }
            });
        }
    }
}