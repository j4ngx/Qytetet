# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
require_relative "jugador.rb"
module ModeloQytetet
  class Especulador < Jugador

    def initialize(jugador, fianza)
      super(jugador.nombre);
      @encarcelado = jugador.encarcelado
      @saldo = jugador.saldo
      @carta_libertad = jugador.carta_libertad
      @casilla_actual = jugador.casilla_actual
      @propiedades = jugador.propiedades
      @factor_especulacion = 2
      @fianza = fianza
    end
    
    def convertirme(fianza) 
        return self
    end
    
    def pagar_impuestos(cantidad)
      modificar_saldo(-cantidad / 2)
    end
    
    def pagar_fianza(cantidad)
      pagada = false
      
      if(saldo >= cantidad)
        modificar_saldo(-cantidad)
        pagada = true
      end
      
      pagada
    end
    def ir_a_carcel(casilla)
      if(!pagar_fianza(@fianza))
        @casilla_actual = casilla
        @encarcelado = true
      end
    end 
    protected :pagar_impuestos
    private :pagar_fianza
  end
end
