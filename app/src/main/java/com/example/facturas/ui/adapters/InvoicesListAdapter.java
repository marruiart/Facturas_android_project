package com.example.facturas.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturas.R;
import com.example.facturas.App;
import com.example.facturas.data.database.entity.InvoiceEntity;
import com.example.facturas.data.model.InvoiceVO;
import com.example.facturas.utils.MyConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InvoicesListAdapter extends RecyclerView.Adapter<InvoicesListAdapter.InvoicesViewHolder> {
    private ArrayList<InvoiceVO> invoices;
    private RecyclerOnClickListener listener;

    public InvoicesListAdapter(List<InvoiceVO> invoices) {
        this.invoices = (ArrayList<InvoiceVO>) invoices;
    }

    public interface RecyclerOnClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(RecyclerOnClickListener listener) {
        this.listener = listener;
    }

    public void setInvoices(List<InvoiceVO> invoices) {
        this.invoices = (ArrayList<InvoiceVO>) invoices;
        notifyDataSetChanged();
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
        return invoices == null ? 0 : invoices.size();
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
