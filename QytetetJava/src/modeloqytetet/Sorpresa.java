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
public class Sorpresa {
    //Atributos
    private String texto;
    private TipoSorpresa tipo;
    private int valor;
    
    /**
     * /
     * @param texto texto de la sopresa
     * @param valor 
     * @param tipo El tipo de sopresa que es
     */
    public Sorpresa(String texto, int valor, TipoSorpresa tipo){
        this.texto = texto;
        this.valor = valor;
        this.tipo = tipo;
    }

    //Metodos getter y setters

    String getTexto() {
        return texto;
    }

    void setTexto(String texto) {
        this.texto = texto;
    }

    TipoSorpresa getTipo() {
        return tipo;
    }

    void setTipo(TipoSorpresa tipo) {
        this.tipo = tipo;
    }

    int getValor() {
        return valor;
    }

    void setValor(int valor) {
        this.valor = valor;
    }
    
    @Override
    public String toString(){
        return "Sopresa{" + "texto=" + texto +", valor=" 
                + Integer.toString(valor) +", tipo=" + tipo + "}";
    }
    
}
