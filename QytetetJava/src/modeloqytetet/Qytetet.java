/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jo_se
 */
public class Qytetet {

    private static final Qytetet instance = new Qytetet();
    public static final int MAX_JUGADORES = 4;

    static final int MAX_CARTAS = 10;
    static final int MAX_CASILLA = 20;
    static final int PREICIO_LIBERTAD = 200;
    static final int SALDO_SALIDA = 1000;

    private Sorpresa cartaActual;
    private ArrayList<Sorpresa> mazo;
    private Jugador jugadorActual;
    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private Qytetet() {
    }

    public static Qytetet getInstance() {
        return instance;
    }

    public boolean aplicarSorpresa() {
        boolean tienePropietario = false;
        if (null != cartaActual.getTipo()) {
            switch (cartaActual.getTipo()) {
                case PAGARCOBRAR:
                    jugadorActual.modificarSaldo(cartaActual.getValor());
                    break;
                case IRACASILLA:
                    boolean esCarcel = tablero.esCasillaCarcel(cartaActual.getValor());
                    if (esCarcel) {
                        encarcelarJugador();
                    } else {
                        Casilla nuevaCasilla = tablero.obtenerCasillaNumero(cartaActual.getValor());
                        tienePropietario = jugadorActual.actualizarPosicion(nuevaCasilla);
                    }
                    break;
                case PORCASAHOTEL:
                    jugadorActual.pagarCobrarPorCasaYHotel(cartaActual.getValor());
                    break;
                case PORJUGADOR:
                    for (Jugador jugador : jugadores) {
                        if (jugadorActual != jugador) {
                            jugador.modificarSaldo(cartaActual.getValor());
                            jugadorActual.modificarSaldo(-cartaActual.getValor());
                        }

                    }
                    break;
                case CONVERTIRME:
                    int indice = jugadores.indexOf(jugadorActual);
                    jugadores.set(indice, jugadorActual.convertirme(cartaActual.getValor()));
                    jugadorActual = jugadores.get(indice);
                    break;
                default:
                    break;
            }
        }

        if (cartaActual.getTipo() == TipoSorpresa.SALIRCARCEL) {
            jugadorActual.setCartaLibertad(cartaActual);
        } else {
            mazo.add(cartaActual);
        }
        
        cartaActual = null;
        
        return tienePropietario;
    }

    public boolean cancelarHipoteca(Casilla casilla) {
        boolean cancelada = false;
        if (casilla.soyEdificable()) {
            boolean estaHipotecada = ((Calle) casilla).estaHipoteca();
            if (estaHipotecada) {
                if (jugadorActual.puedoPagarHipoteca(casilla)) {
                    int precioCacelacion = ((Calle) casilla).cancelarHipoteca();
                    jugadorActual.modificarSaldo(-precioCacelacion);
                    cancelada = true;
                }
            }
        }
        return cancelada;
    }

    public boolean comprarTituloPropiedad() {
        return jugadorActual.comprarTitulo();
    }

    public boolean edificarCasa(Casilla casilla) {
        boolean puedoEdificar = false;
        if (casilla.soyEdificable()) {
            boolean sePuedeEdificar;
            
            if(jugadorActual instanceof Especulador)
                sePuedeEdificar = ((Calle) casilla).sePuedeEdificarCasa(Especulador.getFactorEspeculador());
            else
                sePuedeEdificar = ((Calle) casilla).sePuedeEdificarCasa(Jugador.getFactorEspeculador());
            
            if (sePuedeEdificar) {
                puedoEdificar = jugadorActual.puedoEdificarCasa(casilla);
                if (puedoEdificar) {
                    int costeEdificarCasa = ((Calle) casilla).edificarCasa();
                    jugadorActual.modificarSaldo(-costeEdificarCasa);

                }
            }
        }
        return puedoEdificar;
    }

    public boolean edificarHotel(Casilla casilla) {
        boolean puedoEdificar = false;
        if (casilla.soyEdificable()) {
            boolean sePuedeEdificar;
            
            if(jugadorActual instanceof Especulador)
                sePuedeEdificar = ((Calle) casilla).sePuedeEdificarHotel(Especulador.getFactorEspeculador());
            else
                sePuedeEdificar = ((Calle) casilla).sePuedeEdificarHotel(Jugador.getFactorEspeculador());

            if (sePuedeEdificar) {
                puedoEdificar = jugadorActual.puedoEdificarHotel(casilla);
                if (puedoEdificar) {
                    int costeEdificarHotel = ((Calle) casilla).edificarHotel();
                    jugadorActual.modificarSaldo(-costeEdificarHotel);

                }
            }
        }
        return puedoEdificar;
    }

