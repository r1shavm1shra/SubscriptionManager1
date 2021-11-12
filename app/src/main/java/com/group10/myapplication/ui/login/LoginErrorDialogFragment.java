package com.group10.myapplication.ui.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.group10.myapplication.R;

/**
 * DialogFragment that gives user login error.
 *

 */

public class LoginErrorDialogFragment extends DialogFragment {
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireActivity())
                .setTitle(getResources().getString(R.string.error))
                .setMessage(getResources().getString(R.string.login_error_text))
                .setPositiveButton(getResources().getString(R.string.ok_text),
                        (dialog, which) -> {
                        }).create();
    }
}
