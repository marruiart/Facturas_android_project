package com.example.facturas;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInvoice(InvoiceVO invoice);

    @Query("SELECT * FROM invoices")
    List<InvoiceVO> getAllInvoices();
}
