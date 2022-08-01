package ogeny.com.mislugaresdemo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import ogeny.com.mislugaresdemo.interfaces.ILugar;
import ogeny.com.mislugaresdemo.models.Lugar;
import ogeny.com.mislugaresdemo.models.LugaresDB;

public class LugarDbAdapter extends LugarAdapter {
    protected Cursor myCursor;

    public LugarDbAdapter(/*Context _context, */ILugar _iLugar, Cursor _cursor) {
        super(_iLugar);
        this.myCursor = _cursor;
    }

    public Cursor getMyCursor() { return myCursor; }

    public void setMyCursor(Cursor cursor) { this.myCursor = cursor; }

    /* Con el método lugarPosicion() vamos a poder acceder a un lugar indicar su posición en Cursor,
     o lo que es lo mismo, su posición en el listado.  Para ello movemos el cursor a la posición
     indicada y extraemos el lugar en esta posición utilizando un método estático de LugarBD
     */
    public Lugar lugarPosicion(int posicion) {
        myCursor.moveToPosition(posicion);
        return LugaresDB.extraerLugar(myCursor);
    }

    public int idPosicion(int posicion) {
        myCursor.moveToPosition(posicion);
        return myCursor.getInt(0);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // super.onBindViewHolder(holder, position);

        Lugar lugar = lugarPosicion(position);
        if (lugar != null) {
            personalizarVista(holder, lugar);
        } else {
            Log.i("lugar", "El lugar es null");
        }
    }

    @Override
    public int getItemCount() {
        //return super.getItemCount();

        return myCursor.getCount();
    }
}
