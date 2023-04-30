package com.example.facturas;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<InvoiceVO> invoicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_invoices);
        setLayoutManager();
        enqueueInvoices();
    }

    private LinearLayoutManager setLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        return layoutManager;
    }

    private void initializeAdapter() {
        InvoicesRecyclerAdapter adapter = new InvoicesRecyclerAdapter(invoicesList);
        mRecyclerView.setAdapter(adapter);
    }

    public void enqueueInvoices() {
        Call<InvoicesApiResponse> call = InvoicesJsonApiAdapter.getApiService().getInvoices();
        call.enqueue(new Callback<InvoicesApiResponse>() {
            @Override
            public void onResponse(Call<InvoicesApiResponse> call, Response<InvoicesApiResponse> response) {
                if (response.isSuccessful()) {
                    List<InvoiceVO> invoices = response.body().getFacturas();
                    Log.d("onResponse invoices", "Size of 'facturas' list: " + invoices.size());
                    invoicesList = response.body().getFacturas();
                    initializeAdapter();
                }
            }

            @Override
            public void onFailure(Call<InvoicesApiResponse> call, Throwable t) {
                String error = t.getMessage();
                Log.d("onFailure error message", error);
            }
        });
    }
}