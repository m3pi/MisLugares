package ogeny.com.mislugaresdemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ogeny.com.mislugaresdemo.adapters.LugarAdapter;
import ogeny.com.mislugaresdemo.adapters.LugarDbAdapter;
import ogeny.com.mislugaresdemo.interfaces.ILugar;
import ogeny.com.mislugaresdemo.interfaces.LugarService;
import ogeny.com.mislugaresdemo.models.LugaresDB;

public class LugarListActivity extends AppCompatActivity implements LocationListener {
    private RecyclerView revLugaresList;
    // public LugarAdapter lugarAdapter;
    // base de datos
    // base de datos
    public static LugaresDB iLugar;
    public static LugarDbAdapter lugarAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // localizacion
    private LocationManager locationManager;
    private Location mejorLocation;
    final static int SOLICITUD_PERMISO_LOCALIZACION = 2;
    private static final long DOS_MINUTOS = 2*60*1000;

    // db refrescar la lista con los parámetros de preferencias
    ActivityResultLauncher<Intent> preferencesArl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Integer a = result.getResultCode();

                    Log.i("resultado", a.toString());
                    if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        lugarAdapter.setMyCursor(iLugar.extraerCursor());
                        lugarAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(LugarListActivity.this, "Error preferencias actulizar", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar_list);

        revLugaresList = (RecyclerView) findViewById(R.id.rev_lugares_list);
        //lugarAdapter = new LugarAdapter(this, ScrollingActivity.iLugar);
        //lugarAdapter = new LugarAdapter(ScrollingActivity.iLugar);
        // base de datos
        // base de datos
        iLugar = new LugaresDB(this);
        lugarAdapter = new LugarDbAdapter(iLugar, iLugar.extraerCursor());
        revLugaresList.setAdapter(lugarAdapter);

        layoutManager = new LinearLayoutManager(this);
        revLugaresList.setLayoutManager(layoutManager);

        // para el vento click
        lugarAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LugarListActivity.this, LugarInfoActivity.class);
                intent.putExtra("id", (long) revLugaresList.getChildAdapterPosition(view));
                startActivity(intent);
            }
        });


        // localización
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getUltimaLocalizacion();
    }


    // region Localizacion
    @SuppressLint("MissingPermission")
    private void getUltimaLocalizacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizarMejorLocalizacion(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                actualizarMejorLocalizacion(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            } else {
                Toast.makeText(this, "Desde 1", Toast.LENGTH_SHORT).show();
                LugarInfoActivity.solicitarPermiso(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        "Sin el permiso de localización no se puede mostrar la distancia a los luagres",
                        SOLICITUD_PERMISO_LOCALIZACION,
                        this);
            }
        }  else {
            Toast.makeText(this, "Desde 2", Toast.LENGTH_SHORT).show();
            LugarInfoActivity.solicitarPermiso(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso de localización no se puede mostrar la distancia a los luagres",
                    SOLICITUD_PERMISO_LOCALIZACION,
                    this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUltimaLocalizacion();
                activarProveedores();
                lugarAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activarProveedores();
    }

    @SuppressLint("MissingPermission")
    private void activarProveedores() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        20*1000,
                        5,
                        this);
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        10*1000,
                        10,
                        this);
            }
            else {
                Toast.makeText(this, "Desde 01", Toast.LENGTH_SHORT).show();
                LugarInfoActivity.solicitarPermiso(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        "Sin el permiso de localización no se puede mostrar la distancia a los luagres",
                        SOLICITUD_PERMISO_LOCALIZACION,
                        this);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(LugarService.TAG, "Nueva localización:" + location);

        actualizarMejorLocalizacion(location);
        lugarAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);

        Log.d(LugarService.TAG, "Cambia estado: " + provider);
        activarProveedores();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);

        Log.d(LugarService.TAG, "Se habilita: " + provider);
        activarProveedores();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);

        Log.d(LugarService.TAG, "Se deshabilita: " + provider);
        activarProveedores();
    }

    private void actualizarMejorLocalizacion(Location location) {
        if (location != null &&
                (mejorLocation == null
                        || location.getAccuracy() < 2 * mejorLocation.getAccuracy()
                        || location.getTime() - mejorLocation.getTime() > DOS_MINUTOS
                )) {
            Log.d(LugarService.TAG, "Nueva mejor localización:");
            mejorLocation = location;

            LugarService.posicionActual.setLatitud(location.getLatitude());
            LugarService.posicionActual.setLongitud(location.getLongitude());
        }
    }

    // endregion

    // db refrescar la lista con los parámetros de preferencias
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
            //openLugarInfoActivity();
            return true;
        }

        if (id == R.id.action_preferences) {
            // openPreferencesActivity();
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.action_about) {
            //openAboutActivity(null);
            return true;
        }
        if (id == R.id.action_mapa) {
            Intent intent = new Intent(this, MapaActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarPreferencias(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        preferencesArl.launch(intent);
    }

}