package ogeny.com.mislugaresdemo.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ogeny.com.mislugaresdemo.LugarInfoActivity;
import ogeny.com.mislugaresdemo.LugarListActivity;
import ogeny.com.mislugaresdemo.R;
import ogeny.com.mislugaresdemo.interfaces.ILugar;
import ogeny.com.mislugaresdemo.interfaces.LugarService;
import ogeny.com.mislugaresdemo.models.Lugar;

public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.ViewHolder> {
    protected ILugar iLugar; // Lista de lugares
    // protected LayoutInflater layoutInflater; // Crea layouts a partir de archivos xml
    // protected Context context; // Lo necesitamos para el inflater
    protected View.OnClickListener _onClickListener;

    public LugarAdapter(/*Context _context,*/ ILugar _iLugar) {
        // this.context = _context;
        this.iLugar = _iLugar;
        // this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tevNombre, tevDireccion;
        private final ImageView imvFoto;
        private final RatingBar rabValoracion;
        public TextView tevDistancia;

        public ViewHolder(View itemView) {
            super(itemView);

            tevNombre = (TextView) itemView.findViewById(R.id.tev_nombre_row);
            tevDireccion = (TextView) itemView.findViewById(R.id.tev_direccion_row);
            imvFoto = (ImageView) itemView.findViewById(R.id.imv_foto_row);
            rabValoracion = (RatingBar) itemView.findViewById(R.id.rab_valoracion);
            tevDistancia = (TextView) itemView.findViewById(R.id.tev_distancia);
        }
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        //View view = layoutInflater.inflate(R.layout.row_lugar_list, null);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_lugar_list, parent, false);

        // para el vento click
        view.setOnClickListener(_onClickListener);
        return new ViewHolder(view);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Lugar lugar = iLugar.getLugarById(position);
        personalizarVista(holder, lugar);
    }

    @Override
    public int getItemCount() {
        return iLugar.tamanyo();
    }

    // Personalizamos un ViewHolder a partir de un lugar
    protected void personalizarVista(ViewHolder holder, Lugar lugar) {
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

        if (LugarService.posicionActual != null && lugar.getPosicion() != null) {
            int d = (int) LugarService.posicionActual.distancia(lugar.getPosicion());

            if (d < 2000) {
                holder.tevDistancia.setText(d + " m");
            } else {
                holder.tevDistancia.setText(d/1000 + " Km");
            }
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this._onClickListener = onClickListener;
    }
}
