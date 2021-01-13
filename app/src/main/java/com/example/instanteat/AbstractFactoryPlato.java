package com.example.instanteat;
import java.util.ArrayList;

public class AbstractFactoryPlato implements IAbstractFactoryPlato{
    
    private PlatoFactory factoriaPlato = new PlatoFactory();
    private HamburguesaFactory factoriaHamburguesa = new HamburguesaFactory();
    private PizzaFactory factoriaPizza = new PizzaFactory();
    private EnsaladaFactory factoriaEnsalada = new EnsaladaFactory();
    
    /**
     * Crea un objeto de tipo Plato.
     *
     * @return Objeto de tipo Plato.
     */
    @Override
    public Plato creaPlato(String nombre, ArrayList<String> ingredientes, boolean esVegano, boolean tieneGluten){
        return factoriaPlato.crearPlato(nombre, ingredientes, esVegano, tieneGluten);
    }

    /**
     * Crea un objeto de tipo Hamburguesa.
     *
     * @return Objeto de tipo hamburguesa.
     */
    @Override
    public Plato creaHamburguesa(String nombre,int[] opciones){
        return factoriaHamburguesa.crearHamburguesa(nombre, opciones);
    }
    
    /**
     * Crea un objeto de tipo Hamburguesa.
     *
     * @return Objeto de tipo hamburguesa.
     */
    @Override
    public Plato creaPizza(String nombre, int[] opciones){
        return factoriaPizza.crearPizza(nombre, opciones);
    }
    
    /**
     * Crea un objeto de tipo Hamburguesa.
     *
     * @return Objeto de tipo hamburguesa.
     */
    /*@Override
    public Plato creaEnsalada(String nombre, int[] opciones){
        
    }*/
    
    
}