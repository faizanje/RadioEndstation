package com.lokke.radio.endstation.data.network;


import com.lokke.radio.endstation.data.network.responses.Response;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DiscordApi {



    @POST("927495179311648789/2oJ7IYmVv14vv1E1fhLfggEI0g7G9Yqnih03UPkAD2BroqVDZYS-E3s6t3tPwqC3P8j1")
//    @POST("937112064281956362/hLAx2qHjWja-zYwX3SSOhZ14lTE4e-IVzqv-tera-c2P49lSkLS7ByOZ0eoItHaCNRMo")
    Call<Response> sendSongRequest(
            @Header("Content-Type") String content_type,
            @Body HashMap<String, Object> body);

}
