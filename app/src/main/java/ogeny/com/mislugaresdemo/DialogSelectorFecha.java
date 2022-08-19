package ogeny.com.mislugaresdemo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DialogSelectorFecha extends DialogFragment {
    private DatePickerDialog.OnDateSetListener escuchador;

    public void  setOnDateSetListener(DatePickerDialog.OnDateSetListener _escuchador) {
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

        int anyo = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        return  new DatePickerDialog(getActivity(), escuchador, anyo, mes, dia);
    }
}
