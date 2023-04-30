package com.example.facturas;

import java.util.ArrayList;

public class InvoicesApiResponse {
    private int numFacturas;
    private ArrayList<InvoiceVO> facturas;

    public InvoicesApiResponse(int numFacturas, ArrayList<InvoiceVO> facturas) {
        this.numFacturas = numFacturas;
        this.facturas = facturas;
    }

    public int getNumFacturas() {
        return numFacturas;
    }

    public void setNumFacturas(int numFacturas) {
        this.numFacturas = numFacturas;
    }

    public ArrayList<InvoiceVO> getFacturas() {
        return facturas;
    }

    public void setFacturas(ArrayList<InvoiceVO> facturas) {
        this.facturas = facturas;
    }
}
