package com.example.facturas;

import java.util.Date;
import java.util.HashMap;

public class FilterDataVO {
    private static FilterDataVO instance = null;
    private int minRangeAmount;
    private int maxRangeAmount;
    private int amountProgress;
    private Date dateIssuedFrom;
    private Date dateIssuedTo;
    private HashMap<Integer, Boolean> state;

    public FilterDataVO(int minRangeAmount, int maxRangeAmount, int amountProgress, Date dateIssuedFrom, Date dateIssuedTo) {
        this.minRangeAmount = minRangeAmount;
        this.maxRangeAmount = maxRangeAmount;
        this.amountProgress = amountProgress;
        this.dateIssuedFrom = dateIssuedFrom;
        this.dateIssuedTo = dateIssuedTo;
        this.state = new HashMap<>();
    }

    public static FilterDataVO getInstance() {
        if (instance == null) {
            instance = new FilterDataVO(0, 0, 0, new Date(0), new Date());
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public int getMinRangeAmount() {
        return minRangeAmount;
    }

    public void setMinRangeAmount(int minRangeAmount) {
        this.minRangeAmount = minRangeAmount;
    }

    public int getMaxRangeAmount() {
        return maxRangeAmount;
    }

    public void setMaxRangeAmount(int maxRangeAmount) {
        this.maxRangeAmount = maxRangeAmount;
    }

    public int getAmountProgress() {
        return amountProgress;
    }

    public void setAmountProgress(int amountProgress) {
        this.amountProgress = amountProgress;
    }

    public Date getDateIssuedFrom() {
        return dateIssuedFrom;
    }

    public void setDateIssuedFrom(Date dateIssuedFrom) {
        this.dateIssuedFrom = dateIssuedFrom;
    }

    public Date getDateIssuedTo() {
        return dateIssuedTo;
    }

    public void setDateIssuedTo(Date dateIssuedTo) {
        this.dateIssuedTo = dateIssuedTo;
    }

    public HashMap<Integer, Boolean> getState() {
        return state;
    }

    public void setState(HashMap<Integer, Boolean> state) {
        this.state = state;
    }

    public void putStateCheckbox(Integer id, Boolean isChecked) {
        state.put(id, isChecked);
    }
}
