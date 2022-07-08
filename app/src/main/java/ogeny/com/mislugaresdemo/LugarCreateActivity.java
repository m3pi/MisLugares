package ogeny.com.mislugaresdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LugarCreateActivity extends AppCompatActivity {
    private EditText entrada;
    private TextView salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar_create);

        entrada = (EditText) findViewById(R.id.edt_entrada);
        salida = (TextView) findViewById(R.id.tv_salida);
    }

    public void pulsar(View view) {
        Toast.makeText(this, "Pulsado", Toast.LENGTH_SHORT).show();
    }

    public void pulsarCero(View view) {
        /*entrada.setText(entrada.getText() + "0");*/
//        entrada.setText(entrada.getText() + (String) view.getTag());


        salida.setText(String.valueOf(Float.parseFloat(
                entrada.getText().toString()) * 2.0));


    }
}