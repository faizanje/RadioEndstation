package com.lokke.radio.endstation.data.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import com.lokke.radio.endstation.util.NoConnectivityDialog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafeApiRequest {

    public static final String NO_CONNECTIVITY_DIALOG = "no_connectivity_dialog";


    public static <T> MutableLiveData<T> callRetrofitObjectResponse(Context context, Call<T> call) {
        MutableLiveData<T> responseObject = new MutableLiveData<>();

        Log.e("inside", String.valueOf(call.request().url()));

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if(response.isSuccessful()){
                    Log.d("faizan", "onResponse: is success" + response.raw().toString());
                }else{
                    Log.d("faizan", "onResponse: is not success" );
                    try {
                        Log.d("faizan", "onResponse error :" + response.errorBody().string());
                    } catch (IOException e) {
                        Log.d("faizan", "onResponse error string exception: " + e.toString());
                    }
                }
                responseObject.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                responseObject.setValue(null);
                showDialog(context, t);
            }
        });

        return responseObject;
    }

    private static void showDialog(Context context, Throwable t) {
        if (t instanceof NoConnectivityException) {
            NoConnectivityDialog dialog = NoConnectivityDialog.newInstance();
            dialog.show(((FragmentActivity) context).getSupportFragmentManager(), NO_CONNECTIVITY_DIALOG);
        }
    }
}


