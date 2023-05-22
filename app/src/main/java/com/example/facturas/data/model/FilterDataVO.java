package com.example.facturas.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.CheckBox;

import com.example.facturas.utils.MyConstants;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FilterDataVO implements Parcelable {
    private int minRangeAmount;
    private int maxRangeAmount;
    private int amountProgress;
    private LocalDate dateIssuedFrom;
    private LocalDate dateIssuedTo;
    private HashMap<Integer, Boolean> state;

    public FilterDataVO() {
        this.minRangeAmount = 0;
        this.maxRangeAmount = 0;
        this.amountProgress = 0;
        this.dateIssuedFrom = MyConstants.EPOCH_DATE;
        this.dateIssuedTo = MyConstants.NOW;
        this.state = new HashMap<>();
    }

    private FilterDataVO(int minRangeAmount, int maxRangeAmount, int amountProgress, LocalDate dateIssuedFrom, LocalDate dateIssuedTo, HashMap<Integer, Boolean> state) {
        this.minRangeAmount = minRangeAmount;
        this.maxRangeAmount = maxRangeAmount;
        this.amountProgress = amountProgress;
        this.dateIssuedFrom = dateIssuedFrom;
        this.dateIssuedTo = dateIssuedTo;
        this.state = state;
    }

    public void resetInstance(View view) {
        this.amountProgress = this.maxRangeAmount;
        this.dateIssuedFrom = MyConstants.EPOCH_DATE;
        this.dateIssuedTo = MyConstants.NOW;
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

    public LocalDate getDateIssuedFrom() {
        return dateIssuedFrom;
    }

    public void setDateIssuedFrom(LocalDate dateIssuedFrom) {
        this.dateIssuedFrom = dateIssuedFrom;
    }

    public LocalDate getDateIssuedTo() {
        return dateIssuedTo;
    }

    public void setDateIssuedTo(LocalDate dateIssuedTo) {
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
        dest.writeSerializable(dateIssuedFrom);
        dest.writeSerializable(dateIssuedTo);
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
        dateIssuedFrom = (LocalDate) (in.readSerializable());
        dateIssuedTo = (LocalDate) (in.readSerializable());
        state = (HashMap<Integer, Boolean>) in.readSerializable();
    }

    public FilterDataVO cloneFilter() {
        return new FilterDataVO(minRangeAmount, maxRangeAmount, amountProgress, dateIssuedFrom, dateIssuedTo, state);
    }
}
