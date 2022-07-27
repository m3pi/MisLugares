package ogeny.com.mislugaresdemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import ogeny.com.mislugaresdemo.models.Lugar;

public class LugarInfoActivity extends AppCompatActivity {
    private long id;
    private Lugar lugar;
    // con ActivityResultLauncher no es necesario los códigos
    // final static int RESULTADO_EDITAR = 1;

    // añadiendo imágenes desde la galeria
    private ImageView fotoLugar;
    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;

    // añadiendo imágenes desde la cámara
    private Uri uriFotoLugar;
//    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Log.i("resultado", Integer.toString(result.getResultCode()));
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                // otra opcion es obtener el valor de un extra
                                Log.i("llamada", "ingreso");
                                updateLayout();
                                findViewById(R.id.info_lugar).invalidate();
                            }
                        }
                    });

    ActivityResultLauncher<Intent> galeriaArl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        lugar.setFoto(result.getData().getDataString());
                        insertarFotoLugar(fotoLugar, lugar.getFoto());
                    } else {
                        Toast.makeText(LugarInfoActivity.this, "Foto no cargada", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    ActivityResultLauncher<Intent> camaraArl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Integer a = result.getResultCode();

                    Log.i("resultado", a.toString());
                    if (result.getResultCode() == Activity.RESULT_OK
                    && lugar != null
                    && uriFotoLugar != null) {
                        lugar.setFoto(uriFotoLugar.toString());
                        insertarFotoLugar(fotoLugar, lugar.getFoto());
                    } else {
                        Toast.makeText(LugarInfoActivity.this, "Error en captura", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar_info);

        Bundle extras = getIntent().getExtras();

        id = extras.getLong("id", -1);

        fotoLugar = (ImageView) findViewById(R.id.imv_foto);

        updateLayout();

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
                //Uso de intenciones para compartir
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, String.format("%s - %s", lugar.getNombre(), lugar.getUrl()));
                startActivity(intent);
                return true;
            case R.id.action_como_llegar:
                openMapa();
                return true;
            case R.id.action_edit:
                openLugarEeditActivity();
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

    private void openLugarEeditActivity() {
        Intent intent = new Intent(this, LugarCreateActivity.class);
        intent.putExtra("id", id);
        /*startActivity(intent);*/
        activityResultLauncher.launch(intent);
    }

    private void updateLayout() {
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

        // insewrtar la foto tomada
        insertarFotoLugar(fotoLugar, lugar.getFoto());
    }

    private void openMapa() {
        Uri uri;
        double lat = lugar.getPosicion().getLatitud();
        double lng = lugar.getPosicion().getLongitud();

        if (lat != 0 || lng != 0) {
            uri = Uri.parse("geo:" + lat + "," + lng);
        } else {
            uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openLlamar(View v) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.getTelefono())));
    }

    public void openWeb(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lugar.getUrl())));
    }

    // region Añadiendo imágenes
    public void openGaleria(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeriaArl.launch(intent);
    }

    protected void insertarFotoLugar(ImageView imageView, String uri) {
        if (uri != null && !uri.isEmpty() && !uri.equals("null")) {
            if (uri.startsWith("content://ogeny.com.mislugaresdemo/") ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.
                            READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                imageView.setImageBitmap(reduceBitmap(this, uri, 1024,   1024));
            } else {
                imageView.setImageURI(Uri.parse(uri));
            }

        } else {
            imageView.setImageBitmap(null);
        }
    }

    public static Bitmap reduceBitmap(Context contexto, String uri,
                                      int maxAncho, int maxAlto) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso no encontrado",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    public void tomarFoto(View view) {
        /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*uriFotoLugar = Uri.fromFile(
                new File(Environment.getExternalStorageDirectory() + File.separator
                + "img_" + (System.currentTimeMillis() / 1000) + ".jpg")
        );*/
/*
        File file = new File(Environment.getExternalStorageDirectory() + File.separator
                + "img_" + (System.currentTimeMillis() / 1000) + ".jpg");
        uriFotoLugar = FileProvider.getUriForFile(this, AUTHORITY, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoLugar);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip=
                    ClipData.newUri(getContentResolver(), "A photo", uriFotoLugar);

            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        else {
//            List<ResolveInfo> resInfoList=
//                    getPackageManager()
//                            .queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
//
//            for (ResolveInfo resolveInfo : resInfoList) {
//                String packageName = resolveInfo.activityInfo.packageName;
//                grantUriPermission(packageName, outputUri,
//                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            }
        }*/

        File file = null;
        try {
            File rutaImagenes = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            file = File.createTempFile("img_" + (System.currentTimeMillis() / 1000),
                    ".jpg",
                    rutaImagenes
            );
        } catch (IOException e) {
//            e.printStackTrace();
            Toast.makeText(this, "Error al crear imagen temporal", Toast.LENGTH_SHORT).show();
        }

        try {
            if (file != null) {
                uriFotoLugar = FileProvider.getUriForFile(this, AUTHORITY, file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoLugar);
                camaraArl.launch(intent);
            }
        }
        catch (ActivityNotFoundException e) {
            Log.e("mompi", e.toString());
            Toast.makeText(this, "R.string.msg_no_camera", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // endregion

}