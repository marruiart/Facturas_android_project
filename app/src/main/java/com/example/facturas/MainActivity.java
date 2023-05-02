package com.example.facturas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnDataPassListener {
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
        Bundle passedData = new Bundle();
        passedData.putParcelableArrayList("invoicesList", invoicesList);
        filterFragment.setArguments(passedData);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, filterFragment, FRAGMENT_TAG)
                .commit();
    }

    private void setLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initializeAdapter(ArrayList<InvoiceVO> filteredInvoicesList) {
        InvoicesRecyclerAdapter adapter = new InvoicesRecyclerAdapter(filteredInvoicesList);
        mRecyclerView.setAdapter(adapter);
    }

    private void initializeAdapter() {
        initializeAdapter(invoicesList);
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
                        // Set state as strings from app resources
                        for (InvoiceVO invoice : invoicesList) {
                            invoice.setDescEstado(invoice.getDescEstado());
                        }
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

    @Override
    public void onFilterApply(ArrayList<InvoiceVO> filteredInvoicesList) {
        onFilterClose();
        initializeAdapter(filteredInvoicesList);
        Log.d("filterApplied", String.format("Original size: %d  Filtered size: %d", invoicesList.size(), filteredInvoicesList.size()));
    }

    @Override
    public void onFilterClose() {
        Fragment filterFragment = getSupportFragmentManager()
                .findFragmentByTag("FILTER_FRAGMENT");
        if (filterFragment != null) {
            getSupportFragmentManager().
                    beginTransaction().
                    remove(filterFragment).
                    commit();
        }
    }
}