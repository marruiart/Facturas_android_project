package com.example.facturas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG = "FILTER_FRAGMENT";
    private RecyclerView mRecyclerView;
    private ArrayList<InvoiceVO> invoicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView_invoices);
        setLayoutManager();
        enqueueInvoices();
    }

    public void openFilterFragment(View v) {
        FilterFragment filterFragment = new FilterFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, filterFragment, FRAGMENT_TAG)
                .commit();
    }

    public void closeFilterFragment(View v) {
        Fragment filterFragment = getSupportFragmentManager()
                .findFragmentByTag("FILTER_FRAGMENT");
        if (filterFragment != null) {
            getSupportFragmentManager().
                    beginTransaction().
                    remove(filterFragment).
                    commit();
        }
    }

    private void setLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
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
                    InvoicesApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        invoicesList = apiResponse.getFacturas();
                        Log.d("onResponse invoices", "Size of 'facturas' list: " + invoicesList.size());
                        initializeAdapter();
                    }
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