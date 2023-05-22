package com.example.facturas.data.network.retrofit;

import com.example.facturas.utils.MyConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoicesRetrofitApiService {
    private static InvoicesRetrofitApiRequests apiService;

    public static InvoicesRetrofitApiRequests getApiService() {
        // Create interceptor and indicate log level
        HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();
        loggin.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Associate interceptor to requests
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggin);

        if (apiService == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, localDateJsonDeserializer)
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MyConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
            apiService = retrofit.create(InvoicesRetrofitApiRequests.class);
        }
        return apiService;
    }

    public static JsonDeserializer<LocalDate> localDateJsonDeserializer = (jsonElem, type, context) -> {
        if (jsonElem == null) {
            return null;
        }
        String localDateStr = jsonElem.getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MyConstants.API_DATE_FORMAT);
        return LocalDate.parse(localDateStr, formatter);
    };
}
