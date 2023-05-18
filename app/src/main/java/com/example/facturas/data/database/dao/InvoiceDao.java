package com.example.facturas.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.facturas.data.database.entity.InvoiceEntity;

import java.util.List;

@Dao
public interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInvoices(InvoiceEntity... invoice);

    @Query("SELECT * FROM invoices")
    LiveData<List<InvoiceEntity>> getAllInvoicesFromDao();

    @Query("DELETE FROM invoices")
    void deleteAll();
}
