package utils;

import org.joda.time.*;
import org.joda.time.format.*;

public class FormatoFechaHora {
    public static String dateFormat = "dd/MM/yyyy";
    public static String timeFormat12 = "hh:mm a";
    public static String timeFormat24 = "HH:mm";
    public static DateTimeZone ZONA_PERU = DateTimeZone.forOffsetHours(-5); 

    private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(dateFormat);
    private static DateTimeFormatter timeFormatter12 = DateTimeFormat.forPattern(timeFormat12);
    private static DateTimeFormatter timeFormatter24 = DateTimeFormat.forPattern(timeFormat24);

    public static LocalDate obtenerFecha(String dato){
        return LocalDate.parse(dato, dateFormatter);
    }
    
    public static LocalTime obtenerHora(String dato){
        return LocalTime.parse(dato,
                formato12(dato) ? timeFormatter12 : timeFormatter24);
    }

    private static boolean formato12(String texto) {
        return texto.toUpperCase().contains("AM") ||
                texto.toUpperCase().contains("PM");
    }
}
