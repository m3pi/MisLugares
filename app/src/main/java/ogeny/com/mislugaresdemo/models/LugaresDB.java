package ogeny.com.mislugaresdemo.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import ogeny.com.mislugaresdemo.LugarListActivity;
import ogeny.com.mislugaresdemo.interfaces.ILugar;

public class LugaresDB extends SQLiteOpenHelper implements ILugar {
    Context _context;

    public LugaresDB(Context context) {
        super(context, "Lugares", null, 1);
        this._context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Lugares (" +
                "LugarId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Nombre TEXT, " +
                "Direccion TEXT, " +
                "Longitud REAL, " +
                "Latitud REAL, " +
                "Tipo INTEGER, " +
                "Foto TEXT, " +
                "Telefono INTEGER, " +
                "Url TEXT, " +
                "Comentario TEXT, " +
                "Fecha BIGINT, " +
                "Valoracion REAL " +
                ")"
        );

        sqLiteDatabase.execSQL("INSERT INTO Lugares VALUES ("+
                "null," +
                "'Escuela Politécnica Superior de Gandía'," +
                "'C/ Paranimf, 1 46730 Gandia (SPAIN)'," +
                "-0.166093," +
                "38.995656," +
                TipoLugar.EDUCACION.ordinal() + "," +
                "''," +
                "962849300," +
                "'http://www.epsg.upv.es'," +
                "'Uno de los mejores lugares para formarse.'," +
                System.currentTimeMillis() + "," +
                "3.0" +
                ")"
        );
        sqLiteDatabase.execSQL("INSERT INTO Lugares VALUES (" +
                "null," +
                "'Al de siempre',"+
                "'P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)',"+
                " -0.190642," +
                "38.925857," +
                TipoLugar.BAR.ordinal() + "," +
                "'',"+
                "636472405," +
                "'',"+
                "'No te pierdas el arroz en calabaza.'," +
                System.currentTimeMillis() +"," +
                "3.0)"
        );
        sqLiteDatabase.execSQL("INSERT INTO Lugares VALUES (" +
                "null," +
                "'androidcurso.com',"+
                "'ciberespacio'," +
                "0.0," +
                "0.0," +
                TipoLugar.EDUCACION.ordinal()+"," +
                "'',"+
                "962849300," +
                "'http://androidcurso.com',"+
                "'Amplia tus conocimientos sobre Android.',"+
                System.currentTimeMillis() +"," +
                "5.0)"
        );
        sqLiteDatabase.execSQL("INSERT INTO Lugares VALUES (" +
                "null," +
                "'Barranco del Infierno',"+
                "'Vía Verde del río Serpis. Villalonga (Valencia)'," +
                "-0.295058,"+
                "38.867180,"+
                TipoLugar.NATURALEZA.ordinal() + "," +
                "''," +
                "0,"+
                "'http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-verde-del-rio.html'," +
                "'Espectacular ruta para bici o andar',"+
                System.currentTimeMillis() +"," +
                "4.0)"
        );
        sqLiteDatabase.execSQL("INSERT INTO Lugares VALUES (" +
                "null," +
                "'La Vital',"+
                "'Avda. La Vital,0 46701 Gandia (Valencia)'," +
                "-0.1720092," +
                "38.9705949,"+
                TipoLugar.COMPRAS.ordinal() + "," +
                "''," +
                "962881070,"+
                "'http://www.lavital.es'," +
                "'El típico centro comercial',"+
                System.currentTimeMillis() + "," +
                "2.0)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public static Lugar extraerLugar(Cursor cursor) {
        Lugar lugar = new Lugar();
        lugar.setNombre(cursor.getString(1));
        lugar.setDireccion(cursor.getString(2));
        lugar.setPosicion(new GeoPunto(cursor.getDouble(3),
                cursor.getDouble(4)));
        lugar.setTipoLugar(TipoLugar.values()[cursor.getInt(5)]);
        lugar.setFoto(cursor.getString(6));
        lugar.setTelefono(cursor.getInt(7));
        lugar.setUrl(cursor.getString(8));
        lugar.setComentario(cursor.getString(9));
        lugar.setFecha(cursor.getLong(10));
        lugar.setValoracion(cursor.getFloat(11));
        return lugar;
    }

    public Cursor extraerCursor() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String consulta = "SELECT * FROM Lugares";
        String orden = preferences.getString("pref_orden_lugares", "0");
        String take = preferences.getString("pref_maximo_lugares_pagina", "12");

        switch (orden) {
            case "1":
                consulta = "SELECT * FROM Lugares ORDER BY Valoracion DESC";
                break;
            case "2":
                GeoPunto posicionActual = ILugar.posicionActual;
                double lon = posicionActual.getLongitud();
                double lat = posicionActual.getLatitud();
                consulta = "SELECT * FROM lugares ORDER BY " +
                        "(" + lon + "-longitud)*(" + lon + "-longitud) + " +
                        "(" + lat + "-latitud )*(" + lat + "-latitud )";
                break;
            default:
                break;
        }

        // cantidad de ementos a obtner
        consulta += " LIMIT " + take;

        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(consulta, null);
    }

    @Override
    public Lugar getLugarById(int id) {
        Lugar lugar = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Lugares WHERE LugarId = " + id, null);

        if (cursor != null) {
            // para el cursor pase a la siguiente fila encontrada. Como es la primera llamada estamos hablando del primer elemento
            if (cursor.moveToNext()) {
                lugar = extraerLugar(cursor);
            }
        }

        cursor.close();
        db.close();
        return lugar;
    }

    @Override
    public void anyade(Lugar lugar) {

    }

    @Override
    public int nuevo() {
        int lugarId = -1;
        Lugar lugar = new Lugar();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Lugares (Longitud, Latitud, Tipo, Fecha) " +
                "VALUES (" + lugar.getPosicion().getLongitud() + "," +
                lugar.getPosicion().getLatitud() + "," +
                lugar.getTipoLugar().ordinal() + "," +
                lugar.getFecha() +
                ")");

        Cursor cursor = db.rawQuery("SELECT LugarId FROM Lugares WHERE Fecha = " + lugar.getFecha(), null);

        if (cursor.moveToNext()) {
            lugarId = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return lugarId;
    }

    @Override
    public void borrar(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Lugares WHERE LugarId = " + id);
        db.close();
    }

    @Override
    public int tamanyo() {
        return 0;
    }

    @Override
    public void actualiza(int id, Lugar lugar) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Lugares SET " +
                "nombre = '" + lugar.getNombre() +
                "', direccion = '" + lugar.getDireccion() +
                "', longitud = " + lugar.getPosicion().getLongitud() +
                ", latitud = " + lugar.getPosicion().getLatitud() +
                ", tipo = " + lugar.getTipoLugar().ordinal() +
                ", foto = '" + lugar.getFoto() +
                "', telefono = " + lugar.getTelefono() +
                ", url = '" + lugar.getUrl() +
                "', comentario = '" + lugar.getComentario() +
                "', fecha = " + lugar.getFecha() +
                ", valoracion = " + lugar.getValoracion() +
                " WHERE LugarId = " + id);
        db.close();
    }

    // mostrar las preferencias
    private String getPreferenciaOrden() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        return preferences.getString("pref_orden_lugares", "0");
    }
}
