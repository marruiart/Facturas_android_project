package com.example.facturas;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<InvoiceVO> invoicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_invoices);
        setLayoutManager();
        fillInvoiceList();
        initializeAdapter();
    }

    private LinearLayoutManager setLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        return layoutManager;
    }

    private void initializeAdapter() {
        InvoicesRecyclerAdapter adapter = new InvoicesRecyclerAdapter(invoicesList);
        mRecyclerView.setAdapter(adapter);
    }

    private void fillInvoiceList() {
        invoicesList.add(new InvoiceVO("Pagada", "40.15", "08/01/2019"));
        invoicesList.add(new InvoiceVO("Pendiente de pago", "40.15", "08/01/2019"));
        invoicesList.add(new InvoiceVO("Pendiente de pago", "40.15", "05/10/2018"));
        invoicesList.add(new InvoiceVO("Pagada", "40.15", "10/03/2020"));
        invoicesList.add(new InvoiceVO("Pendiente de pago", "40.15", "05/09/2018"));
        invoicesList.add(new InvoiceVO("Pendiente de pago", "40.15", "05/09/2018"));

    }
}