package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/demo/")
public class RestDemo {
    
    Result result = new Result();
    
    @GetMapping("saludo")
    public ResponseEntity saludo(@RequestParam("nombre") String nombre){
        
        try{
            
            if (nombre.matches("\\d+")) {
                result.correct = false;
                result.errorMessage = "El nombre no debe de contener numeros";
                result.status = 400;
            }else{
                
                result.correct = true;
                result.status = 200;
                result.Object = "Hola, te mando un saludo " + nombre; 
                
            }
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        
        return ResponseEntity.status(result.status).body(result);
        
    }
    
    @GetMapping("divicion")
    public ResponseEntity Divicion(@RequestParam("numeroUno") int numeroUno, @RequestParam("numeroDos") int numeroDos){
        
        try{
            
            if(numeroDos == 0){
                
                result.correct = false;
                result.errorMessage = "No se puede dividir entre 0";
                result.Object = null;
                result.status = 400;
                
            }else{
                int divicion = numeroUno / numeroDos;
                result.correct = true;
                result.status = 200;
                result.Object = "La divicion de: "+ numeroUno + " / " + numeroDos + " es:" + divicion;
                
            }
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        
        
        return ResponseEntity.status(result.status).body(result);
    }
    
    @GetMapping("multi")
    public ResponseEntity Multi(@RequestParam("numeroUno") int numeroUno, @RequestParam("numeroDos") int numeroDos){
        
        try{
            
            if(numeroUno == 0 || numeroDos == 0){
                result.correct = true;
                result.errorMessage = null;
                result.Object = "Resultado: 0";
                result.status = 200;
            }else{
                
                int multi = numeroUno * numeroDos;
                result.correct = true;
                result.Object = "Respuesta de la multiplicacion de: " + numeroUno + " * " + numeroDos + " es:" + multi;
                
            }
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.Object = null;
            result.status = 500;
        }
        
        
        return ResponseEntity.status(result.status).body(result);
    }
    
    @PostMapping("/sumaN")
    public ResponseEntity sumaN(@RequestParam("numero") List<Integer> numeros){
        
        try{
            
            if(numeros == null){
                result.correct = false;
                result.errorMessage = "Debes de ingresa numeros para sumar";
                result.status = 400;
            }else{
                
                int suma = 0;
                
                for (int num : numeros){
                    suma += num;
                }
                
                result.correct = true;
                result.Object = "El resultado de la suma es: " + suma;
                result.status = 200;
                
            }
            
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
            
        }
        
        
        return ResponseEntity.status(result.status).body(result);
    }
    
}
