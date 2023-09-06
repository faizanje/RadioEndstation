package com.lokke.radio.endstation.ui.player;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class TimerDialog implements DialogInterface.OnClickListener {

    Context context;
    TimerListener timerListener;

    public TimerDialog(@NonNull Context context, TimerListener timerListener) {
        this.context = context;
        this.timerListener = timerListener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sleep Timer");
        String[] durations = {"None", "5 minutes", "10 minutes", "20 minutes", "30 minutes", "45 minutes", "1 hour", "2 hour", "3 hour"};
        builder.setItems(durations, this);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:
                timerListener.onTimerSelected(0, "none"); //none
                break;
            case 1:
                timerListener.onTimerSelected(5 * 60 * 1000, "5 min"); //5 minutes
                break;
            case 2:
                timerListener.onTimerSelected(10 * 60 * 1000, "10 min"); //10 minute
                break;
            case 3:
                timerListener.onTimerSelected(20 * 60 * 1000, "20 min"); //20 minute
                break;
            case 4:
                timerListener.onTimerSelected(30 * 60 * 1000, "30 min"); //30 minute
                break;
            case 5:
                timerListener.onTimerSelected(45 * 60 * 1000, "45 min"); //45 minute
                break;
            case 6:
                timerListener.onTimerSelected(60 * 60 * 1000, "1 hour"); //1 hour
                break;
            case 7:
                timerListener.onTimerSelected(120 * 60 * 1000, "2 hour"); //2 hour
                break;
            case 8:
                timerListener.onTimerSelected(180 * 60 * 1000, "3 hour"); //3 hour
                break;

        }
    }
}
