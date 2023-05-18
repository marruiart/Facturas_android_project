package com.example.facturas.data.model;

import java.util.Date;

public interface Invoice {
    int getId();

    String getDescEstado();

    Float getImporteOrdenacion();

    Date getFecha();

    Integer getTextColor();
}
