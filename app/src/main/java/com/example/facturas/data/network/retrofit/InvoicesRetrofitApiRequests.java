package com.example.facturas.data.network.retrofit;

import com.example.facturas.data.model.InvoicesApiResponse;
import com.example.facturas.utils.MyConstants;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InvoicesRetrofitApiRequests {
    @GET(MyConstants.URL_PATH)
    Call<InvoicesApiResponse> getInvoices();
}
