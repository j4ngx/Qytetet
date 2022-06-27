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
public class TituloPropiedad {

    private String nombre;
    private boolean hipotecada;
    private int alquilerBase;
    private float factorRevalorizacion;
    private int hipotecaBase;
    private int precioEdificar;
    private Jugador propietario;
    private Casilla casilla;

    public TituloPropiedad(String nombre, boolean hipotecado, int alquilerBase,
            float factorRevalorizacion, int hipotecaBase, int precioEdificar) {
        this.nombre = nombre;
        this.hipotecada = hipotecado;
        this.alquilerBase = alquilerBase;
        this.factorRevalorizacion = factorRevalorizacion;
        this.hipotecaBase = hipotecaBase;
        this.precioEdificar = precioEdificar;
    }

    //Constructor sin necesidad de decir si esta hipotecada, ya que por defecto sera false
    public TituloPropiedad(String nombre, int alquilerBase,
            float factorRevalorizacion, int hipotecaBase, int precioEdificar) {
        this.nombre = nombre;
        this.hipotecada = false;
        this.alquilerBase = alquilerBase;
        this.factorRevalorizacion = factorRevalorizacion;
        this.hipotecaBase = hipotecaBase;
        this.precioEdificar = precioEdificar;
    }

    //Constructor indicando solo el nombre, los demas valores estaran puestos al minimo
    public TituloPropiedad(String nombre) {
        this.nombre = nombre;
        this.hipotecada = false;
        this.alquilerBase = 50;
        this.factorRevalorizacion = (float) 0.1;
        this.hipotecaBase = 150;
        this.precioEdificar = 250;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean getHipotecada() {
        return hipotecada;
    }

    int getAlquilerBase() {
        return alquilerBase;
    }

    float getFactorRevalorizacion() {
        return factorRevalorizacion;
    }

    public int getHipotecaBase() {
        return hipotecaBase;
    }

    public int getPrecioEdificar() {
        return precioEdificar;
    }

    public Casilla getCasilla() {
        return casilla;
    }

    void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }

    /**
     * Añade la casilla a la que le corresponde este titulo de propiedad
     *
     * @param casilla Debe ser de tipo calle
     */
    void setCasilla(Casilla casilla) {
        this.casilla = casilla;
    }

    void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }

    /**
     *
     * @param coste
     */
    void cobrarAlquiler(int coste) {
        propietario.modificarSaldo(coste);
    }

    /**
     * Devuelve si el propietario esta encarcelado
     *
     * @return
     */
    boolean propietarioEncarcelado() {
        return propietario.getEncarcelado();
    }

    /**
     * Devuelve si el titulo de la propiedad tiene dueño
     *
     * @return
     */
    boolean tengoPropietario() {
        boolean propietario = false;
        if(this.propietario != null)
            propietario = true;
        
        return propietario;
    }

    @Override
    public String toString() {
        String cadena = "TituloPropiedad{" + "\n nombre=" + nombre + "\n hipotecado="
                + hipotecada + "\n Factor de Revalorizacion=" + factorRevalorizacion
                + "\n Alquiler Base=" + alquilerBase + "\n Hipoteca Base="
                + hipotecaBase + "\n Precio Edificar= " + precioEdificar + "\n Propietario= ";
        if (propietario != null){
            cadena += propietario.getNombre();
        }else{
            cadena += "Sin propietari";
        }
        cadena += "\n";
        return cadena;
         
    }

}
