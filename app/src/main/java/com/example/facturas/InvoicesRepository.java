package com.example.facturas;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.facturas.data.database.InvoicesDatabase;
import com.example.facturas.data.database.entity.InvoiceEntity;
import com.example.facturas.data.model.InvoiceVO;
import com.example.facturas.data.model.InvoicesApiResponse;
import com.example.facturas.data.network.retrofit.InvoicesRetrofitApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InvoicesRepository {

    private static InvoicesRepository instance;

    private final InvoicesDatabase database;
    private MediatorLiveData<List<InvoiceEntity>> observableInvoices;

    private InvoicesRepository() {
        database = App.getDatabase();
        /* To ensure that the UI uses the list of products only after the database has been
          pre-populated, a MediatorLiveData object is used. This observes the changes of the
          list of products and only forwards it when the database is ready to be used. */
        observableInvoices = new MediatorLiveData<>();
        observableInvoices.addSource(database.invoiceDao().getAllInvoicesFromDao(), invoicesEntities -> {
            if (database.isDatabaseCreated().getValue() != null) {
                observableInvoices.postValue(invoicesEntities);
            }
        });
    }

    public static synchronized InvoicesRepository getInstance() {
        if (instance == null) {
            instance = new InvoicesRepository();
        }
        return instance;
    }

    public void insertInvoices(InvoiceEntity... invoices) {
        AppExecutors.ioThread(() -> {
            database.invoiceDao().insertInvoices(invoices);
        });
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<InvoiceEntity>> getAllInvoicesFromRepository() {
        // Update data from API
        refreshInvoices();
        // Return invoices from updated database
        return database.invoiceDao().getAllInvoicesFromDao();
    }

    public void deleteAll() {
        database.invoiceDao().deleteAll();
    }

    private void refreshInvoices() {
        // Use of Retrofit to call the API and update the database
        Call<InvoicesApiResponse> call = InvoicesRetrofitApiService.getApiService().getInvoices();
        call.enqueue(new Callback<InvoicesApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<InvoicesApiResponse> call, Response<InvoicesApiResponse> response) {
                if (response.isSuccessful()) {
                    InvoicesApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        // Retrieve the invoice list from the api
                        ArrayList<InvoiceVO> invoicesList = (ArrayList<InvoiceVO>) apiResponse.getFacturas();
                        List<InvoiceEntity> invoicesEntitites = InvoiceEntity.fromInvoiceVoList(invoicesList);
                        observableInvoices.setValue(invoicesEntitites);
                        Log.d("Fill/update invoicesList", "Api -> Size of 'facturas' list: " + invoicesList.size());
                        // Set state as strings from app resources
                        setStringsForInvoiceDescEstado(invoicesList);
                        // Insert the data into Room database
                        populateRoomDatabase(invoicesList);
                    }
                }
                Log.d("refreshInvoices()", "Invoices refreshed");
            }

            @Override
            public void onFailure(Call<InvoicesApiResponse> call, Throwable t) {
                String error = t.getLocalizedMessage();
                Log.d("onFailure error message", error);
            }
        });
    }

    private void setStringsForInvoiceDescEstado(ArrayList<InvoiceVO> invoicesList) {
        invoicesList.forEach(invoice -> invoice.setDescEstado(invoice.getDescEstado()));
    }

    private void populateRoomDatabase(ArrayList<InvoiceVO> invoicesList) {
        AppExecutors.ioThread(() -> {
            this.deleteAll();
            this.insertInvoices(InvoiceEntity.fromInvoiceVoArray(invoicesList));
        });
    }
}
