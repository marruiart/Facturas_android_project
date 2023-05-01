package com.example.facturas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;

public class FilterFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private static int minRangeAmount = 0;
    private static int maxRangeAmount = 999;
    private static Date dateIssuedFrom = new Date(0);
    private static Date dateIssuedTo = new Date();
    private static HashMap<String, Boolean> state = new HashMap<>();

    public FilterFragment() {
        // Empty constructor needed
    }

    public static int getMinRangeAmount() {
        return minRangeAmount;
    }

    public static int getMaxRangeAmount() {
        return maxRangeAmount;
    }

    public static Date getDateIssuedFrom() {
        return dateIssuedFrom;
    }

    public static Date getDateIssuedTo() {
        return dateIssuedTo;
    }

    public static HashMap<String, Boolean> getState() {
        return state;
    }

    private void setSeekbarRange(int minRangeAmount, int maxRangeAmount) {
        SeekBar amountSeekbar = getView().findViewById(R.id.seekBar_amount);
        TextView minRangeTv = getView().findViewById(R.id.textView_minRange);
        TextView maxRangeTv = getView().findViewById(R.id.textView_maxRange);
        amountSeekbar.setMax(maxRangeAmount);
        minRangeTv.setText(String.format("%d €", minRangeAmount));
        maxRangeTv.setText(String.format("%d €", maxRangeAmount));
        setFilteredRangeTextview(minRangeAmount, maxRangeAmount);
    }

    private void setFilteredRangeTextview(int minRangeAmount, int maxRangeAmount) {
        TextView filteredRangeTv = getView().findViewById(R.id.textView_filteredRange);
        filteredRangeTv.setText(String.format("%d €   -   %d €", minRangeAmount, maxRangeAmount));
    }

    private void findAllCheckboxes(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof CheckBox && child.getId() != View.NO_ID) {
                String idString = getResources().getResourceEntryName(child.getId());
                if (idString.contains("checkBox")) {
                    ((CheckBox) child).setOnCheckedChangeListener(this);
                    state.put(idString, ((CheckBox) child).isChecked());
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ((SeekBar) view.findViewById(R.id.seekBar_amount)).setOnSeekBarChangeListener(this);
        findAllCheckboxes((ViewGroup) view.getRootView());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSeekbarRange(minRangeAmount, maxRangeAmount);
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        String idString = getResources().getResourceEntryName(view.getId());
        state.put(idString, isChecked);
        Log.d(idString, "" + isChecked);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.maxRangeAmount = progress;
        Log.d("SeekBar", "New max in range: " + progress);
        setFilteredRangeTextview(minRangeAmount, maxRangeAmount);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // OnSeekBarChangeListener interface method
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // OnSeekBarChangeListener interface method
    }
}