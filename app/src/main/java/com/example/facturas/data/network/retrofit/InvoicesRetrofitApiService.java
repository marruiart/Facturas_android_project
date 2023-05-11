package com.example.facturas.data.network.retrofit;

import com.example.facturas.data.model.InvoiceVO;
import com.example.facturas.utils.MyConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoicesRetrofitApiService {
    private static InvoicesRetrofitApiRequests apiService;
    public List<InvoiceVO> invoicesList = new ArrayList<>();

    public static InvoicesRetrofitApiRequests getApiService() {
        // Create interceptor and indicate log level
        HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();
        loggin.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Associate interceptor to requests
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggin);

        if (apiService == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat(MyConstants.API_DATE_FORMAT)
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
}
