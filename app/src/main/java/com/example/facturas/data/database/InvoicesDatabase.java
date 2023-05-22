package com.example.facturas.data.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.*;

import com.example.facturas.data.database.entity.InvoiceEntity;
import com.example.facturas.data.database.converter.DateConverter;
import com.example.facturas.data.database.dao.InvoiceDao;

@Database(entities = {InvoiceEntity.class}, version = 3)
@TypeConverters({DateConverter.class})
public abstract class InvoicesDatabase extends RoomDatabase {

    private static final String DB_NAME = "invoice_db";
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public abstract InvoiceDao invoiceDao();

    private static InvoicesDatabase instance;

    public static synchronized InvoicesDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static InvoicesDatabase create(final Context context) {
        return Room.databaseBuilder(context, InvoicesDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public LiveData<Boolean> isDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
