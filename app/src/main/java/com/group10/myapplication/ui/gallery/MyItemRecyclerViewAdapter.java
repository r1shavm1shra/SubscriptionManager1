package com.group10.myapplication.ui.gallery;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.group10.myapplication.R;
import com.group10.myapplication.data.SubscriptionViewModel;
import com.group10.myapplication.data.model.Subscription;
import com.group10.myapplication.databinding.FragmentItemBinding;
import com.group10.myapplication.ui.home.HomeFragment;

import org.w3c.dom.Text;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Subscription}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>{

    private List<Subscription> mValues;
    private GalleryFragment mFragment;
    public MyItemRecyclerViewAdapter(List<Subscription> items, GalleryFragment galleryFragment) {
        mValues = items;
        mFragment = galleryFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).mName);
        holder.mContentView.setText(mValues.get(position).mCost);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mIdView;
        public final TextView mContentView;
        public Subscription mItem;
        private ImageView mDeleteButton;
        private TextView mNameButton;
        private TextView mCostButton;
        private SubscriptionViewModel mSubscriptionViewModel;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.name;
            mContentView = binding.cost;
            View v = binding.getRoot();
            mDeleteButton = v.findViewById(R.id.delete);
            mNameButton = v.findViewById(R.id.name);
            mCostButton = v.findViewById(R.id.cost);
            mNameButton.setOnClickListener(this);
            mCostButton.setOnClickListener(this);
            mDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int viewId = view.getId();
            if (viewId == R.id.delete) {
                //removeAt(getLayoutPosition(), view);
                mFragment.delete(mValues.get(getLayoutPosition()));
            }
            else{
                mFragment.navigateToAdd(mValues.get(getLayoutPosition()).mUid);
            }
        }
        public void removeAt(int position, View v) {
            mValues.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mValues.size());

        }
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
    public void setData(List<Subscription> newData) {
        this.mValues = newData;
        notifyDataSetChanged();
    }
}