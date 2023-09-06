package com.lokke.radio.endstation.data.network;


import com.lokke.radio.endstation.data.network.responses.Response;
import com.lokke.radio.endstation.data.network.responses.Radio;
import com.lokke.radio.endstation.ui.feedback.Feedback;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyApi {

    @POST("{path}")
    Call<Response> sendFeedback(@Path(value = "path", encoded = true) String path, @Body Feedback body);

    //reporting radio
    @GET("{path}")
    Call<Response> reportRadio(@Path(value = "path", encoded = true) String path);

    //radio information
    @GET("{path}")
    Call<Radio> getRadio(@Path(value = "path", encoded = true) String path);




}
