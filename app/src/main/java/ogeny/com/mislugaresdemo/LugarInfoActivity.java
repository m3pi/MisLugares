package ogeny.com.mislugaresdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        lugar = ScrollingActivity.iLugar.elemento((int) id);

        TextView tevNombre = (TextView) findViewById(R.id.tev_nombre_lugar);
        tevNombre.setText(lugar.getNombre());

        ImageView imvLogoTipo = (ImageView) findViewById(R.id.imv_logo_tipo);
        imvLogoTipo.setImageResource(lugar.getTipoLugar().getRecurso());

        TextView tevTipoLogo = (TextView) findViewById(R.id.tev_tipo_logo);
        tevTipoLogo.setText(lugar.getTipoLugar().getTexto());

        TextView tevDireccion = (TextView) findViewById(R.id.tev_direccion);
        tevDireccion.setText(lugar.getDireccion());

        TextView tevTelefono = (TextView) findViewById(R.id.tev_telefono);
        tevTelefono.setText(Integer.toString(lugar.getTelefono()));

        TextView tevUrl = (TextView) findViewById(R.id.tev_url);
        tevUrl.setText(lugar.getUrl());

        TextView tevComentario = (TextView) findViewById(R.id.tev_comentario);
        tevComentario.setText(lugar.getComentario());

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
}