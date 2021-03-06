package ogeny.com.mislugaresdemo.interfaces;

import ogeny.com.mislugaresdemo.models.GeoPunto;
import ogeny.com.mislugaresdemo.models.Lugar;

public interface ILugar {
    final static String TAG = "MisLugares";
    static GeoPunto posicionActual = new GeoPunto(0,0);

    Lugar getLugarById(int id); //Devuelve el elemento dado su id
    void anyade(Lugar lugar); //Añade el elemento indicado
    int nuevo(); //Añade un elemento en blanco y devuelve su id
    void borrar(int id); //Elimina el elemento con el id indicado
    int tamanyo(); //Devuelve el número de elementos
    void actualiza(int id, Lugar lugar); //Reemplaza un elemento
}
