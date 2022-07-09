package ogeny.com.mislugaresdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TerminosActivity extends AppCompatActivity {
    TextView ttvTerminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos);

        Bundle extras = getIntent().getExtras();
        String nombreUsuario = extras.getString("nombre");

        ttvTerminos = (TextView) findViewById(R.id.tv_teminos_user);
        ttvTerminos.setText("Hola " + nombreUsuario + "¿Aceptas las condiciones?");

        Button btnAceptar = (Button) findViewById(R.id.btn_aceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnValor("Aceptado");
            }
        });

        Button btnRechazar = (Button) findViewById(R.id.btn_rechazar);
        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnValor("Rechazado");
            }
        });

    }

    private void returnValor(String valor) {
        Intent intent = new Intent();
        intent.putExtra("resultado", valor);
        setResult(RESULT_OK, intent);
        finish();
    }
}