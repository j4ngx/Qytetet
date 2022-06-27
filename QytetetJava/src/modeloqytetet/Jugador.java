/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;

/**
 *
 * @author antonio
 */
public class Jugador {
    
    private boolean encarcelado;
    private String nombre;
    private int saldo;
    private Sorpresa cartaLibertad;
    private ArrayList<TituloPropiedad> propiedades;
    private Casilla casillaActual;
    static int FactorEspeculador = 1;
    
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.encarcelado = false;
        this.saldo = 7500;
        propiedades = new ArrayList();
        //casillaActual = Qytetet.getInstance().getTablero().obtenerCasillaNumero(0);
    }

    protected Jugador(Jugador jugador) {
        this.nombre = jugador.nombre;
        this.encarcelado = jugador.encarcelado;
        this.saldo = jugador.saldo;
        this.cartaLibertad = jugador.cartaLibertad;
        this.propiedades = jugador.propiedades;
        this.casillaActual = jugador.casillaActual;
    }

    //Consultores basicos
    public boolean getEncarcelado() {
        return encarcelado;
    }

    public static int getFactorEspeculador() {
        return Jugador.FactorEspeculador;
    }
    
    
    
    public String getNombre() {
        return nombre;
    }
    
    public int getSaldo() {
        return saldo;
    }
    
    public Sorpresa getCartaLibertad() {
        return cartaLibertad;
    }

    /**
     * Devuelve una lista con todas las propiedades que tiene el jugador
     *
     * @return arraylist de las propiedades
     */
    public ArrayList<TituloPropiedad> getPropiedades() {
        return propiedades;
    }

    /**
     * Retorna el valor de la casilla en la que se encuentra el jugador
     *
     * @return casilla actual del jugador
     */
    public Casilla getCasillaActual() {
        return casillaActual;
    }

    /**
     * Pone valor a cartaLiberta
     *
     * @param carta Debe ser una carta de tipo SalirCarcel
     */
    void setCartaLibertad(Sorpresa carta) {
        this.cartaLibertad = carta;
    }

    /**
     * Mueve el jugador a otra casilla
     *
     * @param casilla Valor de la casilla a la que se movera
     */
    void setCasillaActual(Casilla casilla) {
        this.casillaActual = casilla;
    }
    
    void setEncarcelado(boolean encarcelado) {
        this.encarcelado = encarcelado;
    }

    /**
     * Devuelve si el jugador tiene alguna propiedad
     *
     * @return
     */
    public boolean tengoPropiedades() {
        return !propiedades.isEmpty();
    }

    /**
     * Actualiza la posicion de la casilla del jugador
     *
     * @param casilla
     * @return
     */
    protected boolean actualizarPosicion(Casilla casilla) {
        if (casilla.getNumeroCasilla() < casillaActual.getNumeroCasilla()) {
            modificarSaldo(Qytetet.SALDO_SALIDA);
        }
        
        boolean tienePropietario = false;
        setCasillaActual(casilla);

        //Miramos si es de tipo calle
        if (casilla.soyEdificable()) {

            //Si tiene propietario y si no esta encarcelado se cobrara el alquiler
            tienePropietario = ((Calle)casilla).tengoPropietario();
            if (tienePropietario) {
                boolean estaEncarcelado = ((Calle)casilla).propietarioEncarcelado();
                if (!estaEncarcelado) {
                    int costeAlquiler = ((Calle)casilla).cobrarAlquiler();
                    modificarSaldo(-costeAlquiler);
                }
            }
        }
        
        if (casilla.getTipo() == TipoCasilla.IMPUESTO) {
            int coste = casilla.getCoste();
            pagarImpuestos(coste);
        }
        return tienePropietario;
    }

    /**
     * Compra un titulo de la propiedad
     *
     * @return
     */
    boolean comprarTitulo() {
        boolean comprar = false;
        if (casillaActual.soyEdificable()) {
            boolean tengoPropietario = ((Calle)casillaActual).tengoPropietario();
            
            if (!tengoPropietario) {
                int costeCompra = casillaActual.getCoste();
                
                if (costeCompra <= saldo) {
                    TituloPropiedad titulo = ((Calle)casillaActual).asignarPropietario(this);
                    propiedades.add(titulo);
                    this.modificarSaldo(-costeCompra);
                    comprar = true;
                    //System.out.print(saldo + "aaaaa\n" + -costeCompra);
                }
                
            }
        }
        
        return comprar;
    }

    /**
     * Elimina la carta libertad si no era nula y la devuelve
     *
     * @return devuelve la cartaLibertad o null en caso de que el jugador no la
     * tenga.
     */
    Sorpresa devolverCartaLibertad() {
        Sorpresa libertad = null;
        if (cartaLibertad != null) {
            libertad = cartaLibertad;
            cartaLibertad = null;
        }
        
        return libertad;
    }

    /**
     *
     * @param casilla
     */
    void irACarcel(Casilla casilla) {
        setCasillaActual(casilla);
        setEncarcelado(true);
    }

    /**
     * añade un nuevo saldo restando si es negativo o sumado si es positivo
     *
     * @param Cantidad el sado se sumara o restara este valor
     */
    void modificarSaldo(int cantidad) {
        saldo += cantidad;
    }

    /**
     *
     * @return
     */
    int obtenerCapital() {
        int capital = saldo;
        for (TituloPropiedad t : propiedades) {
            Casilla casilla = t.getCasilla();
            capital += ((Calle)casilla).getCoste();
            int cant = ((Calle)casilla).getNumHoteles() + ((Calle)casilla).getNumCasas();
            capital += cant * t.getPrecioEdificar();
            if (t.getHipotecada()) {
                capital -= t.getHipotecaBase();
            }
        }
        return capital;
    }

    /**
     *
     * @param hipotecadas
     * @return
     */
    ArrayList<TituloPropiedad> obtenerPropiedadesHipotecadas(boolean hipotecadas) {
        
        ArrayList<TituloPropiedad> titulosDevolver = new ArrayList();
        for (TituloPropiedad t : propiedades) {
            if (t.getHipotecada() && hipotecadas) {
                titulosDevolver.add(t);
            } else if (!t.getHipotecada() && !hipotecadas) {
                titulosDevolver.add(t);
            }
            
        }
        return titulosDevolver;
    }

    /**
     *
     * @param cantidad
     */
    void pagarCobrarPorCasaYHotel(int cantidad) {
        int numeroTotal = cuantasCasasHotelTengo();
        modificarSaldo(numeroTotal * cantidad);
    }

    /**
     *
     * @param cantidad
     */
    protected void pagarImpuestos(int cantidad) {
        modificarSaldo(-cantidad);
    }
    
    protected Especulador convertirme(int fianza) {
        Especulador especulador = new Especulador(this, fianza);
        return especulador;
    }

    /**
     *
     * @param cantidad
     * @return
     */
    boolean pagarLibertad(int cantidad) {
        boolean tengoSaldo = tengoSaldo(cantidad);
        if (tengoSaldo) {
            modificarSaldo(cantidad);
        }
        
        return tengoSaldo;
    }

    /**
     *
     * @param casilla
     * @return
     */
    boolean puedoEdificarCasa(Casilla casilla) {
        boolean puedoEficiar = false;
        if (esDeMiPropiedad(casilla)) {
            int precio = ((Calle)casilla).getPrecioEdificar();
            puedoEficiar = tengoSaldo(precio);
        }
        
        return puedoEficiar;
    }

    /**
     *
     * @param casilla
     * @return
     */
    boolean puedoEdificarHotel(Casilla casilla) {
        boolean puedoEficiar = false;
        if (esDeMiPropiedad(casilla)) {
            int precio = ((Calle)casilla).getPrecioEdificar();
            puedoEficiar = tengoSaldo(precio);
        }
        
        return puedoEficiar;
    }

    /**
     *
     * @param casilla
     * @return true si se puede hipotecar
     */
    boolean puedoHipotecar(Casilla casilla) {
        return this.esDeMiPropiedad(casilla);
        
    }

    /**
     *
     * @param casilla
     * @return
     */
    boolean puedoPagarHipoteca(Casilla casilla) {
        boolean puedoPagar = false;
        if (((Calle)casilla).getCosteHipoteca() <= saldo && this.esDeMiPropiedad(casilla)) {
            puedoPagar = true;
        }
        return puedoPagar;
    }

    /**
     * Comprueba si la casilla es de mi propiedad y si no esta hipoteca
     *
     * @param casilla
     * @return true si es de mi propiedad y no esta hipotecada fase en otro caso
     */
    boolean puedoVenderPropiedad(Casilla casilla) {
        boolean vender = false;
        if (this.esDeMiPropiedad(casilla) && !casilla.getTitulo().getHipotecada()) {
            vender = true;
        }
        
        return vender;
    }

    /**
     * Comrpueba si se tiene carta libertad
     *
     * @return bool true si se tiene carta false si no se tiene.
     */
    boolean tengoCartaLiberdad() {
        boolean libertad = false;
        if (cartaLibertad != null) {
            libertad = true;
        }
        
        return libertad;
    }

    /**
     *
     * @param casilla
     */
    void venderPropiedad(Casilla casilla) {
        int precioVenta = ((Calle)casilla).venderTitulo();
        modificarSaldo(precioVenta);
        eliminarDeMisPropiedades(casilla);
    }

    /**
     *
     * @return
     */
    private int cuantasCasasHotelTengo() {
        int cantidad = 0;
        for (TituloPropiedad t : propiedades) {
            Casilla casilla = t.getCasilla();
            cantidad += ((Calle)casilla).getNumCasas() + ((Calle)casilla).getNumHoteles();
        }
        return cantidad;
    }

    /**
     *
     * @param casilla
     */
    private void eliminarDeMisPropiedades(Casilla casilla) {
        boolean eliminada = false;
        for (int i = 0; i < propiedades.size() && !eliminada; i++) {
            TituloPropiedad t = propiedades.get(i);
            if (t.getCasilla().getNumeroCasilla() == casilla.getNumeroCasilla()) {
                propiedades.remove(t);                
                eliminada = true;
            }
        }
    }

    /**
     *
     * @param casilla
     * @return
     */
    private boolean esDeMiPropiedad(Casilla casilla) {
        boolean f = false;
        for (TituloPropiedad t : propiedades) {
            if (t.getCasilla().getNumeroCasilla() == casilla.getNumeroCasilla()) {
                f = true;
            }
        }
        return f;
    }

    /**
     *
     * @param cantidad
     * @return
     */
    private boolean tengoSaldo(int cantidad) {
        return (saldo >= cantidad);
    }

    /**
     * Devuelve la informacion del jugador en forma de string
     *
     * @return
     */
    @Override
    public String toString() {
        
        String texto = "Nombre: " + nombre + "\nSaldo: " + saldo;
        if (cartaLibertad != null) {
            texto += "\nCarta de libertad: " + cartaLibertad.toString();
        }
        if (casillaActual != null) {
            texto += "\nCasilla actual: " + casillaActual.toString();
        }
        if (propiedades != null) {
            texto += "\nNumero de propiedades: " + propiedades.size();
            if (!propiedades.isEmpty()) {
                texto += "\nPropiedades:{";
                
                for (TituloPropiedad t : propiedades) {
                    texto += " " + t.toString();
                }
            }
        }
        texto += "\n";
        return texto;
    }
}
