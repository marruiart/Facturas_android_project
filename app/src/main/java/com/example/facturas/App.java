package com.example.facturas;

import android.app.Application;
import android.content.Context;

import com.example.facturas.data.database.InvoicesDatabase;

public class App extends Application {

    private static Context context;
    private static InvoicesDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        db = InvoicesDatabase.getInstance(context);
    }

    public static Context getContext() {
        return context;
    }

    public static InvoicesDatabase getDatabase() {
        return db;
    }

    public static InvoicesRepository getRepository() {
        return InvoicesRepository.getInstance();
    }
}
