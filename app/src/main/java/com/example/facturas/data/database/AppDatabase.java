package com.example.facturas.data.database;

import android.content.Context;

import androidx.room.*;

import com.example.facturas.data.database.model.InvoiceEntity;
import com.example.facturas.utils.Converters;
import com.example.facturas.data.database.dao.InvoiceDao;

@Database(entities = {InvoiceEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "invoice_db";

    public abstract InvoiceDao getInvoiceDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }
}
