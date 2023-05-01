package com.example.facturas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoicesJsonApiAdapter {
    private static InvoicesJsonApiService API_SERVICE;
    private static final String BASE_URL = "https://viewnextandroid.wiremockapi.cloud/";
    public List<InvoiceVO> invoicesList = new ArrayList<>();

    public static InvoicesJsonApiService getApiService() {
        // Create interceptor and indicate log level
        HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();
        loggin.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Associate interceptor to requests
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggin);

        if (API_SERVICE == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("dd/MM/yyyy")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
            API_SERVICE = retrofit.create(InvoicesJsonApiService.class);
        }
        return API_SERVICE;
    }
}
