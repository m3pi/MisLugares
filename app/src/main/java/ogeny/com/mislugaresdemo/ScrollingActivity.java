package ogeny.com.mislugaresdemo;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import ogeny.com.mislugaresdemo.databinding.ActivityScrollingBinding;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private Button btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        Button btnSalir = (Button) findViewById(R.id.btn_salir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeApp(view);
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
        if (id == R.id.action_preferences) {
            Toast.makeText(this, "Preferencias selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_about) {
            Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(this, AboutActivity.class));
    }

    public void closeApp(View view) {
        // finish();
        startActivity(new Intent(this, ValidacionActivity.class));
    }
}