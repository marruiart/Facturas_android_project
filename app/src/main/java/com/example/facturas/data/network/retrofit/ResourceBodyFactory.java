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
        return App.getContext().getResources().openRawResource(R.raw.invoices);
    }
}

