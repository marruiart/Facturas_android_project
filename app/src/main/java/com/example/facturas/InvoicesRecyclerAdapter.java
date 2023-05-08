package com.example.facturas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InvoicesRecyclerAdapter extends RecyclerView.Adapter<InvoicesRecyclerAdapter.InvoicesViewHolder> {
    private final ArrayList<InvoiceVO> invoices;
    private RecyclerOnClickListener listener;

    public InvoicesRecyclerAdapter(ArrayList<InvoiceVO> invoices) {
        this.invoices = invoices;
    }

    public interface RecyclerOnClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(RecyclerOnClickListener listener) {
        this.listener = listener;
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
        invoicesViewHolder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
        InvoiceVO invoice = invoices.get(position);
        invoicesViewHolder.getTvInvoiceDate().setText(invoice.getFecha(MyConstants.DATE_FORMAT));
        invoicesViewHolder.getTvInvoiceAmount().setText(String.format(App.getContext().getString(R.string.invoice_amount), invoice.getImporteOrdenacion()));
        invoicesViewHolder.getTvInvoiceState().setText(invoice.getDescEstado());
        invoicesViewHolder.getTvInvoiceState().setTextColor(invoice.getTextColor());
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
            this.tvInvoiceDate = itemView.findViewById(R.id.textView_InvoiceDate);
            this.tvInvoiceState = itemView.findViewById(R.id.textView_InvoiceState);
            this.tvInvoiceAmount = itemView.findViewById(R.id.textView_invoiceAmount);
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
