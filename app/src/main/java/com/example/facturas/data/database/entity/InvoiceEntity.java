package com.example.facturas.data.database.entity;

import androidx.room.*;

import com.example.facturas.data.model.Invoice;
import com.example.facturas.data.model.InvoiceVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "invoices")
public class InvoiceEntity implements Invoice {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "descEstado")
    private String descEstado;
    @ColumnInfo(name = "importeOrdenacion")
    private Float importeOrdenacion;
    @ColumnInfo(name = "fecha")
    private Date fecha;
    @ColumnInfo(name = "textColor")
    private Integer textColor;

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
        this.descEstado = descEstado;
    }

    @Override
    public Float getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public void setImporteOrdenacion(Float importeOrdenacion) {
        this.importeOrdenacion = importeOrdenacion;
    }

    @Override
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public Integer getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public InvoiceEntity() {
    }

    @Ignore
    public InvoiceEntity(String descEstado, Float importeOrdenacion, Date fecha, Integer textColor) {
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
        this.textColor = textColor;
    }

    public InvoiceEntity(Invoice invoice) {
        this.id = invoice.getId();
        this.descEstado = invoice.getDescEstado();
        this.importeOrdenacion = invoice.getImporteOrdenacion();
        this.fecha = invoice.getFecha();
        this.textColor = invoice.getTextColor();
    }

    public static InvoiceEntity fromInvoiceVo(InvoiceVO invoice) {
        return new InvoiceEntity(invoice.getDescEstado(), invoice.getImporteOrdenacion(), invoice.getFecha(), invoice.getTextColor());
    }

    public InvoiceVO toInvoiceVO() {
        return new InvoiceVO(descEstado, importeOrdenacion, fecha, textColor);
    }

    public static List<InvoiceEntity> fromInvoiceVoList(List<InvoiceVO> invoiceVOList) {
        ArrayList<InvoiceEntity> invoicesList = new ArrayList<>();
        invoiceVOList.forEach(invoice -> invoicesList.add(fromInvoiceVo(invoice)));
        return invoicesList;
    }

    public static InvoiceEntity[] fromInvoiceVoArray(List<InvoiceVO> invoiceVOList) {
        ArrayList<InvoiceEntity> invoicesList = new ArrayList<>();
        invoiceVOList.forEach(invoice -> invoicesList.add(fromInvoiceVo(invoice)));
        return invoicesList.toArray(new InvoiceEntity[invoicesList.size()]);
    }

    public static ArrayList<InvoiceVO> fromInvoiceEntityList(List<InvoiceEntity> invoiceEntityList) {
        ArrayList<InvoiceVO> invoicesList = new ArrayList<>();
        invoiceEntityList.forEach(invoice -> invoicesList.add(invoice.toInvoiceVO()));
        return invoicesList;
    }
}
