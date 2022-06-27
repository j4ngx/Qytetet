# encoding: UTF-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  class TituloPropiedad
    attr_reader :nombre, :alquiler_base, :factor_revalorizacion, :hipoteca_base, :precio_edificar
    attr_accessor :hipotecada, :casilla
    attr_writer  :propietario
    
    #Constructor 
    def initialize (nombre,hipotecada, alquiler_base, factor_revalorizacion, hipoteca_base, precio_edificar)
      @nombre = nombre
      @hipotecada = hipotecada
      @alquiler_base =  alquiler_base
      @factor_revalorizacion = factor_revalorizacion
      @hipoteca_base = hipoteca_base
      @precio_edificar = precio_edificar
      @casilla
      @propietario = nil
    end
    
    #Constructor sin necesidad de decir si esta hipotecada, ya que por defecto sera false
    def self.crear(nombre, alquiler_base, factor_revalorizacion, hipoteca_base, precio_edificar)
      self.new(nombre,false, alquiler_base, factor_revalorizacion, hipoteca_base, precio_edificar)
    end
    
    #Constructor indicando solo el nombre, los demas valores estaran puestos al minimo
    def self.crear_todo_defecto(nombre)
      self.crear(nombre,50, 0.1, 150, 250)
    end
    
    def tengo_propietario()
      estado = false
      if (@propietario != nil)
        estado = true
      end
      
      return estado
	end
	
	def propietario_encarcelado()
	  return @propietario.encarcelado
	end

  def cobrar_alquiler(coste)
    @propietario.modificar_saldo(coste);
  end

    
    
    
    
    #Metodo que devuelve una cadena de caracteres con la informacion del titulo de la propiead
    def to_s
      texto = ""
      if (@propietario != nil)
        texto = "Propietario: "+ @propietario.nombre + "\n"
      end
      texto = texto +"Nombre #{@nombre}\nHipotecada: #{@hipotecada}\nAlquiler base #{@alquiler_base}\n"+
      "Factor revalorizacion #{@factor_revalorizacion}\nHipoteca base #{@hipoteca_base}\n"+
      "Precio_edificar #{@precio_edificar}\n"
    end
  end
end
