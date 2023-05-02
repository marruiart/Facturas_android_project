package com.example.facturas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class FilterFragment extends Fragment
        implements SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    private ArrayList<InvoiceVO> retrievedList;
    private int mSelectedButtonId;
    private OnDataPassListener activityCallback;

    public FilterFragment() {
        // Empty constructor needed
    }

    public interface OnDataPassListener {
        public void onFilterApply();
    }

    private void retrieveInvoiceList() {
        // Get the ArrayList from the Bundle
        Bundle bundle = getArguments();
        retrievedList = bundle.getParcelableArrayList("invoicesList");
    }

    private void findMaxRangeAmount() {
        for (InvoiceVO invoice : retrievedList) {
            if (invoice.getImporteOrdenacion() > FilterDataVO.getMaxRangeAmount())
                FilterDataVO.setMaxRangeAmount((int) Math.ceil(invoice.getImporteOrdenacion()));
        }
    }

    private void setClickListeners(View view) {
        ((SeekBar) view.findViewById(R.id.seekBar_amount)).setOnSeekBarChangeListener(this);
        view.findViewById(R.id.btn_pickerFrom).setOnClickListener(this);
        view.findViewById(R.id.btn_pickerTo).setOnClickListener(this);
        view.findViewById(R.id.btn_apply).setOnClickListener(this);
        view.findViewById(R.id.btn_erase).setOnClickListener(this);
    }

    private void applyFilter() {
        activityCallback.onFilterApply();
    }

    private void resetFilter() {
        resetDatePickers();
        resetSeekbarAmount();
        resetAllCheckboxes();
    }

    private void resetDatePickers() {
        FilterDataVO.setDateIssuedFrom(new Date(0));
        Button btnPickerFrom = getView().findViewById(R.id.btn_pickerFrom);
        btnPickerFrom.setText(getResources().getString(R.string.dd_MM_yy));
        FilterDataVO.setDateIssuedTo(new Date());
        Button btnPickerTo = getView().findViewById(R.id.btn_pickerTo);
        btnPickerTo.setText(getResources().getString(R.string.dd_MM_yy));
        Log.d("resetDatePickers", "datePickers reset");
    }

    private void resetSeekbarAmount() {
        FilterDataVO.setMaxRangeAmount(FilterDataVO.getMaxRangeAmount());
        ((SeekBar) getView().findViewById(R.id.seekBar_amount)).setProgress(FilterDataVO.getMaxRangeAmount());
        Log.d("resetSeekbarAmount", "SeekBar reset");
    }

    private void resetAllCheckboxes() {
        for (Map.Entry<Integer, Boolean> state : FilterDataVO.getState().entrySet()) {
            CheckBox c = getView().findViewById(state.getKey());
            c.setChecked(false);
            state.setValue(false);
        }
        Log.d("resetAllCheckboxes", "CheckBoxes reset");
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
                    FilterDataVO.putStateCheckbox(child.getId(), ((CheckBox) child).isChecked());
                }
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context activity) {
        super.onAttach(activity);
        try {
            activityCallback = (OnDataPassListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement ToolbarListener");
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
        findAllCheckboxes((ViewGroup) view.getRootView());
        setClickListeners(view);
        retrieveInvoiceList();
        findMaxRangeAmount();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSeekbarRange(FilterDataVO.getMinRangeAmount(), FilterDataVO.getMaxRangeAmount());
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        String idString = getResources().getResourceEntryName(view.getId());
        FilterDataVO.putStateCheckbox(view.getId(), view.isChecked());
        Log.d(idString, "" + isChecked);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        FilterDataVO.setMaxRangeAmount(progress);
        Log.d("SeekBar", "New max in range: " + progress);
        setFilteredRangeTextview(FilterDataVO.getMinRangeAmount(), FilterDataVO.getMaxRangeAmount());
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
            FilterDataVO.setDateIssuedFrom(date);
            ((Button) getView().findViewById(R.id.btn_pickerFrom)).setText(dateStr);
        } else if (mSelectedButtonId == R.id.btn_pickerTo) {
            FilterDataVO.setDateIssuedTo(date);
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
        } else if (v.getId() == R.id.btn_erase) {
            resetFilter();
        } else if (v.getId() == R.id.btn_apply) {
            applyFilter();
        }
    }
}