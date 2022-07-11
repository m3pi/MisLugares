package ogeny.com.mislugaresdemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        String resultado = data.getExtras().getString("resultado");
                        tviValidacion.setText(resultado);
                    }
                }
            });

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

    private void  openValidacion0() {
        Intent intent = new Intent(this, TerminosActivity.class);
        intent.putExtra("nombre", edtNombre.getText().toString());
        startActivityForResult(intent, 1234);
    }

    private void  openValidacion() {
        Intent intent = new Intent(this, TerminosActivity.class);
        intent.putExtra("nombre", edtNombre.getText().toString());
        someActivityResultLauncher.launch(intent);
    }

/*
    public void openSomeActivityForResult() {
        Intent intent = new Intent(this, SomeActivity.class);
        someActivityResultLauncher.launch(intent);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        doSomeOperations();
                    }
                }
            });

    The new way (Kotlin):

    fun openSomeActivityForResult() {
        val intent = Intent(this, SomeActivity::class.java)
        resultLauncher.launch(intent)
    }

    var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            doSomeOperations()
        }
    }*/
}