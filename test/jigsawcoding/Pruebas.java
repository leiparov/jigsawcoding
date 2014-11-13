package jigsawcoding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Pruebas {
	public static void main (String args[]){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY MM dd HH mm ss");
		
		Date inicio = new Date(11110);
		
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.HOUR_OF_DAY, 15);
		cal.set(Calendar.MINUTE, 50);
		cal.set(Calendar.SECOND, 0);
		
		System.out.println(cal.getTime());
		cal.add(Calendar.SECOND, 60);
		System.out.println(cal.getTime());
		
	}
}
