package com.lokke.radio.endstation.util;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.databinding.DialogNoConnectivityBinding;
import com.lokke.radio.endstation.ui.SplashScreenActivity;


public class NoConnectivityDialog extends DialogFragment implements View.OnClickListener {

    private DialogNoConnectivityBinding binding;

    public static NoConnectivityDialog newInstance() {
        return new NoConnectivityDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_no_connectivity, null, false);
        binding.btnRetry.setOnClickListener(this);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(binding.getRoot())
                .setCancelable(false)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.btnRetry.getId()) {
            Intent refreshIntent = new Intent(getActivity(), SplashScreenActivity.class);
            startActivity(refreshIntent);
            getActivity().finish();
        }
    }


}
