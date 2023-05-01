package com.example.facturas;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FilterFragment extends Fragment
        implements SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    private static int minRangeAmount = 0;
    private static int maxRangeAmount = InvoiceVO.maxImporteOrdenacion;
    private static Date dateIssuedFrom = new Date(0);
    private static Date dateIssuedTo = new Date();
    private static HashMap<String, Boolean> state = new HashMap<>();
    private int mSelectedButtonId;

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
        view.findViewById(R.id.btn_pickerFrom).setOnClickListener(this);
        view.findViewById(R.id.btn_pickerTo).setOnClickListener(this);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date date = calendar.getTime();
        String dateStr = new SimpleDateFormat("dd MMM yyyy").format(date);

        if (mSelectedButtonId == R.id.btn_pickerFrom) {
            dateIssuedFrom = date;
            ((Button) getView().findViewById(R.id.btn_pickerFrom)).setText(dateStr);
        } else if (mSelectedButtonId == R.id.btn_pickerTo) {
            dateIssuedTo = date;
            ((Button) getView().findViewById(R.id.btn_pickerTo)).setText(dateStr);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_pickerFrom || v.getId() == R.id.btn_pickerTo) {
            mSelectedButtonId = v.getId();

            // Get current date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);
            datePickerDialog.show();
        }
    }
}