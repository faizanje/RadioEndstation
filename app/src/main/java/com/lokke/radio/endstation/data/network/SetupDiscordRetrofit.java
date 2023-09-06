package com.lokke.radio.endstation.data.network;

import android.content.Context;

import com.lokke.radio.endstation.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SetupDiscordRetrofit {

    OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    public SetupDiscordRetrofit(Context context) {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new ConnectivityInterceptor(context))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.discord_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
