package ogeny.com.mislugaresdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ValidacionActivity extends AppCompatActivity {
    EditText edtNombre;
    TextView tviValidacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacion);

        edtNombre = (EditText) findViewById(R.id.et_nombre_val);
        Button btnValidar = (Button) findViewById(R.id.btn_validar);
        tviValidacion = (TextView) findViewById(R.id.et_resultado);

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openValidacion();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            String resultado = data.getExtras().getString("resultado");
            tviValidacion.setText(resultado);
        }
    }

    private void  openValidacion() {
        Intent intent = new Intent(this, TerminosActivity.class);
        intent.putExtra("nombre", edtNombre.getText().toString());
        startActivityForResult(intent, 1234);
    }
}