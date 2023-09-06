package com.lokke.radio.endstation.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class ConnectivityInterceptor implements Interceptor {

    private Context mContext;

    public ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        if (!isOnline(mContext)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }


    public  boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        isOnMobileNetwork(context);

        boolean isConnected3G = false;
        isConnected3G = isOnMobileNetwork(context);

        boolean isConnectedWifi = false;

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {

            isConnectedWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        }

        return netInfo != null && (isConnected3G || isConnectedWifi);
    }

    public  boolean isOnMobileNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected3G = false;

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
            isConnected3G = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

            return isConnected3G;
        }
        return isConnected3G;
    }

}
