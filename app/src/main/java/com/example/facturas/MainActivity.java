package com.example.facturas;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnDataPassListener, InvoicesRecyclerAdapter.RecyclerOnClickListener {
    private static final String FRAGMENT_TAG = "FILTER_FRAGMENT";
    private ArrayList<InvoiceVO> invoicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inicialize activity
        init();
    }

    private void init() {
        // Set layout for the RecyclerView
        setLayoutManager();
        // Callback to get the list of invoices and initialize the RecyclerView
        enqueueInvoices();
    }

    private void setLayoutManager() {
        RecyclerView mRecyclerView;
        LinearLayoutManager layoutManager;

        mRecyclerView = findViewById(R.id.recyclerView_invoices);
        if (mRecyclerView != null) {
            layoutManager = new LinearLayoutManager(MainActivity.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
        }
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
                        initializeRecyclerViewAdapter();
                    }
                }
            }

            @Override
            public void onFailure(Call<InvoicesApiResponse> call, Throwable t) {
                String error = t.getLocalizedMessage();
                Log.d("onFailure error message", error);
            }
        });
    }

    private void initializeRecyclerViewAdapter() {
        initializeRecyclerViewAdapter(invoicesList);
    }

    private void initializeRecyclerViewAdapter(ArrayList<InvoiceVO> invoicesList) {
        RecyclerView mRecyclerView;
        InvoicesRecyclerAdapter adapter;

        adapter = new InvoicesRecyclerAdapter(invoicesList);
        adapter.setOnItemClickListener(this);
        mRecyclerView = findViewById(R.id.recyclerView_invoices);
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void showNotFoundMessage(int visibility) {
        TextView notFound = findViewById(R.id.invoice_not_found);
        notFound.setVisibility(visibility);
    }

    public void openFilterFragment(View v) {
        FilterFragment filterFragment = new FilterFragment();
        Bundle passedData = new Bundle();
        // Hide no invoices found message
        showNotFoundMessage(View.GONE);
        passedData.putParcelableArrayList("invoicesList", invoicesList);
        filterFragment.setArguments(passedData);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, filterFragment, FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onFilterApply(ArrayList<InvoiceVO> filteredInvoicesList) {
        onFilterClose();
        if (filteredInvoicesList.isEmpty()) {
            showNotFoundMessage(View.VISIBLE);
        }
        initializeRecyclerViewAdapter(filteredInvoicesList);
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

    @Override
    public void onItemClick(int position) {
        // OnClick event for the RecyclerView
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información")
                .setMessage("Esta funcionalidad aún no está disponible")
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}