    public Sorpresa getCartaActual() {
        return cartaActual;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public boolean hipotecarPropiedad(Casilla casilla) {
        boolean puedoHipotecarPropiedad = false;
        if (casilla.soyEdificable()) {
            boolean sePuedeHipotecar = !((Calle) casilla).estaHipoteca();

            if (sePuedeHipotecar) {
                boolean puedoHipotecar = jugadorActual.puedoHipotecar(casilla);

                if (puedoHipotecar) {
                    int cantidadRecibida = ((Calle) casilla).hipotecar();
                    jugadorActual.modificarSaldo(cantidadRecibida);
                    puedoHipotecarPropiedad = true;
                }
            }
        }

        return puedoHipotecarPropiedad;
    }

    public void inicializarJuego(ArrayList<String> nombres) {
        //throw new UnsupportedOperationException("sin implementar");
        inicializarJugadores(nombres);
        inicializarTablero();
        inicializarCartasSorpresa();

        salidaJugadores();

    }

    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo) {
        boolean libre = false;
        GUIQytetet.Dado dado = GUIQytetet.Dado.getInstance();
        if (metodo == MetodoSalirCarcel.TIRANDODADO) {
            int valorDado = dado.nextNumber();
            libre = valorDado > 5;
        } else if (metodo == MetodoSalirCarcel.PAGANDOLIBERTAD) {
            boolean tengoSaldo = jugadorActual.pagarLibertad(-PREICIO_LIBERTAD);
            libre = tengoSaldo;
        }

        if (libre) {
            jugadorActual.setEncarcelado(false);
        }

        return libre;
    }

    public boolean jugar() {
        GUIQytetet.Dado dado = GUIQytetet.Dado.getInstance();
        int valorDado = dado.nextNumber();
        Casilla casilllaPosicion = jugadorActual.getCasillaActual();
        Casilla nuevaCasilla = tablero.obtenerNuevaCasilla(casilllaPosicion, valorDado);
        boolean tienePropietario = jugadorActual.actualizarPosicion(nuevaCasilla);

        if (!nuevaCasilla.soyEdificable()) {
            if (nuevaCasilla.getTipo() == TipoCasilla.JUEZ) {
                encarcelarJugador();
            } else if (nuevaCasilla.getTipo() == TipoCasilla.SORPRESA) {
                cartaActual = mazo.get(0);
                mazo.remove(0);
            }

        }

        return tienePropietario;
    }

    /**
     *
     * @return
     */
    public Hashtable obtenerRanking() {
        Hashtable<String, Integer> ranking = new Hashtable<String, Integer>();

        for (Jugador jugador : jugadores) {
            int capital = jugador.obtenerCapital();
            ranking.put(jugador.getNombre(), capital);
        }

        return ranking;
    }

    public ArrayList propiedadesHipotecadasJugador(boolean hipotecadas) {
        ArrayList<Casilla> casillas = new ArrayList();
        ArrayList<TituloPropiedad> titulos = jugadorActual.getPropiedades();
        for (int i = 0; i < titulos.size(); i++) {

            if (hipotecadas == titulos.get(i).getHipotecada()) {
                casillas.add(titulos.get(i).getCasilla());
            }
        }

        return casillas;

    }

    /**
     * Cambia el jugador actual al siguiente jugador
     *
     *
     */
    public Jugador siguienteJugador() {
        int posicion_jugador_actual = jugadores.indexOf(jugadorActual);
        jugadorActual = jugadores.get((posicion_jugador_actual + 1) % jugadores.size());
        return jugadorActual;
    }

    /**
     *
     * @param casilla
     * @return
     */
    public boolean venderPropiedad(Casilla casilla) {
        boolean puedoVender = false;
        if (casilla.soyEdificable()) {
            puedoVender = jugadorActual.puedoVenderPropiedad(casilla);
            if (puedoVender) {
                jugadorActual.venderPropiedad(casilla);
            }
        }
        return puedoVender;
    }

