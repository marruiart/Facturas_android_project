package com.example.facturas;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceVO implements Parcelable {
    private String descEstado;
    private Float importeOrdenacion;
    private Date fecha;
    private int textColor;

    public InvoiceVO(String descEstado, Float importeOrdenacion, Date fecha) {
        setDescEstado(descEstado);
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
    }

    // Constructor to read from Parcel
    public InvoiceVO(Parcel in) {
        this.descEstado = in.readString();
        this.importeOrdenacion = in.readFloat();
        this.fecha = new Date(in.readLong());
        this.textColor = in.readInt();
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        Resources context = App.getContext().getResources();
        this.textColor = BLACK;
        switch (descEstado) {
            case "Pagada":
                this.descEstado = context.getString(R.string.fragment_filter_cb_paid);
                break;
            case "Anulada":
                this.descEstado = context.getString(R.string.fragment_filter_cb_canceled);
                break;
            case "Cuota fija":
                this.descEstado = context.getString(R.string.fragment_filter_cb_fixed_fee);
                break;
            case "Pendiente de pago":
                this.textColor = RED;
                this.descEstado = context.getString(R.string.fragment_filter_cb_pending_payment);
                break;
            case "Plan de pago":
                this.descEstado = context.getString(R.string.fragment_filter_cb_payment_plan);
                break;
            default:
                this.descEstado = "";
        }
    }

    public Float getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public String getFecha(String format) {
        return new SimpleDateFormat(format).format(fecha);
    }

    public Date getFecha() {
        return this.fecha;
    }

    public int getTextColor() {
        return textColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(descEstado);
        out.writeFloat(importeOrdenacion);
        out.writeLong(fecha.getTime());
        out.writeInt(textColor);
    }

    // Parcelable CREATOR
    public static final Parcelable.Creator<InvoiceVO> CREATOR = new Parcelable.Creator<InvoiceVO>() {
        public InvoiceVO createFromParcel(Parcel in) {
            return new InvoiceVO(in);
        }

        public InvoiceVO[] newArray(int size) {
            return new InvoiceVO[size];
        }
    };
}
