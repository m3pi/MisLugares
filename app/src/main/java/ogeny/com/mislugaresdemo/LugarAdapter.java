package ogeny.com.mislugaresdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ogeny.com.mislugaresdemo.interfaces.ILugar;
import ogeny.com.mislugaresdemo.models.Lugar;

public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.ViewHolder> {
    protected ILugar iLugar; // Lista de lugares
    protected LayoutInflater layoutInflater; // Crea layouts a partir de archivos xml
    protected Context context; // Lo necesitamos para el inflater

    public LugarAdapter(Context _context, ILugar _iLugar) {
        this.context = _context;
        this.iLugar = _iLugar;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tevNombre, tevDireccion;
        private final ImageView imvFoto;
        private final RatingBar rabValoracion;

        public ViewHolder(View itemView) {
            super(itemView);

            tevNombre = (TextView) itemView.findViewById(R.id.tev_nombre_row);
            tevDireccion = (TextView) itemView.findViewById(R.id.tev_direccion_row);
            imvFoto = (ImageView) itemView.findViewById(R.id.imv_foto_row);
            rabValoracion = (RatingBar) itemView.findViewById(R.id.rab_valoracion);
        }
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View view = layoutInflater.inflate(R.layout.row_lugar_list, null);
        return new ViewHolder(view);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lugar lugar = iLugar.getLugarById(position);
        personalizarVista(holder, lugar);
    }

    @Override
    public int getItemCount() {
        return iLugar.tamanyo();
    }

    // Personalizamos un ViewHolder a partir de un lugar
    private void personalizarVista(ViewHolder holder, Lugar lugar) {
        holder.tevNombre.setText(lugar.getNombre());
        holder.tevDireccion.setText(lugar.getDireccion());

        int id = R.drawable.otros;
        switch (lugar.getTipoLugar()) {
            case RESTAURANTE:id = R.drawable.restaurante; break;
            case BAR:        id = R.drawable.bar;         break;
            case COPAS:      id = R.drawable.copas;       break;
            case ESPECTACULO:id = R.drawable.espectaculos; break;
            case HOTEL:      id = R.drawable.hotel;       break;
            case COMPRAS:    id = R.drawable.compras;     break;
            case EDUCACION:  id = R.drawable.educacion;   break;
            case DEPORTE:    id = R.drawable.deporte;     break;
            case NATURALEZA: id = R.drawable.naturaleza;  break;
            case GASOLINERA: id = R.drawable.gasolinera;  break;
        }
        holder.imvFoto.setImageResource(id);
        holder.imvFoto.setScaleType(ImageView.ScaleType.FIT_END);
        holder.rabValoracion.setRating(lugar.getValoracion());
    }
}
