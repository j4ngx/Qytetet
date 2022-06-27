/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.Random;

/**
 *
 * @author antonio
 */
public class Dado {
    
    private static final Dado instance = new Dado();
    
    private Dado(){};
    
    
    public static Dado getInstance(){
        return instance;
    }
    
    /**
     * Se tira el dado para que de un numero aleatorio de 1 - 6
     * 
     * @return numero de la tirada del dado
     */
    int Tirar(){
        
        Random aleatorio = new Random(System.currentTimeMillis());
        int intAleatorio = aleatorio.nextInt(6) + 1;
        
        return intAleatorio;
        
        /*return 1;*/
    }
}
