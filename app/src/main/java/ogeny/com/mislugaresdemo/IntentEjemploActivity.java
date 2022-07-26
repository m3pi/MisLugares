package ogeny.com.mislugaresdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class IntentEjemploActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_ejemplo);
        Button btnAbriPaginaWeb = (Button) findViewById(R.id.button);
        Button btnLlamadaTelefono = (Button) findViewById(R.id.button2);
        Button btnGoogleMaps = (Button) findViewById(R.id.button3);
        Button btntomarFoto = (Button) findViewById(R.id.button4);
        Button btnEnviarCorreo = (Button) findViewById(R.id.button5);

        btnAbriPaginaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPaginaWeb(view);
            }
        });

        btnLlamadaTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamadarTelefono(view);
            }
        });

        btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMaps(view);
            }
        });

        btntomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(view);
            }
        });

        btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarCorreo(view);
            }
        });
    }

    public void abrirPaginaWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.androidcurso.com/"));
        startActivity(intent);
    }
    public void llamadarTelefono(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:962849347"));
        startActivity(intent);
    }
    public void googleMaps(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:41.656313,-0.877351"));
        startActivity(intent);
    }
    public void tomarFoto(View view) {
        // Intent intent = new Intent("MediaStore.ACTION_IMAGE_CAPTURE");
        // "android.media.action.IMAGE_CAPTURE"
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }
    public void enviarCorreo(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "asunto");
        intent.putExtra(Intent.EXTRA_TEXT, "texto del correo");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jtomas@upv.es"});
        startActivity(intent);
    }
}