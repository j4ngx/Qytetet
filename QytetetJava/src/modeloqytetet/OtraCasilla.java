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
public class OtraCasilla extends Casilla{
    
    public OtraCasilla (int numeroCasilla, TipoCasilla tipo) {
        super(numeroCasilla, tipo);
    }
    
    public OtraCasilla (int numeroCasilla, int coste, TipoCasilla tipo) {
        super(numeroCasilla, coste, tipo);
    }
    @Override
    boolean soyEdificable() {
        return false;
    }
    @Override
    public TituloPropiedad getTitulo() {
        throw new UnsupportedOperationException("sin implementar");
    }
   
   
}
