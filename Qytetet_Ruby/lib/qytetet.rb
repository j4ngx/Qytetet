# encoding: UTF-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
require "singleton"
require_relative "sorpresa"
require_relative "tablero"
require_relative "jugador"
require_relative "dado"
require_relative "tipo_sopresa"
module ModeloQytetet
  class Qytetet
    include Singleton

    
    
    @@MAX_JUGADORES = 4
    @@MAX_CARTAS = 10
    @@MAX_CASILLAS = 20
    @@PRECIO_LIBERTAD = 200
    @@SALDO_SALIDA = 1000
    
    attr_accessor :carta_actual, :mazo, :jugadores, :jugador_actual, :tablero, :dado
    def initialize()
      @carta_actual = nil
      @mazo = Array.new
      @jugadores = Array.new
      @jugador_actual = nil
      @tablero = nil
      @dado = Dado.instance
    end
    
    def self.MAX_CASILLAS
      @@MAX_CASILLAS
    end
    
    def self.MAX_JUGADORES
      @@MAX_JUGADORES
    end
    
    def self.MAX_CARTAS
      @@MAX_CARTAS
    end
    
    def self.MAX_LIBERTAD
      @@PRECIO_LIBERTAD
    end
    
    def self.SALDO_SALIDA
      @@SALDO_SALIDA
    end
    
    def siguiente_jugador
      
      posicion_jugador = (@jugadores.rindex(@jugador_actual) + 1) % @jugadores.size
      @jugador_actual = @jugadores[posicion_jugador]
      
    end
    
    def salida_jugadores
      casilla = @tablero.obtener_casilla_numero(0)
      #inicializamos todos los jugadores en la salida
      @jugadores.each do |j|  
        j.casilla_actual = casilla
      end
      #elegimos un jugador al azar de todos los jugadores del array
      @jugador_actual = @jugadores.sample
    end
    
    def propiedades_hipotecadas_jugador(hipotecadas)
      casillas = Array.new
      
      @jugador_actual.propiedades.each do |propiedad|
        if (propiedad.hipotecada == hipotecadas)
          casillas << propiedad.casilla
        end
      end
      
      return casillas
      
    end
    
    def comprar_titulo_propiedad
      return @jugador_actual.comprar_titulo
    end
    
    def edificar_casa(casilla)
      puedo_edificar = false;
      if (casilla.soy_edificable()) 
        se_puede_edificar = casilla.se_puede_edificar_casa(@jugador_actual.factor_especulador)
          if (se_puede_edificar) 
              puedo_edificar = @jugador_actual.puedo_edificar_casa(casilla);
              if (puedo_edificar) 
                  coste_edificar_casa = casilla.edificar_casa
                  @jugador_actual.modificar_saldo(-coste_edificar_casa);

              end
          end
      end
      return puedo_edificar;
    end
    
    
    def hipotecar_propiedad(casilla)
      puedo_hipotecar_propiedad = false;
      if (casilla.soy_edificable()) 
          se_puede_hipotecar = !casilla.esta_hipotecada

          if (se_puede_hipotecar) 
              puedo_hipotecar = @jugador_actual.puedo_hipotecar(casilla);

              if (puedo_hipotecar) 
                  cantidad_recibida = casilla.hipotecar
                  @jugador_actual.modificar_saldo(cantidad_recibida);
                  puedo_hipotecar_propiedad = true;
              end
          end
      end

      return puedo_hipotecar_propiedad;
    end
    
    def aplicar_sorpresa
       tiene_propietario = false
        if (@carta_actual.tipo == TipoSorpresa::PAGARCOBRAR) 
            @jugador_actual.modificar_saldo(@carta_actual.valor);

        elsif (@carta_actual.tipo == TipoSorpresa::IRACASILLA)
            es_carcel = tablero.es_casilla_carcel(@carta_actual.valor);

            if (es_carcel)
                encarcelar_jugador
            else
                nueva_casilla = tablero.obtener_casilla_numero(@carta_actual.valor);
                
                tiene_propietario = @jugador_actual.actualizar_posicion(nueva_casilla);
            end

        elsif (@carta_actual.tipo == TipoSorpresa::PORCASAHOTEL)
            @jugador_actual.pagar_cobrar_por_casa_hotel(@carta_actual.valor);

        elsif (@carta_actual.tipo == TipoSorpresa::PORJUGADOR) 
            @jugadores.each do |jugador|
                if (@jugador_actual != jugador) 
                    jugador.modificar_saldo(@carta_actual.valor);
                    @jugador_actual.modificar_saldo(-@carta_actual.valor);
                end

            end
        elsif (@carta_actual.tipo ==  TipoSorpresa::CONVERTIRME)
          indice = 0
          num_jugadores = @jugadores.length
          
          for i in 0..num_jugadores
            if (@jugador_actual == @jugadores.at(i))
              indice = i
            end
          end

          @jugador_actual = @jugador_actual.convertirme(@carta_actual.valor)
          @jugadores[indice] = @jugador_actual
        end

        if (@carta_actual.tipo == TipoSorpresa::SALIRCARCEL)
          @jugador_actual.carta_libertad = @cata_actual
        else
          @mazo<< (@carta_actual);
        end

      return tiene_propietario;
    end
    
    def encarcelar_jugador
      
      if(!@jugador_actual.tengo_carta_libertad)
        casilla_carcel = @tablero.carcel
        @jugador_actual.ir_a_carcel(casilla_carcel)
      else
        carta = @jugador_actual.devolver_carta_libertad
        @mazo<< carta
      end
    end
    
    def vender_propiedad(casilla)
      puedo_vender = false;
      if casilla.soy_edificable()
       
        puedo_vender = @jugador_actual.puedo_vender_propiedad(casilla);
        if puedo_vender
          @jugador_actual.vender_propiedad(casilla);
        end
        
      end
      return puedo_vender
    end
    
    def intentar_salir_carcel(metodo)
      libre = false
      
      if metodo == ModeloQytetet::MetodoSalirCarcel::TIRANDODADO
        
        valor_dado = dado.tirar
        libre = valor_dado > 5
       
      else metodo == ModeloQytetet::MetodoSalirCarcel::PAGANDOLIBERTAD
      
        libre = @jugador_actual.pagar_libertad(@@PRECIO_LIBERTAD)
        
      end
      
      if libre
        @jugador_actual.encarcelado = false
      end
      return libre
    end
    
    def jugar()
      valor_dado = @dado.tirar
      casilla_posicion = @jugador_actual.casilla_actual
      nueva_casilla = @tablero.obtener_nueva_casilla(casilla_posicion, valor_dado)
      tiene_propietario=@jugador_actual.actualizar_posicion(nueva_casilla)
      
      if !nueva_casilla.soy_edificable
        
        if nueva_casilla.tipo == TipoCasilla::JUEZ
          puts "El jugador ha caido en casilla juez"
          encarcelar_jugador
          
        elsif nueva_casilla.tipo == TipoCasilla::SORPRESA
          @carta_actual = mazo[0]
          @mazo.shift
          
        end
      end
      return tiene_propietario
    end
    
    def edificar_hotel(casilla)
      puedo_edificar = false
      
      if casilla.soy_edificable          
        if casilla.se_puede_edificar_hotel(@jugador_actual.factor_especulador)
          puedo_edificar = @jugador_actual.puedo_edificar_hotel(casilla)
          if puedo_edificar
            coste_edificar_hotel = casilla.edificar_hotel
            @jugador_actual.modificar_saldo(-coste_edificar_hotel)
          end
        end  
      end      
      return puedo_edificar
    end
    
    def cancelar_hipoteca(casilla)
      cancelada = false
      if casilla.soy_edificable()
        esta_hipotecada = casilla.esta_hipotecada
        if esta_hipotecada
          if @jugador_actual.puedo_pagar_hipoteca(casilla)
            precio_cancelacion = casilla.cancelar_hipoteca
            @jugador_actual.modificar_saldo(-precio_cancelacion)
            cancelada = true
          end
        end
      end
      return cancelada
    end
    
    def obtener_ranking
      ranking = Hash.new
      
      @jugadores.each do |jugador|
        capital = jugador.obtener_capital
        ranking[jugador.nombre] = capital
      end
      ranking = Hash[ ranking.sort_by { |key, val| val } ]
      
      
      
    end

    
    def inicializar_cartas_sorpresa
      @mazo << Sorpresa.new("Te hemos pillado copiando en un examen " +
          "¡Debes ir a la carcel!" , 5, 
        TipoSorpresa::IRACASILLA)
      @mazo<< Sorpresa.new("Son las 15:15 y las 15:30 tienes practicas " + 
          "de EC. Deberias ir a por un cafe antes.",12, 
        TipoSorpresa::IRACASILLA)
      @mazo<< Sorpresa.new("Tu profesor no ha venido y no a avisado antes. " +
          "Te toca ir a las mesas rojas",12, 
        TipoSorpresa::IRACASILLA)
                          
      @mazo<< Sorpresa.new("El director de la ETSIIT se ha apiadado de ti." +
          "Sales de la carcel.", 0, 
        TipoSorpresa::SALIRCARCEL)
                          
      @mazo<< Sorpresa.new("Has suspendido PDOO en la convocatoria " +
          "extraordinaria.¡Paga doble matricula!", -130, 
        TipoSorpresa::PAGARCOBRAR)
      @mazo<< Sorpresa.new("Has conseguido una matricula de honor. " +
          "¡ENHORABUENA EMPOLLON!. Te devuelven el dinero", 66, 
        TipoSorpresa::PAGARCOBRAR)
                          
      @mazo<< Sorpresa.new("Empieza un nuevo mes. Tienes que hacerle la " +
          "transferencia al casero.", -180, 
        TipoSorpresa::PORCASAHOTEL)
      @mazo<< Sorpresa.new("El fin de curso a llegado. El casero te devuelve" +
          "la fianza.", 180, 
        TipoSorpresa::PORCASAHOTEL)
      @mazo<< Sorpresa.new("Tus compañeros te ofrecen dinero para que le " +
          "pases las practicas. Todos hacen un bote " + 
          "y te lo dan",25,
        TipoSorpresa::PORJUGADOR)
      @mazo<< Sorpresa.new("Tus compañeros te han pillado copiandote. " + 
          "Todos te piden dinero por su silencio. " + 
          "Te toca pagar.", 15,
        TipoSorpresa::PORJUGADOR)
      @mazo<< Sorpresa.new("Has sido aceptado en la facultad de economia. " +
        "Prearate para ser un gran especulador." , 3000,
        TipoSorpresa::CONVERTIRME)
      @mazo<< Sorpresa.new("Despues de mucho esfuerzo entras a trabajar en banco. " +
        "Hora de especular.", 5000,
        TipoSorpresa::CONVERTIRME)
      @mazo.shuffle!
    end
    
    def inicializar_jugadores(nombres)
      nombres.each do |nombre|
        @jugadores << Jugador.new(nombre)
      end
    end
    
    def inicializar_tablero
      @tablero = Tablero.new
    end
    
    def inicializar_juego(nombres)
      inicializar_tablero()
      inicializar_cartas_sorpresa()
      inicializar_jugadores(nombres)
      salida_jugadores
    end
    
    def to_s
      cadena = ""
      
      if(@carta_actual != nil)
        cadena += "Carta actual: "+@carta_actual.to_s+"\n"
      end
      
      if(@jugador_actual != nil)
        cadena += "Jugador actual: "+@jugador_actual.to_s+"\n"
      end
      
      if(@tablero != nil)
        cadena += "Tablero: "+@tablero.to_s+"\n"
      end
      
      if(!@mazo.empty?)
        cadena += "Cartas: \n"
        @mazo.each do |carta|
          cadena += " " + carta.to_s
        end
      end
      
      if(!@jugadores.empty?)
        cadena += "Jugadores: \n"
        @jugadores.each do |jugador|
          cadena += " " + jugador.to_s
        end
      end
      
      return cadena
    end
    
    private  :inicializar_cartas_sorpresa, :inicializar_jugadores, :inicializar_tablero
    
    
  end
end
