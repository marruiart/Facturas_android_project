package com.example.facturas;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceVO {
    public static int maxImporteOrdenacion = 0;
    private String descEstado;
    private Float importeOrdenacion;
    private Date fecha;

    public InvoiceVO(String descEstado, String importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = Float.parseFloat(importeOrdenacion);
        setFecha(fecha);
    }

    public String getDescEstado() {
        return descEstado;
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
}
