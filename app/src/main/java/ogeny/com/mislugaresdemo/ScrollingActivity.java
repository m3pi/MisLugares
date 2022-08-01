package ogeny.com.mislugaresdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ogeny.com.mislugaresdemo.databinding.ActivityScrollingBinding;
import ogeny.com.mislugaresdemo.interfaces.ILugar;
import ogeny.com.mislugaresdemo.interfaces.LugarService;
import ogeny.com.mislugaresdemo.models.LugaresDB;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private Button btnAbout;
    private Button btnPreferences;
    private Button btnTerminos;
    private Button btnSalir;
    private Button btnMisLugares;
    //public static ILugar iLugar = new LugarService();
    // base de datos
    public static LugaresDB iLugar;

    // procesos
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // base de datos
        iLugar = new LugaresDB(this);

        // ciclo de vida
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        // procesos
        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        mediaPlayer.start();

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btnAbout = (Button) findViewById(R.id.btn_info);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAboutActivity(null);
            }
        });

        btnTerminos = (Button) findViewById(R.id.btn_terminos);
        btnTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTerminoCondiciones();
            }
        });

        btnSalir = (Button) findViewById(R.id.btn_salir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeApp(view);
            }
        });

        btnPreferences = (Button) findViewById(R.id.btn_preferencias);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPreferencesActivity();
            }
        });

        mostrarPreferencias();

        btnMisLugares = (Button) findViewById(R.id.btn_mostrar_lugares);
        btnMisLugares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLugaresListActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.menu_buscar) {
            openLugarInfoActivity();
            return true;
        }

        if (id == R.id.action_preferences) {
            openPreferencesActivity();
            return true;
        }
        if (id == R.id.action_about) {
            openAboutActivity(null);
            return true;
        }
        if (id == R.id.action_mapa) {
            Intent intent = new Intent(this, MapaActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Para acceder a un recurso definido en los ejemplos anteriores desde Java usaremos el siguiente código:
    private void GetRecursoXML() {
        Resources res = getResources();
        Drawable drawable =  res.getDrawable(R.drawable.ic_launcher_background);
        String titulo = res.getString(R.string.app_name);

        /*int color =res.getColor(R.color.verde_opaco);
        float tamanoFuente =res.getDimension(R.dimen.tamano_fuente);
        int maxAsteroides =res.getInteger(R.integer.max_asteroides);
        boolean ilimitados = res.getBoolean(R.bool.misiles_ilimitados);
        String[] diasSemana =res.getStringArray(R.array.dias_semana);
        int[] primos =res.getIntArray(R.array.primos);
        TypedArray asteroides =res.obtainTypedArray(R.array.asteroides);
        Drawable asteroide1 =asteroides.getDrawable(0);*/
    }

    private void openAboutActivity(View view) {
        /*Intent intent = new Intent(this, AboutActivity.class);*/
        /*startActivity(new Intent(this, AboutActivity.class));*/
        startActivity(new Intent(this, IntentEjemploActivity.class));
    }

    private void openPreferencesActivity() {
        /*Intent intent = new Intent(this, AboutActivity.class);*/
        startActivity(new Intent(this, PreferencesActivity.class));
    }

    void openTerminoCondiciones() {
        startActivity(new Intent(this, ValidacionActivity.class));
    }

    void openLugarInfoActivity() {
        /*Intent intent = new Intent(this, LugarInfoActivity.class);
        intent.putExtra("id", (long) 0);
        startActivity(intent);*/

        final EditText edtBuscar = new EditText(this);
        edtBuscar.setText("0");

        // podría ser así
        /*AlertDialog.Builder object = new AlertDialog.Builder(this);
        object.setTitle("Mi título");*/

        new AlertDialog.Builder(this)
                .setTitle("Selección de lugar")
                .setMessage("Indica el id")
                .setView(edtBuscar)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long id = Long.parseLong(edtBuscar.getText().toString());

                        Intent intent = new Intent(ScrollingActivity.this, LugarInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    void openLugaresListActivity() {
        startActivity(new Intent(this, LugarListActivity.class));
    }

    public void closeApp(View view) {
        finish();
    }

    // mostrar las preferencias
    void mostrarPreferencias() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String msg = String.format("Notificaciones %s, máximo alistar: %s",
                preferences.getBoolean("pref_notificaciones", true),
                preferences.getString("pref_maximo_lugares_pagina", "?"));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //region Ciclo de vida

    // Nos indica que la actividad está a punto de ser mostrada al usuario.
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    // Se llama cuando la actividad va a comenzar a interactuar con el usuario.
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
    }

    // Indica que la actividad está a punto de ser lanzada a segundo plano, normalmente porque otra actividad es lanzada
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    }

    // La actividad ya no va a ser visible para el usuario
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        mediaPlayer.pause();
    }

    // Indica que la actividad va a volver a ser representada después de haber pasado por onStop().
    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    // Se llama antes de que la actividad sea totalmente destruida
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    // endregion

    // region Guardar estado de actividad

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mediaPlayer != null) {
            outState.putInt("current_time", mediaPlayer.getCurrentPosition());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && mediaPlayer != null) {
            int currentTime = savedInstanceState.getInt("current_time");
            mediaPlayer.seekTo(currentTime);
        }
    }

    // endregion
}