    /**
     *
     * @return
     */
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    private void encarcelarJugador() {
        if (!jugadorActual.tengoCartaLiberdad()) {
            Casilla casillaCarcel = tablero.getCarcel();
            jugadorActual.irACarcel(casillaCarcel);
        } else {
            Sorpresa cartaCarcel = jugadorActual.devolverCartaLibertad();
            mazo.add(cartaCarcel);
        }
    }

    /**
     *
     */
    private void salidaJugadores() {
        Casilla salida = tablero.obtenerCasillaNumero(0);
        for (int i = 0; i < jugadores.size(); i++) {
            jugadores.get(i).setCasillaActual(salida);
        }
        jugadorActual = jugadores.get((int) (Math.random() * jugadores.size()));
    }

    private void inicializarCartasSorpresa() {
        mazo = new ArrayList(MAX_CARTAS);
        mazo.add(new Sorpresa("Despues de mucho esfuerzo entras a trabajar en banco. "
                + "Hora de especular.",
                5000, TipoSorpresa.CONVERTIRME));
        //Cartas de ir a casilla
        mazo.add(new Sorpresa("Te hemos pillado copiando en un examen, "
                + "¡Debes ir a la carcel!", tablero.getCarcel().getNumeroCasilla(),
                TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("Son las 15:15 y las 15:30 tienes practicas de EC"
                + ". Deberias ir a por un cafe antes.", 12,
                TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("Tu profesor no ha venido y no a avisado antes."
                + " Te toca ir a las mesas rojas", 12,
                TipoSorpresa.IRACASILLA));
        //Carta salir de la carcel
        mazo.add(new Sorpresa("El director de la ETSIIT se ha apiadado de ti. "
                + "Sales de la carcel.", 0, TipoSorpresa.SALIRCARCEL));
        //Carta de pagar o cobrar
        mazo.add(new Sorpresa("Has suspendido PDOO en la convocatoria "
                + "extraordinaria.¡Paga doble matricula!", -130,
                TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("Has conseguido una matricula de honor. "
                + "¡ENHORABUENA EMPOLLON!. Te devuelven el dinero", 66,
                TipoSorpresa.PAGARCOBRAR));
        //Cartas por casa u hotel
        mazo.add(new Sorpresa("Empieza un nuevo mes. Tienes que hacerle la "
                + "transferencia al casero.", -180, TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa("El fin de curso a llegado. El casero te devuelve"
                + " la fianza.", 180, TipoSorpresa.PORCASAHOTEL));
        //Carta por jugador
        mazo.add(new Sorpresa("Tus compañeros te ofrecen dinero para que le "
                + "pases las practicas. Todos hacen un bote y te lo dan", 25, TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa("Tus compañeros te han pillado copiandote. "
                + "Todos te piden dinero por su silencio. Te toca pagar.",
                -15, TipoSorpresa.PORJUGADOR));

        mazo.add(new Sorpresa("Has sido aceptado en la facultad de economia. "
                + "Prearate para ser un gran especulador.",
                3000, TipoSorpresa.CONVERTIRME));

        Collections.shuffle(mazo);
    }

    private void inicializarTablero() {
        tablero = new Tablero();
    }

    private void inicializarJugadores(ArrayList<String> nombres) {

        jugadores = new ArrayList(nombres.size());

        for (String nombre : nombres) {

            jugadores.add(new Jugador(nombre));

        }
    }

    @Override
    public String toString() {
        String cadena = "Turno actual: ";

        if (jugadorActual != null) {
            cadena += jugadorActual.toString();
        } else {
            cadena += "El turno actual esta sin definir aun.\n";
        }

        cadena += "Jugadores: ";
        if (jugadores != null && !jugadores.isEmpty()) {
            for (Jugador j : jugadores) {
                cadena += j.toString() + "\n";
            }
        } else {
            cadena += "No hay jugadores \n";
        }

        cadena += "Tablero: ";
        if (tablero != null) {
            cadena += tablero.toString();
        } else {
            cadena += "Tablero no esta iniciado. \n";
        }

        cadena += "Mazo: ";
        if (mazo != null && !mazo.isEmpty()) {
            for (Sorpresa s : mazo) {
                cadena += s.toString() + "\n";
            }
        } else {
            cadena += "No hay jugadores \n";
        }

        return cadena;
    }

    private void elif() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
