package org.androidtown.test1;

import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class MyDatePickerDialog extends DialogFragment{
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2500;

    Button btnConfirm;
    Button btnCancel;
    int start_year,start_month;
    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    public MyDatePickerDialog()
    {
        start_month = cal.get(Calendar.MONTH) + 1;
        start_year = cal.get(Calendar.YEAR);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.activity_datepicker, null);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyDatePickerDialog.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                start_year = yearPicker.getValue();
                start_month =monthPicker.getValue();
                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                MyDatePickerDialog.this.getDialog().cancel();
            }
        });

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        //monthPicker.setValue(cal.get(Calendar.MONTH) + 1);
        monthPicker.setValue(start_month);
        int year = start_year;
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);

        builder.setView(dialog);

        return builder.create();
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }
}
