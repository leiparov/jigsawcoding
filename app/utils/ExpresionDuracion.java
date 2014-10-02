package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ExpresionDuracion {
    
    private static Pattern patronHoras = Pattern.compile("(\\d+)\\s?h");
    private static Pattern patronMinutos = Pattern.compile("(\\d+)\\s?m");
    
    private int horas = 0;
    private int minutos = 0;
    
    public ExpresionDuracion(String texto){
        if(texto == null) return;
        
        Matcher buscaHoras = patronHoras.matcher(texto);
        Matcher buscaMinutos = patronMinutos.matcher(texto);
        
        if(!buscaHoras.find(0) && !buscaMinutos.find(0))
            throw new IllegalArgumentException("No es expresion de duracion");
        
        //Recordar que el grupo 0 es toda la expresion
        if(buscaHoras.find(0)) this.horas = Integer.parseInt(buscaHoras.group(1));
        if(buscaMinutos.find(0)) this.minutos = Integer.parseInt(buscaMinutos.group(1));
    }
    
    public ExpresionDuracion(String horas, String minutos){
        if(horas != null && !horas.isEmpty()) this.horas = Integer.parseInt(horas);
        if(minutos != null && !minutos.isEmpty()) this.minutos = Integer.parseInt(minutos);
    }
    
    public int toMinutos(){
        return 60*horas + minutos;
    }

}
