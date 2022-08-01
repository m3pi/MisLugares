package ogeny.com.mislugaresdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ogeny.com.mislugaresdemo.models.GeoPunto;
import ogeny.com.mislugaresdemo.models.Lugar;

public class MapaActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fgm_mapa);
        mapFragment.getMapAsync(this);
    }

    // https://developers.google.com/maps/documentation/android-sdk/location
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                Toast.makeText(this, "Solictar permiso", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mapa.setMyLocationEnabled(true);
                mapa.getUiSettings().setZoomControlsEnabled(true);
                mapa.getUiSettings().setCompassEnabled(true);
            }
        }

        //if (ScrollingActivity.iLugar.tamanyo() > 0) {
        // db
        if (LugarListActivity.lugarAdapter.getItemCount() > 0) {
            //GeoPunto p = ScrollingActivity.iLugar.getLugarById(0).getPosicion();
            // db
            GeoPunto p = LugarListActivity.lugarAdapter.lugarPosicion(0).getPosicion();
            mapa.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitud(), p.getLongitud()),
                            12));
        }

        //for (int n = 0; n < ScrollingActivity.iLugar.tamanyo(); n++) {
        // db
        for (int n = 0; n < LugarListActivity.lugarAdapter.getItemCount() ; n++) {
            //Lugar lugar = ScrollingActivity.iLugar.getLugarById(n);
            // db
            Lugar lugar = LugarListActivity.lugarAdapter.lugarPosicion(n);
            GeoPunto p = lugar.getPosicion();

            if (p != null && p.getLatitud() != 0) {
                BitmapDrawable iconoDrawable = (BitmapDrawable) ContextCompat.getDrawable(this,
                        lugar.getTipoLugar().getRecurso());
                Bitmap iconoGrande = iconoDrawable.getBitmap();
                Bitmap icono = Bitmap.createScaledBitmap(iconoGrande, iconoGrande.getWidth() / 7,
                        iconoGrande.getHeight() / 7, false);
                mapa.addMarker(new MarkerOptions().position(new LatLng(p.getLatitud(), p.getLongitud()))
                        .title(lugar.getNombre())
                        .snippet(lugar.getDireccion())
                        .icon(BitmapDescriptorFactory.fromBitmap(icono)));
            }
        }

        // añadiendo un escuchador
        mapa.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        //for (int id = 0; id < ScrollingActivity.iLugar.tamanyo(); id++) {
        for (int id = 0; id < LugarListActivity.lugarAdapter.getItemCount(); id++) {
            //if (ScrollingActivity.iLugar.getLugarById(id).getNombre()
            if (LugarListActivity.lugarAdapter.lugarPosicion(id).getNombre()
                    .equals(marker.getTitle())) {
                Intent intent = new Intent(this, LugarInfoActivity.class);
                intent.putExtra("id", (long) id);
                startActivity(intent);
                break;
            }
        }
    }




}