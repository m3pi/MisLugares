package ogeny.com.mislugaresdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ogeny.com.mislugaresdemo.models.Lugar;
import ogeny.com.mislugaresdemo.models.TipoLugar;

public class LugarCreateActivity extends AppCompatActivity {
    /*private EditText entrada;
    private TextView salida;*/
    private long id;
    private Lugar lugar;
    private EditText edtNombre;
    private Spinner sprTipo;
    private EditText edtTelefono;
    private EditText edtDireccion;
    private EditText edtComentario;
    private EditText edtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar_create);

        /*entrada = (EditText) findViewById(R.id.edt_entrada);
        salida = (TextView) findViewById(R.id.tv_salida);*/

        Bundle extras = getIntent().getExtras();

        id = extras.getLong("id", -1);
        //lugar = ScrollingActivity.iLugar.getLugarById((int) id);
        //db
        lugar = LugarListActivity.lugarAdapter.lugarPosicion((int) id);

        edtNombre = (EditText) findViewById(R.id.edt_nombre);
        edtNombre.setText(lugar.getNombre());

        /*Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);*/


        sprTipo = (Spinner) findViewById(R.id.spr_tipo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                TipoLugar.getNombres());
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sprTipo.setAdapter(adapter);
        sprTipo.setSelection(lugar.getTipoLugar().ordinal());

        edtTelefono = (EditText) findViewById(R.id.edt_telefono);
        edtTelefono.setText(Integer.toString(lugar.getTelefono()));
        edtDireccion = (EditText) findViewById(R.id.edt_direccion);
        edtDireccion.setText(lugar.getDireccion());
        edtComentario = (EditText) findViewById(R.id.edt_comentario);
        edtComentario.setText(lugar.getComentario());
        edtUrl = (EditText) findViewById(R.id.edt_url);
        edtUrl.setText(lugar.getUrl());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lugar_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            case R.id.action_guardar:
                updateLugar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void pulsar(View view) {
        Toast.makeText(this, "Pulsado", Toast.LENGTH_SHORT).show();
    }

    public void pulsarCero(View view) {
        *//*entrada.setText(entrada.getText() + "0");*//*
//        entrada.setText(entrada.getText() + (String) view.getTag());


        salida.setText(String.valueOf(Float.parseFloat(
                entrada.getText().toString()) * 2.0));


    }*/

    private void updateLugar() {
        lugar.setNombre(edtNombre.getText().toString());
        lugar.setTipoLugar(TipoLugar.values()[sprTipo.getSelectedItemPosition()]);
        lugar.setDireccion(edtDireccion.getText().toString());
        lugar.setTelefono(Integer.parseInt(edtTelefono.getText().toString()));
        lugar.setUrl(edtUrl.getText().toString());
        lugar.setComentario(edtComentario.getText().toString());

        ScrollingActivity.iLugar.actualiza((int) id, lugar);

        setResult(RESULT_OK);
        finish();
    }

}