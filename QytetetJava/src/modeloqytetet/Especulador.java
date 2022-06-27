/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author antonio
 */
public class Especulador extends Jugador {

    private int fianza;
    static int FactorEspeculador = 2;
    /**
     *
     * @param jugador
     * @param fianza
     */
    protected Especulador(Jugador jugador, int fianza) {
        super(jugador);
        this.fianza = fianza;
    }

    /**
     *
     * @param cantidad
     */
    @Override
    protected void pagarImpuestos(int cantidad) {
        super.pagarImpuestos(cantidad / 2);
    }

    /**
     *
     * @param casilla
     */
    @Override
    protected void irACarcel(Casilla casilla) {
        if (!pagarFianza(fianza)) {
            super.irACarcel(casilla);
        }
    }

    /**
     *
     * @param fianza
     * @return
     */
    @Override
    protected Especulador convertirme(int fianza) {
        return this;
    }

    private boolean pagarFianza(int cantidad) {
        boolean pagada = false;
        if (super.getSaldo() >= cantidad) {
            super.modificarSaldo(-cantidad);
            pagada = true;
        }
        return pagada;
    }

    public static int getFactorEspeculador() {
        return Especulador.FactorEspeculador;
    }

    public int getFianza() {
        return fianza;
    }
    
    

}
