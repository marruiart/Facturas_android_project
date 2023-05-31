package com.example.facturas.data.network.retrofit;

import androidx.annotation.NonNull;

import com.example.facturas.App;
import com.example.facturas.R;

import java.io.IOException;
import java.io.InputStream;

import co.infinum.retromock.BodyFactory;

public final class ResourceBodyFactory implements BodyFactory {

    @Override
    public InputStream create(@NonNull final String input) throws IOException {
        // this will throw if input is empty string, regular class loader opens a stream to directory
        InputStream inputStream;
        switch (input) {
            case "paid_invoices.json":
                inputStream = App.getContext().getResources().openRawResource(R.raw.paid_invoices);
                break;
            case "pending_invoices.json":
                inputStream = App.getContext().getResources().openRawResource(R.raw.pending_invoices);
                break;
            default:
                inputStream = App.getContext().getResources().openRawResource(R.raw.invoices);
        }
        return inputStream;
    }
}

