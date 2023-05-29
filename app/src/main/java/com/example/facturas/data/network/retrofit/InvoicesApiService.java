package com.example.facturas.data.network.retrofit;

import com.example.facturas.data.model.InvoicesApiResponse;
import com.example.facturas.utils.MyConstants;

import co.infinum.retromock.meta.Mock;
import co.infinum.retromock.meta.MockResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface InvoicesApiService {
    @Mock
    @MockResponse(body = "invoices")
    @GET(MyConstants.URL_PATH)
    Call<InvoicesApiResponse> getInvoices();
}
