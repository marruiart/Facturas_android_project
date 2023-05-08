package com.example.facturas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FilterFragment extends Fragment {
    private FilterDataVO filter = null;
    private ArrayList<InvoiceVO> retrievedList;
    private OnDataPassListener activityCallback;

    public FilterFragment() {
        // Empty constructor needed
    }

    public interface OnDataPassListener {
        void onFilterApply(ArrayList<InvoiceVO> filteredInvoicesList, FilterDataVO filter);

        void onFilterClose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        retrievePassedData();
        if (filter.getState().isEmpty()) {
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
    public void onAttach(@NonNull Context activity) {
        super.onAttach(activity);
        try {
            activityCallback = (OnDataPassListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement ToolbarListener");
        }
    }

    private void retrievePassedData() {
        Bundle bundle = getArguments();
        assert bundle != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Get the ArrayList from the Bundle
            retrievedList = bundle.getParcelableArrayList("invoicesList", InvoiceVO.class);
            // Get the filter from the Bundle
            filter = bundle.getParcelable("filter", FilterDataVO.class);
        } else {
            retrievedList = bundle.getParcelableArrayList("invoicesList");
            filter = bundle.getParcelable("filter");
        }
    }

    private Date getCalendarDate(Calendar calendar, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

    private int getCurrentDateFromCalendar(final int YEAR_MONTH_OR_DAY) {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(YEAR_MONTH_OR_DAY);
    }

    private void setClickListeners(View view) {
        Button fromDateBtn;
        Button toDateBtn;
        HashMap<Integer, Boolean> allStateCheckboxes;

        setSeekBarListener(view);
        fromDateBtn = view.findViewById(R.id.btn_pickerFrom);
        setDateButtonListener(fromDateBtn);
        toDateBtn = view.findViewById(R.id.btn_pickerTo);
        setDateButtonListener(toDateBtn);
        setApplyButtonListener(view);
        setCloseButtonListener(view);
        setResetButtonListener(view);
        allStateCheckboxes = filter.getState();
        for (Map.Entry<Integer, Boolean> set : allStateCheckboxes.entrySet()) {
            CheckBox stateCheckbox = view.findViewById(set.getKey());
            stateCheckbox.setOnCheckedChangeListener(this::updateCheckboxStatus);
        }
    }

    // DatePickers related methods
    private void setDatePickersStatus(View view) {
        SimpleDateFormat dateFormat;
        String dateStr;
        Date originDate = new Date(0);
        Date now = new Date();

        dateFormat = new SimpleDateFormat(MyConstants.DATE_FORMAT, Locale.getDefault());
        if (!originDate.equals(filter.getDateIssuedFrom())) {
            dateStr = dateFormat.format(filter.getDateIssuedFrom());
            Button btnPickerFrom = view.findViewById(R.id.btn_pickerFrom);
            if (btnPickerFrom != null) {
                btnPickerFrom.setText(dateStr);
            }
        }
        if (!now.equals(filter.getDateIssuedTo())) {
            dateStr = dateFormat.format(filter.getDateIssuedTo());
            Button btnPickerTo = view.findViewById(R.id.btn_pickerTo);
            if (btnPickerTo != null) {
                btnPickerTo.setText(dateStr);
            }
        }
    }

    private void setDateButtonListener(Button button) {
        // Create listener that change button text on date set
        if (button != null) {
            DatePickerDialog.OnDateSetListener listener = createDateButtonListener(button);
            button.setOnClickListener(btnView -> showDatePickerDialog(listener));
        } else {
            Log.d("setDateButtonListener", "Null button");
        }
    }

    private DatePickerDialog.OnDateSetListener createDateButtonListener(Button clickedBtn) {
        return (view, year, month, dayOfMonth) -> {
            Date date = getCalendarDate(Calendar.getInstance(), year, month, dayOfMonth);
            if (clickedBtn.getId() == R.id.btn_pickerFrom) {
                filter.setDateIssuedFrom(date);
            } else if (clickedBtn.getId() == R.id.btn_pickerTo) {
                filter.setDateIssuedTo(date);
            }
            changeDateButtonText(date, clickedBtn);
        };
    }

    private void changeDateButtonText(Date date, Button clickedBtn) {
        // Set picked date
        String dateStr = new SimpleDateFormat(MyConstants.DATE_FORMAT, Locale.getDefault()).format(date);
        clickedBtn.setText(dateStr);
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener listener) {
        int currentYear = getCurrentDateFromCalendar(Calendar.YEAR);
        int currentMonth = getCurrentDateFromCalendar(Calendar.MONTH);
        int currentDay = getCurrentDateFromCalendar(Calendar.DAY_OF_MONTH);

        // Show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), listener, currentYear, currentMonth, currentDay);
        datePickerDialog.show();
    }


    // SeekBar related methods
    private void findMaxRangeAmount() {
        for (InvoiceVO invoice : retrievedList) {
            if (invoice.getImporteOrdenacion() > filter.getMaxRangeAmount()) {
                filter.setMaxRangeAmount((int) Math.ceil(invoice.getImporteOrdenacion()));
            }
        }
    }

    private void setSeekBarListener(View view) {
        SeekBar amountSeekbar;
        amountSeekbar = view.findViewById(R.id.seekBar_amount);
        if (amountSeekbar != null) {
            amountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    filter.setAmountProgress(progress);
                    Log.d("SeekBar", "New max in range: " + progress);
                    setFilteredRangeTextview(getView());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // OnSeekBarChangeListener interface method
                    Log.d("onStartTrackingTouch", FilterFragment.this.getClass().toString());
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // OnSeekBarChangeListener interface method
                    Log.d("onStopTrackingTouch", FilterFragment.this.getClass().toString());
                }
            });
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
        minRangeTv.setText(String.format(getString(R.string.fragment_filter_tv_range_boundary), filter.getMinRangeAmount()));
        maxRangeTv.setText(String.format(getString(R.string.fragment_filter_tv_range_boundary), filter.getMaxRangeAmount()));
    }

    private void setSeekBarProgress(SeekBar amountSeekbar) {
        int progress = filter.getAmountProgress();
        if (progress == 0 || progress > filter.getMaxRangeAmount()) {
            filter.setAmountProgress(filter.getMaxRangeAmount());
        }
        amountSeekbar.setProgress(filter.getAmountProgress());
    }

    private void setFilteredRangeTextview(View view) {
        if (view != null) {
            TextView filteredRangeTv = view.findViewById(R.id.textView_filteredRange);
            filteredRangeTv.setText(String.format(getString(R.string.fragment_filter_tv_filtered_range), filter.getMinRangeAmount(), filter.getAmountProgress()));
        }
    }

    // CheckBox related methods
    private void findAllCheckboxes(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            int id = child.getId();
            if (child instanceof CheckBox && id != View.NO_ID) {
                String idString = getResources().getResourceEntryName(id);
                if (idString.contains("checkBox") && filter.getState().get(id) == null) {
                    filter.putStateCheckbox(id, ((CheckBox) child).isChecked());
                }
            }
        }
    }

    private void setStateCheckboxesStatus(View view) {
        for (Map.Entry<Integer, Boolean> set : filter.getState().entrySet()) {
            CheckBox c = view.findViewById(set.getKey());
            c.setChecked(set.getValue());
        }
    }

    private void updateCheckboxStatus(CompoundButton view, boolean isChecked) {
        String idString = getResources().getResourceEntryName(view.getId());
        filter.putStateCheckbox(view.getId(), view.isChecked());
        Log.d(idString, "Checked: " + isChecked);
    }

    // Apply filter methods
    private void setApplyButtonListener(View view) {
        Button applyButton = view.findViewById(R.id.btn_apply);
        if (applyButton != null) {
            applyButton.setOnClickListener(applyBtn -> applyFilter());
        }
    }

    private void applyFilter() {
        ArrayList<InvoiceVO> filteredInvoicesList = (ArrayList<InvoiceVO>) retrievedList.clone();
        applyDateFilter(filteredInvoicesList);
        applyAmountFilter(filteredInvoicesList);
        filteredInvoicesList = applyStateFilter(filteredInvoicesList);
        activityCallback.onFilterApply(filteredInvoicesList, filter);
    }

    private void applyDateFilter(ArrayList<InvoiceVO> filteredInvoicesList) {
        filteredInvoicesList.removeIf(i -> i.getFecha().before(filter.getDateIssuedFrom()));
        filteredInvoicesList.removeIf(i -> i.getFecha().after(filter.getDateIssuedTo()));
    }

    private void applyAmountFilter(ArrayList<InvoiceVO> filteredInvoicesList) {
        filteredInvoicesList.removeIf(i -> i.getImporteOrdenacion() > filter.getAmountProgress());
    }

    private ArrayList<InvoiceVO> applyStateFilter(ArrayList<InvoiceVO> filteredInvoicesList) {
        ArrayList<InvoiceVO> newFilteredInvoiceList = new ArrayList<>();
        boolean filterApplied = false;
        for (Map.Entry<Integer, Boolean> state : filter.getState().entrySet()) {
            if (Boolean.TRUE.equals(state.getValue())) {
                ArrayList<InvoiceVO> tmpList = (ArrayList<InvoiceVO>) filteredInvoicesList.clone();
                CheckBox stateCheckbox = getView().findViewById(state.getKey());
                if (stateCheckbox != null) {
                    String checkboxText = (String) stateCheckbox.getText();
                    tmpList.removeIf(invoice -> !invoice.getDescEstado().equals(checkboxText));
                    newFilteredInvoiceList.addAll(tmpList);
                    filterApplied = true;
                } else {
                    Log.d("applyStateFilter", "null stateCheckbox");
                }
            }
        }
        return filterApplied ? newFilteredInvoiceList : filteredInvoicesList;
    }

    // Reset filter methods
    private void setResetButtonListener(View view) {
        Button resetButton = view.findViewById(R.id.btn_erase);
        if (resetButton != null) {
            resetButton.setOnClickListener(eraseBtn -> resetFilters());
        }
    }

    private void resetFilters() {
        filter.resetInstance(getView());
        resetDatePickers();
        resetSeekbarAmount();
    }

    private void resetDatePickers() {
        resetDateButton(R.id.btn_pickerFrom);
        resetDateButton(R.id.btn_pickerTo);
    }

    private void resetDateButton(int id) {
        Button btnPicker = getView().findViewById(id);
        if (btnPicker != null) {
            btnPicker.setText(getResources().getString(R.string.general_dd_MM_yy));
        } else {
            Log.d("resetDateButton", "null btnPicker");
        }
    }

    private void resetSeekbarAmount() {
        SeekBar amountSeekbar;
        amountSeekbar = getView().findViewById(R.id.seekBar_amount);
        if (amountSeekbar != null) {
            amountSeekbar.setProgress(filter.getAmountProgress());
            Log.d("resetSeekbarAmount", "SeekBar reset");
        } else {
            Log.d("resetSeekbarAmount", "null amountSeekbar");
        }
    }

    // Close button methods
    private void setCloseButtonListener(View view) {
        ImageButton closeButton = view.findViewById(R.id.btn_close);
        if (closeButton != null) {
            closeButton.setOnClickListener(closeBtn -> closeFilter());
        }
    }

    private void closeFilter() {
        activityCallback.onFilterClose();
    }
}