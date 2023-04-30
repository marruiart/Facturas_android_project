package com.example.facturas;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InvoiceVO {
    private String descEstado;
    private float importeOrdenacion;
    private Date fecha;

    public InvoiceVO(String descEstado, String importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = Float.parseFloat(importeOrdenacion);
        try {
            this.fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
        } catch (ParseException e) {
            Log.d(TAG, e.getStackTrace().toString());
        }
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    public float getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public void setImporteOrdenacion(float importeOrdenacion) {
        this.importeOrdenacion = importeOrdenacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getFecha(String datePattern) {
        return new SimpleDateFormat(datePattern).format(fecha);
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
