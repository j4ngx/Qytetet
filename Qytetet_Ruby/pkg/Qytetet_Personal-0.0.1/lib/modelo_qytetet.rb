# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
#encoding: utf-8

module ModeloQytetet
  class PruebaQytetet
require_relative "qytetet"
require_relative "sorpresa"
require_relative "tipo_sorpresa"
require_relative "titulo_propiedad"
require_relative "tipo_casilla"
require_relative "casilla"
require_relative "tablero"
require_relative "jugador"
require_relative "dado"
require_relative "estado_juego"
require_relative "metodo_salir_carcel"

  attr_accessor:objetoQytetet
  attr_accessor:nombres
  attr_accessor:prueba
  
    
  def initialize
    @nombres=Array.new
  
    @nombres<<"Primero"
    @nombres<<"Segundo"
    @nombres<<"Tercero"
    @nombres<<"Cuarto"
    
    @objetoQytetet = Qytetet.instance
  end
    
  def main
    @objetoQytetet.inicializar_juego(@nombres)
    puts @objetoQytetet.get_jugador_actual.to_s
    @objetoQytetet.mover(1)
    @objetoQytetet.comprar_titulo_propiedad
    @objetoQytetet.edificar_casa(@objetoQytetet.obtener_casilla_jugador_actual.get_numero_casilla)
    @objetoQytetet.edificar_casa(@objetoQytetet.obtener_casilla_jugador_actual.get_numero_casilla)
    puts @objetoQytetet.get_jugador_actual.to_s
    @objetoQytetet.siguiente_jugador
    @objetoQytetet.mover(3)
    @objetoQytetet.actuar_si_en_casilla_no_edificable
    for i in 0..3
    puts @objetoQytetet.get_jugador_actual.to_s
    @objetoQytetet.siguiente_jugador
    end
  end
 
end

juego = PruebaQytetet.new

juego.main
  
end