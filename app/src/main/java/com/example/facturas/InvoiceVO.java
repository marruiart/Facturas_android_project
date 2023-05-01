package com.example.facturas;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;

import android.content.res.Resources;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceVO {
    public static int maxImporteOrdenacion = 0;
    private String descEstado;
    private Float importeOrdenacion;
    private Date fecha;
    private int textColor;

    public InvoiceVO(String descEstado, String importeOrdenacion, String fecha) {
        setDescEstado(descEstado);
        this.importeOrdenacion = Float.parseFloat(importeOrdenacion);
        setFecha(fecha);
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        Resources context = App.getContext().getResources();
        switch (descEstado) {
            case "Pagada":
                this.textColor = BLUE;
                this.descEstado = context.getString(R.string.paid);
                break;
            case "Anulada":
                this.descEstado = context.getString(R.string.canceled);
                break;
            case "Cuota fija":
                this.descEstado = context.getString(R.string.fixed_fee);
                break;
            case "Pendiente de pago":
                this.textColor = RED;
                this.descEstado = context.getString(R.string.pending_payment);
                break;
            case "Plan de pago":
                this.descEstado = context.getString(R.string.payment_plan);
                break;
            default:
                this.textColor = BLACK;
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

    public void setFecha(String fecha) {
        try {
            this.fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
        } catch (ParseException e) {
            Log.d(TAG, e.getStackTrace().toString());
        }
    }

    public int getTextColor() {
        return textColor;
    }
}
