package com.example.facturas.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.CheckBox;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilterDataVO implements Parcelable {
    private int minRangeAmount;
    private int maxRangeAmount;
    private int amountProgress;
    private Date dateIssuedFrom;
    private Date dateIssuedTo;
    private HashMap<Integer, Boolean> state;

    public FilterDataVO() {
        this.minRangeAmount = 0;
        this.maxRangeAmount = 0;
        this.amountProgress = 0;
        this.dateIssuedFrom = new Date(0);
        this.dateIssuedTo = new Date();
        this.state = new HashMap<>();
    }

    private FilterDataVO(int minRangeAmount, int maxRangeAmount, int amountProgress, Date dateIssuedFrom, Date dateIssuedTo, HashMap<Integer, Boolean> state) {
        this.minRangeAmount = minRangeAmount;
        this.maxRangeAmount = maxRangeAmount;
        this.amountProgress = amountProgress;
        this.dateIssuedFrom = dateIssuedFrom;
        this.dateIssuedTo = dateIssuedTo;
        this.state = state;
    }

    public void resetInstance(View view) {
        this.amountProgress = this.maxRangeAmount;
        this.dateIssuedFrom = new Date(0);
        this.dateIssuedTo = new Date();
        state.forEach((checkboxId, isChecked) -> {
            CheckBox stateCheckbox = view.findViewById(checkboxId);
            stateCheckbox.setChecked(false);
        });
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

    public Map<Integer, Boolean> getState() {
        return state;
    }

    public void setState(Map<Integer, Boolean> state) {
        this.state = (HashMap<Integer, Boolean>) state;
    }

    public void putStateCheckbox(Integer id, Boolean isChecked) {
        state.put(id, isChecked);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minRangeAmount);
        dest.writeInt(maxRangeAmount);
        dest.writeInt(amountProgress);
        dest.writeLong(dateIssuedFrom.getTime());
        dest.writeLong(dateIssuedTo.getTime());
        dest.writeSerializable(state);
    }

    public static final Creator<FilterDataVO> CREATOR = new Creator<FilterDataVO>() {
        @Override
        public FilterDataVO createFromParcel(Parcel in) {
            return new FilterDataVO(in);
        }

        @Override
        public FilterDataVO[] newArray(int size) {
            return new FilterDataVO[size];
        }
    };

    private FilterDataVO(Parcel in) {
        minRangeAmount = in.readInt();
        maxRangeAmount = in.readInt();
        amountProgress = in.readInt();
        dateIssuedFrom = new Date(in.readLong());
        dateIssuedTo = new Date(in.readLong());
        state = (HashMap<Integer, Boolean>) in.readSerializable();
    }

    public FilterDataVO cloneFilter() {
        return new FilterDataVO(minRangeAmount, maxRangeAmount, amountProgress, dateIssuedFrom, dateIssuedTo, state);
    }
}
