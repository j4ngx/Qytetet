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
public class PruebaQytetet {

    /**
     * @param args the command line arguments
     */

    //private static ArrayList<Sorpresa> mazo = new ArrayList();
    
   // private static Tablero tablero = new Tablero();
    
    /*private static void inicializarSorpresa(){
        //Cartas de ir a casilla
        mazo.add(new Sorpresa("Te hemos pillado copiando en un examen, "
                + "¡Debes ir a la carcel!", tablero.getCarcel().getNumeroCasilla(), 
                TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("Son las 15:15 y las 15:30 tienes practicas de EC"
                + ". Deberias ir a por un cafe antes.",12, 
                TipoSorpresa.IRACASILLA));       
        mazo.add(new Sorpresa("Tu profesor no ha venido y no a avisado antes."
                + " Te toca ir a las mesas rojas",12, 
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
                + "pases las practicas. Todos hacen un bote y te lo dan"
                , 25, TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa("Tus compañeros te han pillado copiandote. "
                + "Todos te piden dinero por su silencio. Te toca pagar.",
                -15, TipoSorpresa.PORJUGADOR));
        
    }*/
    
    /*private static ArrayList<Sorpresa> obtenertSorpresasValorPositivo(){
        ArrayList<Sorpresa> sorpresasPositivas = new ArrayList();
        for(Sorpresa s : mazo){
            if(s.getValor() > 0)
                sorpresasPositivas.add(s);
        }
        return sorpresasPositivas;
    }
    
    private static ArrayList<Sorpresa> obtenerSorpresasTipoIrACasilla(){
        ArrayList<Sorpresa> sorpresasIrACasilla = new ArrayList();
        
        for(Sorpresa s : mazo){
            if(s.getTipo() == TipoSorpresa.IRACASILLA)
                sorpresasIrACasilla.add(s);
        }
        
        return sorpresasIrACasilla;
    }
    
    private static ArrayList<Sorpresa> buscarSorpresasPorTipo(TipoSorpresa tipo){
        ArrayList<Sorpresa> sorpresasTipo = new ArrayList();
        
        for(Sorpresa s : mazo){
            if(s.getTipo() == tipo){
                sorpresasTipo.add(s);
            }
        }
        
        return sorpresasTipo;
    }*/
    
    public static void main(String[] args) {
        // TODO code application logic here

        /*PruebaQytetet.inicializarSorpresa();
        ArrayList<Sorpresa> sorpresasPositivas = PruebaQytetet.obtenertSorpresasValorPositivo();
        ArrayList<Sorpresa> sorpresasTipoIrACasilla = PruebaQytetet.obtenerSorpresasTipoIrACasilla();
        ArrayList<Sorpresa> sorpresasTipoIrACasillaBuscadas = PruebaQytetet.buscarSorpresasPorTipo(TipoSorpresa.PAGARCOBRAR);

        for(Sorpresa s : mazo){
            System.out.println(s.toString());
        }
        for(Sorpresa s : sorpresasPositivas){
            System.out.println(s.toString());
        }
        for(Sorpresa s : sorpresasTipoIrACasilla){
            System.out.println(s.toString());
        }
        for(Sorpresa s : sorpresasTipoIrACasillaBuscadas){
            System.out.println(s.toString());
        }*/
        
        // System.out.println(tablero.toString());
        Qytetet juego=Qytetet.getInstance();
        ArrayList<String> nombres = new ArrayList();
        nombres.add("joselito");
        nombres.add("jose");
        juego.inicializarJuego(nombres);
        String cadena = juego.toString();
        System.out.println(cadena);
    }
}
