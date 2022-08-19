package ogeny.com.mislugaresdemo;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.format.DateFormat;

// import java.text.DateFormat;
import java.util.Calendar;

public class DialogSelectorHora extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener escuchador;

    public void  setOnTimeSetListener(TimePickerDialog.OnTimeSetListener _escuchador) {
        this.escuchador = _escuchador;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // return super.onCreateDialog(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        Bundle args = this.getArguments();

        if (args != null) {
            long fecha = args.getLong("fecha");
            calendar.setTimeInMillis(fecha);
        }

        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        return  new TimePickerDialog(getActivity(), escuchador, hora, minuto, DateFormat.is24HourFormat(getActivity()));
    }
}
