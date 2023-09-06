package com.lokke.radio.endstation.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.lokke.radio.endstation.data.network.DiscordApi;
import com.lokke.radio.endstation.data.network.MyApi;
import com.lokke.radio.endstation.data.network.SafeApiRequest;
import com.lokke.radio.endstation.data.network.SetupDiscordRetrofit;
import com.lokke.radio.endstation.data.network.SetupRetrofit;
import com.lokke.radio.endstation.data.network.responses.Radio;
import com.lokke.radio.endstation.data.network.responses.Response;
import com.lokke.radio.endstation.ui.feedback.Feedback;
import com.lokke.radio.endstation.ui.songrequest.SongRequest;

public class MainActivityRepository extends SafeApiRequest {

    private MyApi api;
    private DiscordApi discordApi;
    Context context;

    public MainActivityRepository(Context context) {
        this.context = context;
        SetupRetrofit setupRetrofit = new SetupRetrofit(context);
        SetupDiscordRetrofit setupDiscordRetrofit = new SetupDiscordRetrofit(context);
        this.api = SetupRetrofit.createService(MyApi.class);
        this.discordApi = SetupDiscordRetrofit.createService(DiscordApi.class);
    }

    /**
     * increase view for individual radio station.
     */
    public MutableLiveData<Response> reportRadio() {
        return callRetrofitObjectResponse(context, api.reportRadio("streaming/issue/report/store"));
    }

    /**
     * send feedback.
     */
    public MutableLiveData<Response> sendFeedback(Feedback feedback) {
        return callRetrofitObjectResponse(context, api.sendFeedback("feedback/store",feedback));
    }

    /**
     * send song request.
     */
    public MutableLiveData<Response> sendSongRequest(SongRequest songRequest) {
        Log.d("faizan", "sendSongRequest: " + songRequest.getContent());
//        return callRetrofitObjectResponse(context, api.sendFeedback("feedback/store",new Feedback()));
        return callRetrofitObjectResponse(context, this.discordApi.sendSongRequest("application/json",songRequest.body()));
    }

    /**
     * get radio information
     */
    public MutableLiveData<Radio> getRadio() {
        return callRetrofitObjectResponse(context, api.getRadio("radio"));
    }
}
