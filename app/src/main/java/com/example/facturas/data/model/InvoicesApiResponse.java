package com.example.facturas.data.model;

import java.util.List;

public class InvoicesApiResponse {
    private int numFacturas;
    private List<InvoiceVO> facturas;

    public InvoicesApiResponse(int numFacturas, List<InvoiceVO> facturas) {
        this.numFacturas = numFacturas;
        this.facturas = facturas;
    }

    public int getNumFacturas() {
        return numFacturas;
    }

    public void setNumFacturas(int numFacturas) {
        this.numFacturas = numFacturas;
    }

    public List<InvoiceVO> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<InvoiceVO> facturas) {
        this.facturas = facturas;
    }
}

