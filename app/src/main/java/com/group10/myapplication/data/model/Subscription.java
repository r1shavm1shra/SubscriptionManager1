package com.group10.myapplication.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Fts4
@Entity(tableName = "subscription")
public class Subscription {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    public int mUid;

    @NonNull
    @ColumnInfo(name = "name")
    public String mName;

    @NonNull
    @ColumnInfo(name = "cost")
    public String mCost;

    @NonNull
    @ColumnInfo(name = "duedate")
    public String mDueDate;

    public Subscription(@NonNull String name, @NonNull String cost, @NonNull String dueDate) {
        mName = name;
        mCost = cost;
        mDueDate = dueDate;
    }

    public String getName() {
        return mName;
    }

    public String getCost() {
        return mCost;
    }
    public String getDueDate() {
        return mDueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return mName.equals(that.mName) && mCost.equals(that.mCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUid, mName, mCost);
    }

    @NonNull
    @Override
    public String toString() {
        return "subscription{" +
                "uid=" + mUid +
                "; name='" + mName + '\'' +
                "; cost='" + mCost + '\'' +
                '}';
    }
}