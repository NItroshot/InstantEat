package com.example.instanteat;

import java.util.ArrayList;

public interface IAbstractFactoryPlato {
    
    /**
     * Crea un objeto de tipo Plato.
     *
     * @return Objeto de tipo Plato.
     */
    public Plato creaPlato(String nombre,double precio,ArrayList<String> ingredientes, boolean esVegano, boolean tieneGluten);

    /**
     * Crea un objeto de tipo Hamburguesa.
     *
     * @return Objeto de tipo hamburguesa.
     */
    public Plato creaHamburguesa(String nombre, int[] opciones);
    
    /**
     * Crea un objeto de tipo Pizza.
     *
     * @return Objeto de tipo Pizza.
     */
    public Plato creaPizza(String nombre, int[] opciones);
    
    /**
     * Crea un objeto de tipo Ensalada.
     *
     * @return Objeto de tipo Ensalada.
     */
    public Plato creaEnsalada(String nombre, int[] opciones);
    
}
