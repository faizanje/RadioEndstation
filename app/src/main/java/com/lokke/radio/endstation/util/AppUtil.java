package com.lokke.radio.endstation.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.databinding.DialogExitBinding;
import com.lokke.radio.endstation.databinding.DialogReportBinding;

public class AppUtil {

    //dialog for exiting application.
    public static void showExitDialog(Context context, boolean isPlaying, AlertDialogListener dialogListener) {
        String message = "";
        String negativeText = "";
        if (isPlaying) {
            message = context.getResources().getString(R.string.exit_dialog_message);
            negativeText = context.getResources().getString(R.string.exit_negative_button);
        } else {
            message = context.getResources().getString(R.string.exit_dialog_message2);
            negativeText = context.getResources().getString(R.string.exit_negative_button2);
        }


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        DialogExitBinding binding = DataBindingUtil.inflate(LayoutInflater.from(alertDialog.getContext()), R.layout.dialog_exit, null, false);
        alertDialog.setView(binding.getRoot(), 30, 20, 30, 20);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.exitDialogMessage.setText(message);
        binding.btnMinimize.setText(negativeText);

        binding.btnMinimize.setOnClickListener(view -> {
            alertDialog.dismiss();
            if (isPlaying) dialogListener.onCancel();
        });

        binding.btnExit.setOnClickListener(view -> {
            alertDialog.dismiss();
            dialogListener.onPositive();
        });

        alertDialog.show();
    }

    //dialog for reporting radio station.
    public static void showReportDialog(Context context, AlertDialogListener dialogListener) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        DialogReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(alertDialog.getContext()), R.layout.dialog_report, null, false);
        alertDialog.setView(binding.getRoot(), 30, 20, 30, 20);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.btnReport.setOnClickListener(view -> {
            alertDialog.dismiss();
            dialogListener.onPositive();
        });

        binding.btnCancel.setOnClickListener(view -> alertDialog.dismiss());

        alertDialog.show();
    }

    public static void loadWebView(Context context, String path) {

        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(context);
        WebView wv = new WebView(context);
        wv.loadUrl(path);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Dismiss", (dialog, id) -> dialog.dismiss());

        alert.show();
    }


    public interface AlertDialogListener {
        void onPositive();

        void onCancel();

    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}
