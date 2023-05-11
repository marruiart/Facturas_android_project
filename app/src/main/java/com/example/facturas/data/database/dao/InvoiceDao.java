package com.example.facturas.data.database.dao;

import androidx.room.*;

import com.example.facturas.data.database.model.InvoiceEntity;
import com.example.facturas.data.model.InvoiceVO;

import java.util.List;

@Dao
public interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInvoices(InvoiceEntity... invoice);

    @Query("SELECT * FROM invoices")
        // TODO implement LiveData
    List<InvoiceEntity> getAllInvoices();

    @Query("DELETE FROM invoices")
    void deleteAll();
}
