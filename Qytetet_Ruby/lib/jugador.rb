# encoding: UTF-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
require_relative "especulador"
module ModeloQytetet
  class Jugador
    attr_reader :nombre, :propiedades, :saldo
    attr_accessor :casilla_actual, :encarcelado, :carta_libertad, :factor_especulador
  
    def initialize(nombre)
      @encarcelado = false
      @nombre = nombre
      @saldo = 7500
      @carta_libertad = nil
      @casilla_actual = nil
      @propiedades = Array.new
      @factor_especulador = 1
    end
    
    
   
    def to_s
      cadena = "Nombre: #{@nombre} \nSaldo: #{@saldo} \n"
      
      if (@carta_libertad != nil)
        cadena += "Carta libertad: " + @carta_libertad.to_s + "\n"
      end
      
      if (@casilla_actual != nil)
        cadena += "Casilla Actual: " + @casilla_actual.to_s + "\n"
      end
      
      if (!@propiedades.empty?)
        
        cadena += "Propiedades: "
        @propiedades.each do |propiedad|
          cadena += " " + propiedad.to_s;
        end
        
      end
      return cadena
    end
    
    def devolver_carta_libertad
      
      if(@carta_libertad != nil)
        sorpresa = @carta_libertad
        @carta_libertad = nil
      end
      
      return sorpresa
      
    end
    
    def tengo_carta_libertad
      libertad = false
      if(@carta_libertad != nil)
        libertad = true
      end
      
      return libertad
    end
    
    def puedo_vender_propiedad(casilla)
      estado = false
      if(es_de_mi_propiedad(casilla) && !casilla.titulo.hipotecada)
        estado = true
      end
      
      return estado
    end
    
    def modificar_saldo(cantidad)
      @saldo = @saldo + cantidad
    end
    
    def tengo_propiedades
      return !@propiedades.empty?
    end
	
	
    def cuantas_casas_hoteles_tengo
      cant = 0

      @propiedades.each do |p|
        cant +=  p.casilla.numHoteles + p.casilla.numCasas
      end

      return cant
    end

    def obtener_capital
      capital = @saldo
		
      @propiedades.each do |p|	
        capital = capital + p.casilla.coste
        capital = capital + cuantas_casas_hoteles_tengo*p.precio_edificar
			
        if(p.hipotecada)
          capital = capital- p.hipoteca_base
        end
      end

      return capital
    end

    def es_de_mi_propiedad(casilla)
      f = false;

      @propiedades.each do |p|
        if(p.casilla.numeroCasilla == casilla.numeroCasilla)
          f = true	
        end			
      end    
    
      return f
    end
	
    def eliminar_de_mis_propiedades(casilla)
      for i in @propiedades
        if(i.casilla == casilla)
          @propiedades.delete(i)
        end
      end
    end

    def obtener_propiedades_hipotecadas(hipotecada)
      hipotecadas = Array.new

      @propiedades.each do |p|
        if(p.hipotecada && hipotecada)
          hipotecadas << p
        end

        if(!p.hipotecada && !hipotecada)
          hipotecadas << p
        end
      end
      return hipotecadas
    end

    def tengo_saldo(cantidad)
      return cantidad <= @saldo
    end

    def actualizar_posicion(casilla)
      if (casilla.numeroCasilla < @casilla_actual.numeroCasilla) 
        modificar_saldo(Qytetet.SALDO_SALIDA);
      end

      tiene_propietario = false;
      @casilla_actual = casilla;

      #Miramos si es de tipo calle
      if (casilla.soy_edificable) 

        #Si tiene propietario y si no esta encarcelado se cobrara el alquiler
        tiene_propietario = casilla.tengo_propietario;
        if (tiene_propietario) 
          esta_encarcelado = casilla.preopietario_encarcelado
          if (!esta_encarcelado) 
            coste_alquiler = casilla.cobrar_alquiler
            modificar_saldo(-coste_alquiler);
          end
        end
      end
      
      if (casilla.tipo == TipoCasilla::IMPUESTO) 
        coste = casilla.coste
        pagar_impuestos(coste)
      end
      return tiene_propietario;
    end
	
	
    def comprar_titulo
      comprar = false;
      if(@casilla_actual.soy_edificable)
        
        tengo_propietario = @casilla_actual.tengo_propietario
        
        if(!tengo_propietario)
          coste_compra = @casilla_actual.coste;
          if(coste_compra <= @saldo)
            titulo = @casilla_actual.asignar_propietario(self)
            @propiedades << titulo;
            modificar_saldo(-coste_compra);
            comprar = true;
          end
                
        end
      end

      return comprar;
    end
    
    def puedo_edificar_casa(casilla)
      puedo_eficiar = false;
      if (es_de_mi_propiedad(casilla)) 
        precio = casilla.get_precio_edificar
        puedo_eficiar = tengo_saldo(precio);
      end

      return puedo_eficiar;
    end
    
    def puedo_edificar_hotel(casilla)
      puedo_edificar = false;
      if(es_de_mi_propiedad(casilla))
        precio = casilla.get_precio_edificar
        puedo_edificar = tengo_saldo(precio)
      end
      return puedo_edificar
    end
    
    def puedo_hipotecar(casilla)
      return es_de_mi_propiedad(casilla);
    end
    
    def pagar_cobrar_por_casa_hotel(cantidad)
      numero_total = cuantas_casas_hoteles_tengo
      modificar_saldo(numero_total*cantidad);
    end
    
    def vender_propiedad(casilla)
      precio_venta = casilla.vender_titulo;
      modificar_saldo(precio_venta);
      eliminar_de_mis_propiedades(casilla);
    end
    def ir_a_carcel(casilla)
      @casilla_actual = casilla
      @encarcelado = true
    end 
    
    def pagar_libertad(cantidad)
       saldo = tengo_saldo(cantidad)
       if saldo
         modificar_saldo(-cantidad)
       end
      return saldo
    end
    def puedo_pagar_hipoteca(casilla)
      puedo_pagar = false
      if(casilla.get_coste_hipoteca <= saldo && es_de_mi_propiedad(casilla))
        puedo_pagar = true
      end
      return puedo_pagar
    end
    
    def convertirme(fianza) 
        especulador = Especulador.new(self, fianza);
        
        especulador.propiedades.each do |propiedad|
          propiedad.propietario = especulador
        end
        
        return especulador
    end
    def pagar_impuestos(cantidad)
      modificar_saldo(cantidad)
    end
    private :cuantas_casas_hoteles_tengo, :es_de_mi_propiedad, :eliminar_de_mis_propiedades, :tengo_saldo
    protected :pagar_impuestos
  end
end
