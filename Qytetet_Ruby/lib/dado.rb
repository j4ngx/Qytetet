# encoding: UTF-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
require "singleton"
module ModeloQytetet
  class Dado
    include Singleton
  
    def tirar
      return rand(5) + 1
      #return 1
    end
    
  end
end
