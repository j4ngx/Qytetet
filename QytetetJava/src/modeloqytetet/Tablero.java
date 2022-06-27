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
public class Tablero {

    //Atributos privados
    private ArrayList<Casilla> casillas;
    private Casilla carcel;

    /**
     * Constructor sin argumentos
     */
    public Tablero() {
        inicializar();
    }

    /**
     * Se añade las todas las casillas al tablero
     */
    private void inicializar() {

        casillas = new ArrayList(Qytetet.MAX_CASILLA);

        int numCasillaCarcel = 5;

        casillas.add(new OtraCasilla(0, TipoCasilla.SALIDA));

        casillas.add(new Calle(1, 998,
                new TituloPropiedad("Mesas rojas", 50, (float) 0.11, 150, 250)));

        casillas.add(new OtraCasilla(2, TipoCasilla.SORPRESA));

        casillas.add(new Calle(3, 500,
                new TituloPropiedad("Biblioteca", 65, (float) 0.1, 200, 300)));

        casillas.add(new Calle(4, 350,
                new TituloPropiedad("Cafeteria", 70, (float) 0.12, 350, 350)));

        casillas.add(new OtraCasilla(numCasillaCarcel, TipoCasilla.CARCEL));

        casillas.add(new Calle(6, 289,
                new TituloPropiedad("Secretaria", 85, (float) 0.14, 300, 600)));

        casillas.add(new OtraCasilla(7, TipoCasilla.SORPRESA));

        casillas.add(new Calle(8, 700,
                new TituloPropiedad("Anfiteatro", 60, (float) 0.16, 400, 275)));

        casillas.add(new Calle(9, 500,
                new TituloPropiedad("Aulas pefabricadas", 90, (float) 0.11, 425, 725)));

        casillas.add(new OtraCasilla(10, TipoCasilla.PARKING));

        casillas.add(new Calle(11, 600,
                new TituloPropiedad("Departamento LSI", 100, (float) 0.17, 700, 750)));

        casillas.add(new OtraCasilla(12, TipoCasilla.SORPRESA));

        casillas.add(new Calle(13, 433,
                new TituloPropiedad("Departamento DECSAI", 100, (float) 0.16, 750, 400)));

        casillas.add(new Calle(14, 200,
                new TituloPropiedad("Departamento ATC", 90, (float) 0.15, 625, 575)));

        casillas.add(new OtraCasilla(15, TipoCasilla.JUEZ));

        casillas.add(new Calle(16, 300,
                new TituloPropiedad("Aulario", 150, (float) 0.16, 550, 600)));

        casillas.add(new OtraCasilla(17, 200, TipoCasilla.IMPUESTO));

        casillas.add(new Calle(18, 200,
                new TituloPropiedad("Copisteria", 65, (float) 0.18, 475, 625)));

        casillas.add(new Calle(19, 500,
                new TituloPropiedad("CITIC", 90, (float) 0.2, 480, 450)));

        carcel = casillas.get(numCasillaCarcel);

    }

    /**
     * Devuelve la casilla donde se encuentra la carcel
     *
     * @return el objeto casilla de la carcel
     */
    Casilla getCarcel() {
        return carcel;
    }

    boolean esCasillaCarcel(int numeroCasilla) {
        return carcel.getNumeroCasilla() == numeroCasilla;
    }
    
     /**
     * Devuelve la casilla que se le pide
     *
     * @return el objeto casilla en la posicion numeroCasilla
     *
     */
    Casilla obtenerCasillaNumero(int numeroCasilla) {
        return casillas.get(numeroCasilla);
    }

    /**
     *
     * @param casilla
     * @param desplazamiento
     * @return
     */
    Casilla obtenerNuevaCasilla(Casilla casilla, int desplazamiento) {
        return casillas.get((casilla.getNumeroCasilla() + desplazamiento)%casillas.size());
    }

    @Override
    public String toString() {
        String salida = "";
        for (int x = 0; x < casillas.size(); x++) {
            salida += casillas.get(x).toString() + "\n";
        }
        return salida;
    }

}
