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
public class Calle extends Casilla {

    private int numHoteles;
    private int numCasas;
    private TituloPropiedad titulo;

    public Calle(int numeroCasilla, int coste, TituloPropiedad titulo) {
        super(numeroCasilla, coste, TipoCasilla.CALLE);
        this.titulo = titulo;
        this.numHoteles = 0;
        this.numCasas = 0;
        titulo.setCasilla(this);
    }

    void setNumHoteles(int numHoteles) {
        this.numHoteles = numHoteles;
    }

    void setNumCasas(int numCasas) {
        this.numCasas = numCasas;
    }

    private void setTitulo(TituloPropiedad titulo) {
        this.titulo = titulo;
    }

    int getNumHoteles() {
        return numHoteles;
    }

    int getNumCasas() {
        return numCasas;
    }

    @Override
    public TituloPropiedad getTitulo() {
        return titulo;
    }

    TituloPropiedad asignarPropietario(Jugador jugador) {
        titulo.setPropietario(jugador);
        return titulo;
    }

    int cancelarHipoteca() {
        int valorCancelar = getCosteHipoteca();
        titulo.setHipotecada(false);
        return valorCancelar;
    }

    int calcularValorHipoteca() {
        int hipotecaBase = titulo.getHipotecaBase();
        int valor = hipotecaBase + (int) (numCasas * 0.5 * hipotecaBase + numHoteles * hipotecaBase);

        return valor;
    }

    int cobrarAlquiler() {
        int costeTotal = titulo.getAlquilerBase();
        costeTotal += numCasas * 0.5 + numHoteles * 2;
        titulo.cobrarAlquiler(costeTotal);

        return costeTotal;
    }

    int edificarCasa() {
        setNumCasas(numCasas + 1);
        return titulo.getPrecioEdificar();
    }

    int edificarHotel() {
        setNumHoteles(numHoteles + 1);
        setNumCasas(numCasas - 4);
        return titulo.getPrecioEdificar();
    }

    boolean estaHipoteca() {
        return titulo.getHipotecada();
    }

    int getCosteHipoteca() {
        return (int) (calcularValorHipoteca() * 1.1);
    }

    int getPrecioEdificar() {
        return titulo.getPrecioEdificar();
    }

    int hipotecar() {
        titulo.setHipotecada(true);
        int cantidadRecibida = this.calcularValorHipoteca();

        return cantidadRecibida;
    }

    boolean sePuedeEdificarCasa(int factorEspeculador) {
        return (numCasas < (4 * factorEspeculador));
    }

    boolean sePuedeEdificarHotel(int factorEspeculador) {
        return (numHoteles < (4 * factorEspeculador) && numCasas == 4);
    }

    boolean tengoPropietario() {
        return titulo.tengoPropietario();
    }

    boolean propietarioEncarcelado() {
        return titulo.propietarioEncarcelado();
    }

    int venderTitulo() {
        this.setNumCasas(0);
        this.setNumHoteles(0);
        titulo.setPropietario(null);
        int precioCompra = super.getCoste() + (numCasas + numHoteles) * titulo.getPrecioEdificar();
        int precioVenta = (int) (precioCompra + precioCompra * titulo.getFactorRevalorizacion());
        return precioVenta;
    }

    private TituloPropiedad asignarTituloPropiedad(Jugador jugador) {
        titulo.setPropietario(jugador);
        return titulo;
    }

    @Override
    boolean soyEdificable() {
        return true;
    }

    @Override
    public String toString() {
        /*String stringCasilla = "Casilla{\nNumero Casilla=" + numeroCasilla + "\nTipo="
                + tipo + "\nCoste=" + coste;*/
        String stringCasilla = super.toString();
        stringCasilla += "\nNumero Casas=" + numCasas
                + "\nNumero Hoteles=" + numHoteles + "\n" + titulo.toString();

        stringCasilla += "\n}";

        return stringCasilla;
    }
}
