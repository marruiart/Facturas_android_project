package com.example.facturas.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturas.App;
import com.example.facturas.data.database.entity.InvoiceEntity;
import com.example.facturas.ui.fragments.FilterFragment;
import com.example.facturas.ui.adapters.InvoicesListAdapter;
import com.example.facturas.R;
import com.example.facturas.data.model.FilterDataVO;
import com.example.facturas.data.model.InvoiceVO;
import com.example.facturas.viewmodel.InvoicesListViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnDataPassListener {
    private static final String FRAGMENT_TAG = "FILTER_FRAGMENT";
    private ArrayList<InvoiceVO> invoicesList = new ArrayList<>();
    private FilterDataVO filter = null;
    private InvoicesListAdapter adapter;
    private InvoicesListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.setCurrentActivity(this);
    }

    private void initializeActivity() {
        App.setCurrentActivity(this);
        // Set layout for the RecyclerView
        setLayoutManager();
        // Set toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        // Retrieve existing ViewModel (or create one if non-existent)
        viewModel = new ViewModelProvider(this).get(InvoicesListViewModel.class);
        // Subscribe to data changes
        setLiveDataSubscription(viewModel);
        // Initialize invoice filter
        initializeFilter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setLayoutManager() {
        RecyclerView mRecyclerView;
        LinearLayoutManager layoutManager;

        mRecyclerView = findViewById(R.id.recyclerView_invoices);
        if (mRecyclerView != null) {
            layoutManager = new LinearLayoutManager(MainActivity.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
        }
    }

    /**
     * Invoices list data management
     */
    private void setLiveDataSubscription(InvoicesListViewModel viewModel) {
        // Subscribe to data changes
        viewModel.getAllInvoicesFromViewModel().observe(MainActivity.this, invoiceEntities -> {
            // Action onChanged callback method
            if (invoiceEntities != null) {
                invoicesList = InvoiceEntity.fromInvoiceEntityList(invoiceEntities);
                // Init or update RecyclerView
                printInvoicesList();
            }
        });
    }

    // RecyclerView methods
    private void printInvoicesList() {
        if (adapter != null) {
            ToggleButton toggleButton = findViewById(R.id.toggle_button);
            updateRecyclerViewAdapter(invoicesList);
            if (toggleButton.isChecked()) {
                Toast.makeText(MainActivity.this, R.string.activity_main_toast_list_updated_from_retromock, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.activity_main_toast_list_updated_from_retrofit, Toast.LENGTH_SHORT).show();
            }
            setClickListenersOnItems();
        } else {
            initializeRecyclerViewAdapter();
            setClickListenersOnItems();
            setAdapterToRecyclerView();
        }
    }

    private void initializeRecyclerViewAdapter() {
        initializeRecyclerViewAdapter(invoicesList);
        setAdapterToRecyclerView();
    }

    private void initializeRecyclerViewAdapter(ArrayList<InvoiceVO> invoicesList) {
        adapter = new InvoicesListAdapter(invoicesList);
    }

    private void updateRecyclerViewAdapter(ArrayList<InvoiceVO> newInvoiceList) {
        adapter.setInvoices(newInvoiceList);
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
        builder.setTitle(R.string.alert_dialog_title_info).setMessage(R.string.alert_dialog_message_non_available).setPositiveButton(R.string.alert_dialog_btn_close, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Filter methods
    private void initializeFilter() {
        filter = new FilterDataVO();
    }

    public MenuItem openFilterFragment(MenuItem menuItem) {
        FilterFragment filterFragment = new FilterFragment();
        Bundle passedData = new Bundle();
        // Hide no invoices found message
        showNotFoundMessage(View.GONE);
        passedData.putParcelableArrayList("invoicesList", invoicesList);
        passedData.putParcelable("filter", filter);
        filterFragment.setArguments(passedData);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, filterFragment, FRAGMENT_TAG).commit();
        return menuItem;
    }

    @Override
    public void onFilterApply(ArrayList<InvoiceVO> filteredInvoicesList, FilterDataVO filter) {
        closeFilter(null);
        if (filteredInvoicesList.isEmpty()) {
            showNotFoundMessage(View.VISIBLE);
        }
        this.filter = filter;
        updateRecyclerViewAdapter(filteredInvoicesList);
        Log.d("filterApplied", String.format("Original size: %d  Filtered size: %d", invoicesList.size(), filteredInvoicesList.size()));
    }

    public MenuItem closeFilter(MenuItem menuItem) {
        Fragment filterFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (filterFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(filterFragment).commit();
        }
        return menuItem;
    }

    public void refreshInvoicesFromRetromock(View view) {
        ToggleButton toggleButton = view.findViewById(R.id.toggle_button);
        viewModel.refreshAllInvoices(toggleButton.isChecked());
    }
}