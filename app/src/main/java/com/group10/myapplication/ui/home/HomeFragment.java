package com.group10.myapplication.ui.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.PreferenceManager;

import com.group10.myapplication.R;
import com.group10.myapplication.data.SubscriptionViewModel;
import com.group10.myapplication.data.UserAccountViewModel;
import com.group10.myapplication.data.model.Subscription;
import com.group10.myapplication.data.model.UserAccount;
import com.group10.myapplication.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SubscriptionViewModel mSubscriptionViewModel;
    private int subscriptionCount = 0;
    private float totalSpend = 0;
    private float remainingBudget = -1;
    private float currentBudget = -1;
    private String currency = "";
    private TextView mTotalSpend;
    private TextView mSubscriptionCount;
    private TextView mCurrentBudget;
    private TextView mRemainingBudget;
    private TextView mAverageSpend;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Activity activity = getActivity();
        mSubscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            String username = preferences.getString("name", "");
            mSubscriptionViewModel.getAllSubscriptions(username).observe((LifecycleOwner) activity, new Observer<List<Subscription>>() {

                @Override
                public void onChanged(List<Subscription> subscriptionList) {
                    int subscriptionCount = subscriptionList.size();
                    for (Subscription subscription : subscriptionList) {
                        totalSpend += Float.valueOf(subscription.getCost().replace("$", "").replace(" ", ""));


                    }
                    remainingBudget = currentBudget - totalSpend;
                    mCurrentBudget.setText(mCurrentBudget.getText().toString().replace("{currentBudget}",String.format("%.2f",currentBudget)).replace("$",currency));
                    mRemainingBudget.setText(mRemainingBudget.getText().toString().replace("{remainingBudget}",String.format("%.2f",remainingBudget)).replace("$",currency));
                    mTotalSpend.setText(mTotalSpend.getText().toString().replace("{totalSpend}", String.format("%.2f",totalSpend)).replace("$",currency));
                    mSubscriptionCount.setText(mSubscriptionCount.getText().toString().replace("{subscriptionCount}", String.valueOf(subscriptionCount)));
                    mAverageSpend.setText(mAverageSpend.getText().toString().replace("{averageSpend}", String.format("%.2f", totalSpend/12)).replace("$",currency));
                }

            });

        String budget = preferences.getString("budget", "0.00");
        currentBudget = Float.valueOf(budget);
        currency = preferences.getString("currency", "$");
        }catch (Exception e){
            e.toString();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mTotalSpend = root.findViewById(R.id.home_spend);
        mRemainingBudget = root.findViewById(R.id.home_rem_budget);
        mSubscriptionCount = root.findViewById(R.id.home_total_app);
        mCurrentBudget = root.findViewById(R.id.home_budget);
        mAverageSpend = root.findViewById(R.id.home_average_spend);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}