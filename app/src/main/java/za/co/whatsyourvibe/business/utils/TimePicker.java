package za.co.whatsyourvibe.business.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePicker extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);

        return  new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(), hour,min,true);

    }
}
