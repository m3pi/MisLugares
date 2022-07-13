package ogeny.com.mislugaresdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import ogeny.com.mislugaresdemo.models.Lugar;

public class LugarInfoActivity extends AppCompatActivity {
    private long id;
    private Lugar lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar_info);

        Bundle extras = getIntent().getExtras();

        id = extras.getLong("id", -1);
        lugar = ScrollingActivity.iLugar.getLugarById((int) id);

        TextView tevNombre = (TextView) findViewById(R.id.tev_nombre_lugar);
        tevNombre.setText(lugar.getNombre());

        ImageView imvLogoTipo = (ImageView) findViewById(R.id.imv_logo_tipo);
        imvLogoTipo.setImageResource(lugar.getTipoLugar().getRecurso());

        TextView tevTipoLogo = (TextView) findViewById(R.id.tev_tipo_logo);
        tevTipoLogo.setText(lugar.getTipoLugar().getTexto());

        TextView tevDireccion = (TextView) findViewById(R.id.tev_direccion);
        if (lugar.getDireccion().isEmpty()) {
            tevDireccion.setVisibility(View.GONE);
        } else {
            tevDireccion.setVisibility(View.VISIBLE);
            tevDireccion.setText(lugar.getDireccion());
        }

        TextView tevTelefono = (TextView) findViewById(R.id.tev_telefono);
        if (lugar.getTelefono() == 0) {
            // View.GONE no muestra la estas propiedades la vista ni el espacio ocupado
            // findViewById(R.id.tev_telefono).setVisibility(View.GONE);
            tevTelefono.setVisibility(View.GONE);
        } else {
            tevTelefono.setVisibility(View.VISIBLE);
            //TextView tevTelefono = (TextView) findViewById(R.id.tev_telefono);
            tevTelefono.setText(Integer.toString(lugar.getTelefono()));
        }

        TextView tevUrl = (TextView) findViewById(R.id.tev_url);
        if (lugar.getUrl().isEmpty()) {
            tevUrl.setVisibility(View.GONE);
        } else {
            tevUrl.setVisibility(View.VISIBLE);
            tevUrl.setText(lugar.getUrl());
        }

        TextView tevComentario = (TextView) findViewById(R.id.tev_comentario);
        if (lugar.getComentario().isEmpty()) {
            tevComentario.setVisibility(View.VISIBLE);
        } else {
            tevComentario.setVisibility(View.VISIBLE);
            tevComentario.setText(lugar.getComentario());
        }

        TextView tevFecha = (TextView) findViewById(R.id.tev_fecha);
        tevFecha.setText(DateFormat.getDateInstance()
                .format(new Date(lugar.getFecha()))
        );
        TextView tevHora = (TextView) findViewById(R.id.tev_hora);
        tevHora.setText(DateFormat.getTimeInstance()
                .format(new Date(lugar.getFecha()))
        );

        RatingBar rabValoracion = (RatingBar) findViewById(R.id.rab_valoracion);
        rabValoracion.setRating(lugar.getValoracion());
        rabValoracion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                lugar.setValoracion(v);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_lugar_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                return true;
            case R.id.action_como_llegar:
                return true;
            case R.id.action_edit:
                return true;
            case R.id.action_delete:
                eliminarLugar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void eliminarLugar() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Lugar")
                .setMessage("¿Está seguro de eliminar este lugar?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ScrollingActivity.iLugar.borrar((int) id);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}