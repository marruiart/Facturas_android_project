package com.example.facturas.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.facturas.App;
import com.example.facturas.InvoicesRepository;
import com.example.facturas.data.database.entity.InvoiceEntity;

import java.util.List;

public class InvoicesListViewModel extends AndroidViewModel {
    private InvoicesRepository repository;
    private LiveData<List<InvoiceEntity>> allInvoices;

    public InvoicesListViewModel(@NonNull Application application) {
        super(application);
        repository = App.getRepository();
        allInvoices = repository.getAllInvoicesFromRepository();
    }

    public void insertInvoices(InvoiceEntity... invoices) {
        repository.insertInvoices(invoices);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<InvoiceEntity>> getAllInvoicesFromViewModel() {
        return allInvoices;
    }
}
