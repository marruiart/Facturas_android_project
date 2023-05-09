package com.example.facturas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private FilterDataVO filter = null;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActivity();
    }

    private void initializeActivity() {
        // Set layout for the RecyclerView
        setLayoutManager();
        // Set filter button
        setFilterButtonListener();
        // Retrieve invoices list
        getInvoicesList();
        // Initialize invoice filter
        initializeFilter();
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

    private void setFilterButtonListener() {
        ImageButton filterButton = findViewById(R.id.btn_filter);
        if (filterButton != null) {
            filterButton.setOnClickListener(filterBtn -> openFilterFragment());
        }
    }

    private void getInvoicesList() {
        // Retrieve invoices from local database, if exists
        getInvoicesFromRoom();
        // Callback to get the list of invoices and initialize the RecyclerView adapter
        if (invoicesList == null || invoicesList.isEmpty()) {
            enqueueInvoices();
        }
    }

    private void getInvoicesFromRoom() {
        AppExecutors.ioThread(() -> {
            db = AppDatabase.getInstance(getApplicationContext());
            invoicesList = (ArrayList<InvoiceVO>) db.getInvoiceDao().getAllInvoices();
        });
    }

    public void enqueueInvoices() {
        Call<InvoicesApiResponse> call = InvoicesRetrofitApiService.getApiService().getInvoices();
        call.enqueue(new Callback<InvoicesApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<InvoicesApiResponse> call, Response<InvoicesApiResponse> response) {
                if (response.isSuccessful()) {
                    InvoicesApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        // Retrieve the invoice list from the api
                        invoicesList = (ArrayList<InvoiceVO>) apiResponse.getFacturas();
                        // Insert the data into Room database
                        insertDataInRoomDatabase();
                        // Set state as strings from app resources
                        setStringsForInvoiceDescEstado();
                        Log.d("onResponse invoices", "Size of 'facturas' list: " + invoicesList.size());
                        // Initialize RecyclerView adapter
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

    private void setStringsForInvoiceDescEstado() {
        for (InvoiceVO invoice : invoicesList) {
            invoice.setDescEstado(invoice.getDescEstado());
        }
    }

    private void insertDataInRoomDatabase() {
        AppExecutors.ioThread(() -> invoicesList.forEach(invoice -> db.getInvoiceDao().insertInvoice(invoice)));
    }

    private void initializeFilter() {
        filter = new FilterDataVO();
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

    public void openFilterFragment() {
        FilterFragment filterFragment = new FilterFragment();
        Bundle passedData = new Bundle();
        // Hide no invoices found message
        showNotFoundMessage(View.GONE);
        passedData.putParcelableArrayList("invoicesList", invoicesList);
        passedData.putParcelable("filter", filter);
        filterFragment.setArguments(passedData);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, filterFragment, FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onFilterApply(ArrayList<InvoiceVO> filteredInvoicesList, FilterDataVO filter) {
        onFilterClose();
        if (filteredInvoicesList.isEmpty()) {
            showNotFoundMessage(View.VISIBLE);
        }
        this.filter = filter;
        initializeRecyclerViewAdapter(filteredInvoicesList);
        Log.d("filterApplied", String.format("Original size: %d  Filtered size: %d", invoicesList.size(), filteredInvoicesList.size()));
    }

    @Override
    public void onFilterClose() {
        Fragment filterFragment = getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_TAG);
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
                .setPositiveButton("Cerrar", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}