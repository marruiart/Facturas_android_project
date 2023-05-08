package com.example.facturas;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InvoicesRetrofitApiRequests {
    @GET(MyConstants.URL_PATH)
    Call<InvoicesApiResponse> getInvoices();
}
