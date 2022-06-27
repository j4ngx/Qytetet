# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
require_relative 'vista_textual_qytetet'
require_relative 'qytetet'
require_relative 'metodo_salir_carcel'
require_relative 'tipo_casilla'
require_relative 'jugador'


module InterfazTextualQytetet
  class Controlador_qytetet
    
    
    def initialize
      @vista = VistaTextualQytetet.new
      @juego = ModeloQytetet::Qytetet.instance;
    end
    def incializar_juego()
        
      nombres = Array.new
      nombres = @vista.obtener_nombre_jugadores
      #nombres << "Juan"
      #nombres << "Pepe"
      @juego.inicializar_juego(nombres)
      @jugador = @juego.jugador_actual
      @casilla = @jugador.casilla_actual
      @vista.mostrar(@juego.to_s)
      @vista.pausa
    end
    def gestion_inmobiliarioa(opcion)
      casillas = Array.new
      case opcion

      when 1
        @vista.mostrar("Todas tus propiedades: \n")
        casillas = @juego.propiedades_hipotecadas_jugador(true)
        casillas += @juego.propiedades_hipotecadas_jugador(false)
        casilla_elegida = elegir_propiedad(casillas)
        if(@juego.edificar_casa(casilla_elegida))
          @vista.mostrar("Se ha edificado una casa")
        else
          @vista.mostrar("No se ha podido edificar casa")
        end
      when 2
        @vista.mostrar("Todas tus propiedades: \n")
        casillas = @juego.propiedades_hipotecadas_jugador(true)
        casillas += @juego.propiedades_hipotecadas_jugador(false)
        casilla_elegida = elegir_propiedad(casillas)
        if(@juego.edificar_hotel(casilla_elegida))
          @vista.mostrar("Se ha edificado un hotel")
        else
          @vista.mostrar("No se ha podido edificar hotel")
        end
      when 3
        casillas = @juego.propiedades_hipotecadas_jugador(false)
        if (casillas.any?)
          @vista.mostrar("Tus propiedades no hipotecadas: \n")
          casilla_elegida = elegir_propiedad(casillas)
          @juego.vender_propiedad(casilla_elegida)
        else
          @vista.mostrar("No tiene propiedades para realizar esta opicon \n")
        end

      when 4
        casillas = @juego.propiedades_hipotecadas_jugador(false)
        if (casillas.any?)
          @vista.mostrar("Tus propiedades no hipotecadas: \n")
          casilla_elegida = elegir_propiedad(casillas)
          @juego.hipotecar_propiedad(casilla_elegida)
        else
          @vista.mostrar("No tiene propiedades para realizar esta opicon \n")
        end
      when 5
        casillas = @juego.propiedades_hipotecadas_jugador(true)
        if (casillas.any?)
          @vista.mostrar("Tus propiedades no hipotecadas: \n")
          casilla_elegida = elegir_propiedad(casillas)
          @juego.cancelar_hipoteca(casilla_elegida)
        else
          @vista.mostrar("No tiene propiedades para realizar esta opicon \n")
        end
      end
      @vista.mostrar("El saldo actual del jugador" + @jugador.saldo.to_s)
    end
    
    def casilla_tipo_calle(no_tiene_propietario)
      @vista.mostrar("Casilla actual" + @casilla.to_s)
      
      if(!no_tiene_propietario)
        @vista.mostrar("La casilla no tiene propietario")
        comprar = @vista.elegir_quiero_comprar
        if(comprar)
          if(@juego.comprar_titulo_propiedad)
            @vista.mostrar("Se ha comprado la propiedad su saldo actual es " + 
              @jugador.saldo.to_s);
            
          else
            @vista.mostrar("No se ha podido comprar la propiedad");                        
          end
        end
      else
        @vista.mostrar("La casilla actual tiene propietario, tiene que pagar " + @casilla.precio_alquiler.to_s + "\nSu saldo actual es de " + @jugador.saldo.to_s)
      end
    end
    
    def casilla_tipo_sorpresa(no_tiene_propietario)
      @vista.mostrar('Ha caido en la carta sorpresa\n')
      @vista.mostrar("La carte que le ha tocado es: " + @juego.carta_actual.to_s() + "\n")
      no_tiene_propietario = @juego.aplicar_sorpresa
      @casilla = @jugador.casilla_actual
      if( !@jugador.encarcelado && !bancarrota &&@casilla.tipo == ModeloQytetet::TipoCasilla::CALLE && !no_tiene_propietario)
        comprar = @vista.elegir_quiero_comprar
        if(comprar)
          if(@juego.comprar_titulo_propiedad)
            @vista.mostrar("Se ha comprado la propiedad");                            
          else
            @vista.mostrar("No se ha comprado la propiedad");                            
          end
        end
      end
    end
    
    def bancarrota
      return @jugador.saldo <= 0
    end
    
    def esta_encarcelado
      @vista.mostrar("Esta en la casilla carcel")
      libre = false
      opcion_carcel = @vista.menu_salir_carcel
      if( opcion_carcel == 1)
        libre = @juego.intentar_salir_carcel(ModeloQytetet::MetodoSalirCarcel::PAGANDOLIBERTAD)
      else
        libre = @juego.intentar_salir_carcel(ModeloQytetet::MetodoSalirCarcel::TIRANDODADO)
      end
      if(libre)
        @vista.mostrar("Has sido liberado de la carcel")
      end
      return libre
    end
    
    def esta_libre
      @vista.pausa
      no_tiene_propietario = @juego.jugar
      num_dado = @jugador.casilla_actual.numeroCasilla - @casilla.numeroCasilla
      if(num_dado < 0)
        num_dado = num_dado + 20
      end
      if(!@jugador.encarcelado)
        @vista.mostrar("Saco un " + num_dado.to_s + " al tirar el dado")
      end
      @casilla = @jugador.casilla_actual
            
      if(!bancarrota)
        if(!@jugador.encarcelado)
          
          if(@casilla.tipo == ModeloQytetet::TipoCasilla::CALLE)
            casilla_tipo_calle(no_tiene_propietario)
          end

          if(@casilla.tipo == ModeloQytetet::TipoCasilla::SORPRESA)
            casilla_tipo_sorpresa(no_tiene_propietario)
          end
              
          if(!@jugador.encarcelado && !bancarrota() && @jugador.tengo_propiedades)
            opcion = 1
            while(opcion !=0 && @jugador.tengo_propiedades)
              @vista.mostrar("\nPropiedades del jugador")
              @jugador.propiedades.each do |p|
                @vista.mostrar("Nombre: "+ p.nombre + " Numero de casas: " + p.casilla.numCasas.to_s+ " Numero de hoteles: " + p.casilla.numHoteles.to_s + " Hipotecada: " + p.hipotecada.to_s)
                @vista.mostrar("")
              end
              opcion = @vista.menu_gestion_inmobiliaria
                  
              gestion_inmobiliarioa(opcion)
                 
            end
          end
          if (!@jugador.tengo_propiedades && !@jugador.encarcelado && !bancarrota()) 
            @vista.mostrar("Usted no tiene propiedades para gestionar\n");
          end
        else
          @vista.mostrar("El jugador ha sido encarcelado")
        end
      end
    end
    
    def desarrollo_juego
      while(!bancarrota)
        @vista.mostrar("Turno de: "+@jugador.nombre)
        @vista.mostrar("Saldo de: "+@jugador.saldo.to_s)        
        @vista.mostrar("Casilla actual numero: " + @casilla.numeroCasilla.to_s)
        libre = true
        if(@jugador.encarcelado)
          libre = esta_encarcelado
        end

        if(libre)
          esta_libre
        end
        if (!bancarrota()) 
          @jugador = @juego.siguiente_jugador
          @casilla = @jugador.casilla_actual
        end       
      end
      @vista.mostrar(@juego.obtener_ranking)
    end
    ## --------------------el siguiente método va en ControladorQytetet
    def elegir_propiedad(propiedades) # lista de propiedades a elegir
      @vista.mostrar("\tCasilla\tTitulo");
        
      listaPropiedades= Array.new
      for prop in propiedades  # crea una lista de strings con numeros y nombres de propiedades
        propString= prop.numeroCasilla.to_s+' '+prop.titulo.nombre; 
        listaPropiedades<<propString
      end
      seleccion=@vista.menu_elegir_propiedad(listaPropiedades)  # elige de esa lista del menu
      propiedades.at(seleccion)
    end
    
    def self.main
      controlador = Controlador_qytetet.new
      controlador.incializar_juego
      controlador.desarrollo_juego
    end
    
    Controlador_qytetet.main
  end

end