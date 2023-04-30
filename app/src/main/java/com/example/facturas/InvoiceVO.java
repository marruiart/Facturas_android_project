package com.example.facturas;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceVO {
    private String descEstado;
    private Float importeOrdenacion;
    private String fecha;

    public InvoiceVO(String descEstado, String importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = Float.parseFloat(importeOrdenacion);
        this.fecha = fecha;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    public Float getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public void setImporteOrdenacion(String importeOrdenacion) {
        this.importeOrdenacion = Float.parseFloat(importeOrdenacion);
    }

    public String getFecha() {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(this.fecha);
        } catch (ParseException e) {
            Log.d(TAG, e.getStackTrace().toString());
        }
        return new SimpleDateFormat("dd MMM yyyy").format(date);
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
