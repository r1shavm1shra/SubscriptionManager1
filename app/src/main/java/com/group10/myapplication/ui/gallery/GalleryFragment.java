package com.group10.myapplication.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group10.myapplication.R;
import com.group10.myapplication.data.SubscriptionViewModel;
import com.group10.myapplication.data.model.Subscription;
import com.group10.myapplication.data.model.UserAccount;
import com.group10.myapplication.databinding.FragmentGalleryBinding;
import com.group10.myapplication.placeholder.PlaceholderContent;
import com.group10.myapplication.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import timber.log.Timber;

public class GalleryFragment extends Fragment implements View.OnClickListener{

    private GalleryViewModel galleryViewModel;
    private final String TAG = getClass().getSimpleName();
    private SubscriptionViewModel mSubscriptionViewModel;
    private FragmentGalleryBinding binding;
    private MyItemRecyclerViewAdapter adapter;
    private View root;
    private ImageView mDeleteButton;
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static GalleryFragment newInstance(int columnCount) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.delete) {
            Timber.d(TAG, "The list of UserAccounts just changed; it has %s elements", viewId);
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        Activity activity = requireActivity();
        mSubscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        mSubscriptionViewModel.getAllSubscriptions().observe((LifecycleOwner)activity , new Observer<List<Subscription>>() {
            @Override
            public void onChanged(List<Subscription> subscriptionList) {
                adapter.setData(subscriptionList);
                Timber.d(TAG, "The list of UserAccounts just changed; it has %s elements", subscriptionList.size());
            }
        });
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        List<Subscription> subscriptionList = new ArrayList<Subscription>();
        subscriptionList.add(new Subscription("","",""));

        if (root instanceof RecyclerView) {
            Context context = root.getContext();
            RecyclerView recyclerView = (RecyclerView) root;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter =new MyItemRecyclerViewAdapter(subscriptionList, this);
            recyclerView.setAdapter(adapter);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void delete(Subscription subscription) {
        mSubscriptionViewModel.delete(subscription);
    }

}