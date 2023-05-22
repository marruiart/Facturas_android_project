package com.example.facturas.data.model;

import java.time.LocalDate;

public interface Invoice {
    int getId();

    String getDescEstado();

    Float getImporteOrdenacion();

    LocalDate getFecha();

    Integer getTextColor();
}
