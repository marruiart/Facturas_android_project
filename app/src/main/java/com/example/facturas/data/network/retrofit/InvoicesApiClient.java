package com.example.facturas.data.network.retrofit;

import com.example.facturas.utils.MyConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.infinum.retromock.Retromock;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoicesApiClient {
    private static InvoicesApiService retrofitService;
    private static InvoicesApiService retromockService;

    private InvoicesApiClient() {
        // Private constructor to hide the implicit public one
    }

    public static InvoicesApiService getRetromockService() {
        if (retromockService == null) {
            retromockService = new Retromock.Builder()
                    .retrofit(initRetrofit())
                    .defaultBodyFactory(new ResourceBodyFactory())
                    .build()
                    .create(InvoicesApiService.class);
        }
        return retromockService;
    }

    public static InvoicesApiService getRetrofitService() {
        if (retrofitService == null) {
            retrofitService = initRetrofit()
                    .create(InvoicesApiService.class);
        }
        return retrofitService;
    }

    private static OkHttpClient.Builder initHttpClient() {
        // Create interceptor and indicate log level
        HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();
        loggin.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Associate interceptor to requests
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        return httpClient.addInterceptor(loggin);
    }

    private static Retrofit initRetrofit() {
        OkHttpClient.Builder httpClient = initHttpClient();
        Gson gson = createGson();
        return new Retrofit.Builder()
                .baseUrl(MyConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateJsonDeserializer)
                .create();
    }

    private static final JsonDeserializer<LocalDate> localDateJsonDeserializer = (jsonElem, type, context) -> {
        if (jsonElem == null) {
            return null;
        }
        String localDateStr = jsonElem.getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MyConstants.API_DATE_FORMAT);
        return LocalDate.parse(localDateStr, formatter);
    };
}
