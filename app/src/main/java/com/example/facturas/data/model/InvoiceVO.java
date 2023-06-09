package com.example.facturas.data.model;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.example.facturas.App;
import com.example.facturas.R;
import com.example.facturas.data.database.entity.InvoiceEntity;
import com.example.facturas.utils.MyConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class InvoiceVO implements Parcelable, Invoice {
    private int id;
    private String descEstado;
    private final Float importeOrdenacion;
    private final LocalDate fecha;
    private Integer textColor;

    @Ignore
    public InvoiceVO(String descEstado, Float importeOrdenacion, LocalDate fecha) {
        this(descEstado, importeOrdenacion, fecha, null);
    }

    public InvoiceVO(String descEstado, Float importeOrdenacion, LocalDate fecha, Integer textColor) {
        setDescEstado(descEstado);
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
        this.textColor = textColor;
    }

    public InvoiceEntity toInvoiceEntity() {
        return new InvoiceEntity(descEstado, importeOrdenacion, fecha, textColor);
    }

    // Constructor to read from Parcel
    protected InvoiceVO(Parcel in) {
        id = in.readInt();
        descEstado = in.readString();
        importeOrdenacion = in.readFloat();
        fecha = LocalDate.parse(in.readString());
        textColor = in.readInt();
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        Resources context = App.getContext().getResources();
        this.textColor = BLACK;
        switch (descEstado) {
            case MyConstants.INVOICE_DESC_ESTADO_PAGADA:
                this.descEstado = context.getString(com.example.facturas.R.string.fragment_filter_cb_paid);
                break;
            case MyConstants.INVOICE_DESC_ESTADO_ANULADA:
                this.descEstado = context.getString(com.example.facturas.R.string.fragment_filter_cb_canceled);
                break;
            case MyConstants.INVOICE_DESC_ESTADO_CUOTA_FIJA:
                this.descEstado = context.getString(com.example.facturas.R.string.fragment_filter_cb_fixed_fee);
                break;
            case MyConstants.INVOICE_DESC_ESTADO_PENDIENTE:
                this.textColor = RED;
                this.descEstado = context.getString(com.example.facturas.R.string.fragment_filter_cb_pending_payment);
                break;
            case MyConstants.INVOICE_DESC_ESTADO_PLAN_PAGO:
                this.descEstado = context.getString(R.string.fragment_filter_cb_payment_plan);
                break;
            default:
                this.descEstado = descEstado;
        }
    }

    @Override
    public Float getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public String getFecha(String format) {
        return fecha.format(DateTimeFormatter.ofPattern(format));
    }

    @Override
    public LocalDate getFecha() {
        return this.fecha;
    }

    @Override
    public Integer getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(descEstado);
        out.writeFloat(importeOrdenacion);
        out.writeString(fecha.toString());
        out.writeValue(textColor);
    }

    // Parcelable CREATOR
    public static final Parcelable.Creator<InvoiceVO> CREATOR = new Parcelable.Creator<InvoiceVO>() {
        @Override
        public InvoiceVO createFromParcel(Parcel in) {
            return new InvoiceVO(in);
        }

        @Override
        public InvoiceVO[] newArray(int size) {
            return new InvoiceVO[size];
        }
    };
}
