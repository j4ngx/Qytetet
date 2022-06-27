# encoding: UTF-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
require_relative "qytetet"
require_relative "tipo_casilla"
require_relative "casilla"
require_relative "calle"

module ModeloQytetet
  class Tablero
    attr_reader :carcel, :casillas
    def initialize
      inicializar
    end
    
    def inicializar
      
      @casillas = Array.new();
      
      #Se crea el objeto carcel
      @carcel = Casilla.crear_no_calle(5,TipoCasilla::CARCEL)
      
      @casillas << Casilla.crear_no_calle(0, TipoCasilla::SALIDA)
      
      @casillas << Calle.new(1, 245,
        TituloPropiedad.crear("Mesas rojas",50,0.11,150,250) )
      
      @casillas << Casilla.crear_no_calle(2, TipoCasilla::SORPRESA)
      
      @casillas << Calle.new(3, 500, 
        TituloPropiedad.crear("Biblioteca", 65, 0.1, 200, 300))
      
      @casillas << Calle.new(4, 350, 
        TituloPropiedad.crear("Cafeteria", 70, 0.12, 350, 350))
      
      #Se añade la carcel en el tablero
      @casillas << @carcel;
      
      @casillas << Calle.new(6, 298, 
        TituloPropiedad.crear("Secretaria", 85, 0.14, 300, 600))
      
      @casillas << Casilla.crear_no_calle(7, TipoCasilla::SORPRESA)
      
      @casillas << Calle.new(8, 700,
        TituloPropiedad.crear("Anfiteatro", 60, 0.16, 400, 275) )
      
      @casillas << Calle.new(9, 500,
        TituloPropiedad.crear("Aulas prefabricadas", 90, 0.11, 425, 725) )
      
      @casillas << Casilla.crear_no_calle(10, TipoCasilla::PARKING)
      
      @casillas << Calle.new(11, 600, 
        TituloPropiedad.crear("Departamento LSI", 100, 0.17, 700, 750))
      
      @casillas << Casilla.crear_no_calle(12, TipoCasilla::SORPRESA)
      
      @casillas << Calle.new(13, 433,
        TituloPropiedad.crear("Departamento DECSAI", 100, 0.16, 750, 400))
      
      @casillas << Calle.new(14, 200, 
        TituloPropiedad.crear("Departamento ATC", 90, 0.15, 625, 575))
      
      @casillas << Casilla.crear_no_calle(15, TipoCasilla::JUEZ)
      
      @casillas << Calle.new(16, 150,
        TituloPropiedad.crear("Aulario", 150, 0.16, 550, 600))
      
      @casillas << Casilla.crear_impuesto(17, 200, TipoCasilla::IMPUESTO)
      
      @casillas << Calle.new(18, 200, 
        TituloPropiedad.crear("Copisteria", 65, 0.18, 475, 625))
      
      @casillas << Calle.new(19, 500, 
        TituloPropiedad.crear("CITIC", 90, 0.2, 480, 450))
        
    end
    
    def obtener_casilla_numero(numero_casilla)
      return @casillas[numero_casilla];
    end
    
    def es_casilla_carcel(num_casilla)
      return (num_casilla == carcel.numeroCasilla)
    end
    
    def obtener_nueva_casilla(casilla, desplazamiento)
      return @casillas[(casilla.numeroCasilla + desplazamiento) % @casillas.size ]
    end 
    
    def to_s
      
      texto = ""
      @casillas.each do |casilla|
        texto += casilla.to_s
      end
      return texto
    end
    
    
    
    private :inicializar
  end
end
