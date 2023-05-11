package com.example.facturas;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context appContext;
    private static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        db = AppDatabase.getInstance(getApplicationContext());
    }
    public static Context getContext(){
        return appContext;
    }

    public static AppDatabase getDatabase() {
        return db;
    }
}
