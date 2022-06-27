# encoding: UTF-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  class Casilla
    
    #Atributos de la clase
    attr_reader :numeroCasilla, :coste, :tipo
    
    def initialize(numeroCasilla, coste, tipo)
      @numeroCasilla = numeroCasilla
      @coste = coste
      @tipo = tipo

    end
    

    def self.crear_impuesto(numeroCasilla, coste, tipo)
      self.new(numeroCasilla, coste,tipo)
    end
    
    #construtor para casillas que no son calle
    def self.crear_no_calle(numeroCasilla, tipo)
      self.new(numeroCasilla, nil, tipo)
    end
    
    def soy_edificable()
      return (@tipo == TipoCasilla::CALLE)
    end
    
    #Devuelve un string con la informacion de la casill
    def to_s
      mensaje = "\nNumero casilla #{@numeroCasilla}\nTipo #{@tipo}\n"
      
      if(tipo == TipoCasilla::CALLE)
        mensaje += "\nCoste #{@coste}\nNumero hoteles #{@numHoteles}\nNumero casas #{@numCasas}\nTitulo = " +@titulo.to_s()
      end
      
      return mensaje
    end   

  end
end
