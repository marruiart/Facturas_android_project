package com.example.facturas.utils;

import java.time.LocalDate;

public final class MyConstants {

    private MyConstants() {
        // Private constructor to hide the implicit public one.
    }

    // General
    public static final String DATE_FORMAT = "dd/MMM/yyyy";
    public static final String API_DATE_FORMAT = "dd/MM/yyyy";

    // InvoiceVO
    public static final String INVOICE_DESC_ESTADO_PAGADA = "Pagada";
    public static final String INVOICE_DESC_ESTADO_ANULADA = "Anulada";
    public static final String INVOICE_DESC_ESTADO_CUOTA_FIJA = "Cuota fija";
    public static final String INVOICE_DESC_ESTADO_PENDIENTE = "Pendiente de pago";
    public static final String INVOICE_DESC_ESTADO_PLAN_PAGO = "Plan de pago";

    // Retrofit Api service
    public static final String BASE_URL = "https://viewnextandroid2.wiremockapi.cloud/";
    public static final String URL_PATH = "facturas";

    // Dates
    public static final LocalDate EPOCH_DATE = LocalDate.ofEpochDay(0);
    public static final LocalDate NOW = LocalDate.now();

}
