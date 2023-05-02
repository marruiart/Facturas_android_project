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
import java.util.Locale;
import java.util.Map;

public class FilterFragment extends Fragment
        implements SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener {
    private static FilterDataVO filter = null;
    private ArrayList<InvoiceVO> retrievedList;
    private int mSelectedButtonId;
    private OnDataPassListener activityCallback;

    public FilterFragment() {
        // Empty constructor needed
    }

    public interface OnDataPassListener {
        void onFilterApply(ArrayList<InvoiceVO> filteredInvoicesList);
        void onFilterClose();
    }

    private void retrieveInvoiceList() {
        // Get the ArrayList from the Bundle
        Bundle bundle = getArguments();
        assert bundle != null;
        retrievedList = bundle.getParcelableArrayList("invoicesList");
    }

    private void findMaxRangeAmount() {
        for (InvoiceVO invoice : retrievedList) {
            if (invoice.getImporteOrdenacion() > filter.getMaxRangeAmount())
                filter.setMaxRangeAmount((int) Math.ceil(invoice.getImporteOrdenacion()));
        }
    }

    private void setClickListeners(View view) {
        ((SeekBar) view.findViewById(R.id.seekBar_amount)).setOnSeekBarChangeListener(this);
        view.findViewById(R.id.btn_pickerFrom).setOnClickListener(this);
        view.findViewById(R.id.btn_pickerTo).setOnClickListener(this);
        view.findViewById(R.id.btn_apply).setOnClickListener(this);
        view.findViewById(R.id.btn_close).setOnClickListener(this);
        view.findViewById(R.id.btn_erase).setOnClickListener(this);
        for (Map.Entry<Integer, Boolean> set : filter.getState().entrySet()) {
            CheckBox c = view.findViewById(set.getKey());
            c.setOnCheckedChangeListener(this);
        }
    }

    private void setDatePickersStatus(View view) {
        Date originDate = new Date(0);
        Date now = new Date();
        String dateStr;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        if (!originDate.equals(filter.getDateIssuedFrom())) {
            dateStr = dateFormat.format(filter.getDateIssuedFrom());
            ((Button) view.findViewById(R.id.btn_pickerFrom)).setText(dateStr);
        }
        if (!now.equals(filter.getDateIssuedTo())) {
            dateStr = dateFormat.format(filter.getDateIssuedTo());
            ((Button) view.findViewById(R.id.btn_pickerTo)).setText(dateStr);
        }
    }

    private void setStateCheckboxesStatus(View view) {
        for (Map.Entry<Integer, Boolean> set : filter.getState().entrySet()) {
            CheckBox c = view.findViewById(set.getKey());
            c.setChecked(set.getValue());
        }
    }

    private void setSeekbarStatus(View view) {
        SeekBar amountSeekbar = view.findViewById(R.id.seekBar_amount);
        setSeekBarRange(amountSeekbar, view);
        setSeekBarProgress(amountSeekbar);
        setFilteredRangeTextview(view);
    }

    private void setSeekBarRange(SeekBar amountSeekbar, View view) {
        TextView minRangeTv = view.findViewById(R.id.textView_minRange);
        TextView maxRangeTv = view.findViewById(R.id.textView_maxRange);
        amountSeekbar.setMax(filter.getMaxRangeAmount());
        minRangeTv.setText(String.format(Locale.getDefault(), "%d €", filter.getMinRangeAmount()));
        maxRangeTv.setText(String.format(Locale.getDefault(), "%d €", filter.getMaxRangeAmount()));
    }

    private void setSeekBarProgress(SeekBar amountSeekbar) {
        int progress = filter.getAmountProgress();
        if (progress == 0 || progress > filter.getMaxRangeAmount())
            filter.setAmountProgress(filter.getMaxRangeAmount());
        amountSeekbar.setProgress(filter.getAmountProgress());
    }

    private void setFilteredRangeTextview(View view) {
        if (view != null) {
            TextView filteredRangeTv = view.findViewById(R.id.textView_filteredRange);
            filteredRangeTv.setText(String.format("%d €   -   %d €",
                    filter.getMinRangeAmount(), filter.getAmountProgress()));
        }
    }

    private void findAllCheckboxes(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            int id = child.getId();
            if (child instanceof CheckBox && id != View.NO_ID) {
                String idString = getResources().getResourceEntryName(id);
                if (idString.contains("checkBox")) {
                    if (filter.getState().get(id) == null)
                        filter.putStateCheckbox(id, ((CheckBox) child).isChecked());
                }
            }
        }
    }

    private void applyFilter() {
        ArrayList<InvoiceVO> filteredInvoicesList = (ArrayList<InvoiceVO>) retrievedList.clone();
        applyDateFilter(filteredInvoicesList);
        applyAmountFilter(filteredInvoicesList);
        applyStateFilter(filteredInvoicesList);
        activityCallback.onFilterApply(filteredInvoicesList);
    }

    private void closeFilter() {
        activityCallback.onFilterClose();
    }

    private void applyDateFilter(ArrayList<InvoiceVO> filteredInvoicesList) {
        filteredInvoicesList.removeIf(i -> i.getFecha().before(filter.getDateIssuedFrom()));
        filteredInvoicesList.removeIf(i -> i.getFecha().after(filter.getDateIssuedTo()));
    }

    private void applyAmountFilter(ArrayList<InvoiceVO> filteredInvoicesList) {
        filteredInvoicesList.removeIf(i -> i.getImporteOrdenacion() > filter.getAmountProgress());
    }

    private void applyStateFilter(ArrayList<InvoiceVO> filteredInvoicesList) {
        for (Map.Entry<Integer, Boolean> state : filter.getState().entrySet()) {
            CheckBox c = getView().findViewById(state.getKey());
            String checkboxText = (String) c.getText();
            filteredInvoicesList.removeIf(i -> state.getValue() && !i.getDescEstado().equals(checkboxText));
        }
    }

    private void resetFilters() {
        resetDatePickers();
        resetSeekbarAmount();
        resetAllCheckboxes();
        FilterDataVO.resetInstance();
    }

    private void resetDatePickers() {
        filter.setDateIssuedFrom(new Date(0));
        Button btnPickerFrom = getView().findViewById(R.id.btn_pickerFrom);
        btnPickerFrom.setText(getResources().getString(R.string.dd_MM_yy));
        filter.setDateIssuedTo(new Date());
        Button btnPickerTo = getView().findViewById(R.id.btn_pickerTo);
        btnPickerTo.setText(getResources().getString(R.string.dd_MM_yy));
        Log.d("resetDatePickers", "datePickers reset");
    }

    private void resetSeekbarAmount() {
        filter.setMaxRangeAmount(filter.getMaxRangeAmount());
        filter.setAmountProgress(filter.getMaxRangeAmount());
        ((SeekBar) getView().findViewById(R.id.seekBar_amount)).setProgress(filter.getAmountProgress());
        Log.d("resetSeekbarAmount", "SeekBar reset");
    }

    private void resetAllCheckboxes() {
        for (Map.Entry<Integer, Boolean> state : filter.getState().entrySet()) {
            CheckBox c = getView().findViewById(state.getKey());
            c.setChecked(false);
            state.setValue(false);
        }
        Log.d("resetAllCheckboxes", "CheckBoxes reset");
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
        retrieveInvoiceList();
        if (filter == null) {
            filter = FilterDataVO.getInstance();
            findAllCheckboxes((ViewGroup) view.getRootView());
        } else {
            setDatePickersStatus(view);
            setStateCheckboxesStatus(view);
        }
        findMaxRangeAmount();
        setSeekbarStatus(view);
        setClickListeners(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        String idString = getResources().getResourceEntryName(view.getId());
        filter.putStateCheckbox(view.getId(), view.isChecked());
        Log.d(idString, "Checked: " + isChecked);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        filter.setAmountProgress(progress);
        Log.d("SeekBar", "New max in range: " + progress);
        setFilteredRangeTextview(getView());
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
        // Establish picked date
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date date = calendar.getTime();

        String dateStr = new SimpleDateFormat("dd MMM yyyy").format(date);
        if (mSelectedButtonId == R.id.btn_pickerFrom) {
            filter.setDateIssuedFrom(date);
            ((Button) getView().findViewById(R.id.btn_pickerFrom)).setText(dateStr);
        } else if (mSelectedButtonId == R.id.btn_pickerTo) {
            filter.setDateIssuedTo(date);
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
            resetFilters();
        } else if (v.getId() == R.id.btn_apply) {
            applyFilter();
        } else if (v.getId() == R.id.btn_close) {
            closeFilter();
        }
    }
}