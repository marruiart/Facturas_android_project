package com.example.facturas.data.database.model;

import androidx.room.*;

import com.example.facturas.data.model.InvoiceVO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "invoices")
public class InvoiceEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "descEstado")
    private String descEstado;
    @ColumnInfo(name = "importeOrdenacion")
    private Float importeOrdenacion;
    @ColumnInfo(name = "fecha")
    private Date fecha;
    @ColumnInfo(name = "textColor")
    private int textColor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setImporteOrdenacion(Float importeOrdenacion) {
        this.importeOrdenacion = importeOrdenacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public InvoiceEntity(String descEstado, Float importeOrdenacion, Date fecha, int textColor) {
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
        this.textColor = textColor;
    }

    public static InvoiceEntity fromInvoiceVO(InvoiceVO invoice) {
        return new InvoiceEntity(invoice.getDescEstado(), invoice.getImporteOrdenacion(), invoice.getFecha(), invoice.getTextColor());
    }

    public InvoiceVO toInvoiceVO() {
        return new InvoiceVO(descEstado, importeOrdenacion, fecha, textColor);
    }

    public static InvoiceEntity[] fromInvoiceVOList(List<InvoiceVO> invoiceVOList) {
        ArrayList<InvoiceEntity> invoicesList = new ArrayList<>();
        invoiceVOList.forEach(invoice -> invoicesList.add(fromInvoiceVO(invoice)));
        return invoicesList.toArray(new InvoiceEntity[invoicesList.size()]);
    }

    public static ArrayList<InvoiceVO> toInvoiceVOList(List<InvoiceEntity> invoiceEntityList) {
        ArrayList<InvoiceVO> invoicesList = new ArrayList<>();
        invoiceEntityList.forEach(invoice -> invoicesList.add(invoice.toInvoiceVO()));
        return invoicesList;
    }
}
