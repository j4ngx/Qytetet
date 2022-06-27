/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author jo_se
 */
public abstract class Casilla {

    private int numeroCasilla;
    private int coste;   
    private TipoCasilla tipo;

    //construtor para casillas que no son calle
    public Casilla(int numeroCasilla, TipoCasilla tipo) {
        this.numeroCasilla = numeroCasilla;
        this.tipo = tipo;
        this.coste = 0;
    }


    
    //Contructor para casillas que son impuesto
    public Casilla(int numeroCasilla, int coste, TipoCasilla tipo) {
        this.numeroCasilla = numeroCasilla;
        this.coste = coste;
        this.tipo = tipo;
        
    }
    
    
    public int getNumeroCasilla() {
        return numeroCasilla;
    }

    int getCoste() {
        return coste;
    }


    public TipoCasilla getTipo() {
        return tipo;
    }


    @Override
    public String toString() {
        String stringCasilla = "Casilla{\nNumero Casilla=" + numeroCasilla + "\nTipo="
                + tipo + "\nCoste=" + coste;
        /*if (tipo == TipoCasilla.CALLE) {
            stringCasilla += "\nNumero Casas=" + numCasas
                    + "\nNumero Hoteles=" + numHoteles + "\n" + titulo.toString();
        }*/

        stringCasilla += "\n}";

        return stringCasilla;
    }

    int precioTotalComprar() {
        throw new UnsupportedOperationException("sin implementar");
    }

    boolean calcularHipoteca() {
        throw new UnsupportedOperationException("sin implementar");
    }

    abstract boolean soyEdificable(); 
    public abstract TituloPropiedad getTitulo();
   
}
