package com.example.facturas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InvoicesRecyclerAdapter extends RecyclerView.Adapter<InvoicesRecyclerAdapter.InvoicesViewHolder> {
    private ArrayList<InvoiceVO> invoices = new ArrayList<>();

    public InvoicesRecyclerAdapter(ArrayList<InvoiceVO> invoices) {
        this.invoices = invoices;
    }

    @NonNull
    @Override
    public InvoicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_invoices, parent, false);
        return new InvoicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoicesViewHolder invoicesViewHolder, int position) {
        InvoiceVO invoice = invoices.get(position);
        invoicesViewHolder.getTvInvoiceDate().setText(invoice.getFecha("dd MMM yyyy"));
        invoicesViewHolder.getTvInvoiceAmount().setText(String.format("%.2f €", invoice.getImporteOrdenacion()));
        if (invoice.getImporteOrdenacion() > InvoiceVO.maxImporteOrdenacion)
            InvoiceVO.maxImporteOrdenacion = (int) Math.ceil(invoice.getImporteOrdenacion());
        invoicesViewHolder.getTvInvoiceState().setText(invoice.getDescEstado().equals("Pagada") ? "" : invoice.getDescEstado());
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    public class InvoicesViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvInvoiceDate;
        private final TextView tvInvoiceState;
        private final TextView tvInvoiceAmount;

        public InvoicesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvInvoiceDate = (TextView) itemView.findViewById(R.id.textView_InvoiceDate);
            this.tvInvoiceState = (TextView) itemView.findViewById(R.id.textView_InvoiceState);
            this.tvInvoiceAmount = (TextView) itemView.findViewById(R.id.textView_invoiceAmount);
        }

        public TextView getTvInvoiceDate() {
            return tvInvoiceDate;
        }

        public TextView getTvInvoiceState() {
            return tvInvoiceState;
        }

        public TextView getTvInvoiceAmount() {
            return tvInvoiceAmount;
        }
    }
}
