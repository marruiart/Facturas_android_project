package com.example.facturas;

import android.widget.CheckBox;

import java.util.Date;
import java.util.HashMap;

public class FilterDataVO {
    private static int minRangeAmount = 0;
    private static int maxRangeAmount = InvoiceVO.maxImporteOrdenacion;
    private static Date dateIssuedFrom = new Date(0);
    private static Date dateIssuedTo = new Date();
    private static HashMap<Integer, Boolean> state = new HashMap<>();

    public static int getMinRangeAmount() {
        return minRangeAmount;
    }

    public static void setMinRangeAmount(int minRangeAmount) {
        FilterDataVO.minRangeAmount = minRangeAmount;
    }

    public static int getMaxRangeAmount() {
        return maxRangeAmount;
    }

    public static void setMaxRangeAmount(int maxRangeAmount) {
        FilterDataVO.maxRangeAmount = maxRangeAmount;
    }

    public static Date getDateIssuedFrom() {
        return dateIssuedFrom;
    }

    public static void setDateIssuedFrom(Date dateIssuedFrom) {
        FilterDataVO.dateIssuedFrom = dateIssuedFrom;
    }

    public static Date getDateIssuedTo() {
        return dateIssuedTo;
    }

    public static void setDateIssuedTo(Date dateIssuedTo) {
        FilterDataVO.dateIssuedTo = dateIssuedTo;
    }

    public static HashMap<Integer, Boolean> getState() {
        return state;
    }

    public static void setState(HashMap<Integer, Boolean> state) {
        FilterDataVO.state = state;
    }

    public static void putStateCheckbox (Integer id, Boolean isChecked) {
        state.put(id, isChecked);
    }
}
