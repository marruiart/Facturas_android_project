package com.example.facturas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements FilterFragment.OnDataPassListener {
    private static final String FRAGMENT_TAG = "FILTER_FRAGMENT";
    private ArrayList<InvoiceVO> invoicesList = new ArrayList<>();
    private FilterDataVO filter = null;
    private InvoicesRecyclerAdapter adapter;

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

    // Invoices list data management
    private void getInvoicesList() {
        // Retrieve invoices from local database, if exists
        getInvoicesFromRoom();
        // Callback to get the list of invoices from Retrofit Api
        getInvoicesFromApi();
    }

    private void setStringsForInvoiceDescEstado() {
        invoicesList.forEach(invoice -> invoice.setDescEstado(invoice.getDescEstado()));
    }

    private void getInvoicesFromRoom() {
        AppExecutors.ioThread(() -> {
            invoicesList = (ArrayList<InvoiceVO>) App.getDatabase().getInvoiceDao().getAllInvoices();
            Log.d("Fill/update invoicesList", "Room -> Size of 'facturas' list: " + invoicesList.size());
            // Initialize or update RecyclerView
            (MainActivity.this).runOnUiThread(this::printInvoicesList);
        });
    }

    public void getInvoicesFromApi() {
        Call<InvoicesApiResponse> call = InvoicesRetrofitApiService.getApiService().getInvoices();
        call.enqueue(new Callback<InvoicesApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<InvoicesApiResponse> call, Response<InvoicesApiResponse> response) {
                if (response.isSuccessful()) {
                    int listSize = invoicesList.size();
                    InvoicesApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        // Retrieve the invoice list from the api
                        invoicesList = (ArrayList<InvoiceVO>) apiResponse.getFacturas();
                        Log.d("Fill/update invoicesList", "Api -> Size of 'facturas' list: " + invoicesList.size());
                        // Set state as strings from app resources
                        setStringsForInvoiceDescEstado();
                        // Insert the data into Room database
                        insertDataInRoomDatabase();
                        if (listSize != invoicesList.size()) {
                            // If changed, print invoices in RecyclerView
                            printInvoicesList();
                        }
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

    private void insertDataInRoomDatabase() {
        AppExecutors.ioThread(() -> {
            App.getDatabase().getInvoiceDao().deleteAll();
            App.getDatabase().getInvoiceDao().insertInvoices(invoicesList.toArray(new InvoiceVO[invoicesList.size()]));
        });
    }

    // RecyclerView methods
    private void printInvoicesList() {
        if (adapter != null) {
            updateRecyclerViewAdapter(invoicesList);
            setClickListenersOnItems();
            Toast.makeText(MainActivity.this, R.string.activity_main_toast_list_updated, Toast.LENGTH_SHORT).show();
        } else {
            initializeRecyclerViewAdapter();
            setClickListenersOnItems();
            setAdapterToRecyclerView();
        }
    }

    private void initializeRecyclerViewAdapter() {
        initializeRecyclerViewAdapter(invoicesList);
    }

    private void initializeRecyclerViewAdapter(ArrayList<InvoiceVO> invoicesList) {
        adapter = new InvoicesRecyclerAdapter(invoicesList);
    }

    private void updateRecyclerViewAdapter(ArrayList<InvoiceVO> newInvoiceList) {
        adapter.setInvoices(newInvoiceList);
        adapter.notifyDataSetChanged();
    }

    private void setClickListenersOnItems() {
        // Set listener for click events on items in the RecyclerView
        adapter.setOnItemClickListener(item -> showAlertDialog());
    }

    private void setAdapterToRecyclerView() {
        // Find RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView_invoices);
        if (mRecyclerView != null) {
            // Set adapter to it
            mRecyclerView.setAdapter(adapter);
        }
    }

    // Methods to show info
    private void showNotFoundMessage(int visibility) {
        TextView notFound = findViewById(R.id.invoice_not_found);
        notFound.setVisibility(visibility);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_dialog_title_info)
                .setMessage(R.string.alert_dialog_message_non_available)
                .setPositiveButton(R.string.alert_dialog_btn_close, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Filter methods
    private void initializeFilter() {
        filter = new FilterDataVO();
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
        updateRecyclerViewAdapter(filteredInvoicesList);
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
}