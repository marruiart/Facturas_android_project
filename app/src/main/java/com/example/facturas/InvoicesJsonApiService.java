package com.example.facturas;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InvoicesJsonApiService {
    @GET("facturas")
    Call<InvoicesApiResponse> getInvoices();
}
