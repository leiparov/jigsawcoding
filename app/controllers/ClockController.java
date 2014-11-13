package controllers;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.entities.SesionJigsaw;
import models.services.SesionJigsawService;
import play.Logger;
import play.libs.Akka;
import play.libs.EventSource;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class ClockController extends Controller {
    
    final static ActorRef clock = Clock.instance;
    static Date limite;
    
    private static SesionJigsawService sesionJigsawService = new SesionJigsawService();
    
    public static Result index() {
        return ok(views.html.eventsourceclock.index.render());
    }
    
    public static Result liveClock(Integer id) {
    	
    	SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
    	Date inicioRE = s.getInicioReunionExpertos();
    	int duracionRE = s.getDuracionReunionExpertosEnSegundos();
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(inicioRE);
    	cal.add(Calendar.SECOND, duracionRE);
    	limite = new Date(cal.getTimeInMillis());
    	
		//cal.set(Calendar.YEAR, 2014);
		//cal.set(Calendar.HOUR_OF_DAY, 15);
		//cal.set(Calendar.MINUTE, 50);
		//cal.set(Calendar.SECOND, 0);
    	
        return ok(new EventSource() {  
            public void onConnected() {
               clock.tell(this, null); 
            } 
        });
    }
    
    public static class Clock extends UntypedActor {
        
        static ActorRef instance = Akka.system().actorOf(Props.create(Clock.class));
        
        // Send a TICK message every 100 millis
        static {
            Akka.system().scheduler().schedule(
                Duration.Zero(),
                Duration.create(1000, MILLISECONDS),
                instance, "TICK",  Akka.system().dispatcher(),
                null
            );
        }
        
        List<EventSource> sockets = new ArrayList<EventSource>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm ss");
        
        public void onReceive(Object message) {

            // Handle connections
            if(message instanceof EventSource) {
                final EventSource eventSource = (EventSource)message;
                
                if(sockets.contains(eventSource)) {                    
                    // Browser is disconnected
                    sockets.remove(eventSource);
                    Logger.info("Browser disconnected (" + sockets.size() + " browsers currently connected)");
                    
                } else {                    
                    // Register disconnected callback 
                    eventSource.onDisconnected(new Callback0() {
                        public void invoke() {
                            getContext().self().tell(eventSource, null);
                        }
                    });                    
                    // New browser connected
                    sockets.add(eventSource);
                    Logger.info("New browser connected (" + sockets.size() + " browsers currently connected)");
                    
                }
                
            }             
            // Tick, send time to all connected browsers
            if("TICK".equals(message)) {
                // Send the current time to all EventSource sockets
                List<EventSource> shallowCopy = new ArrayList<EventSource>(sockets); //prevent ConcurrentModificationException
                for(EventSource es: shallowCopy) {
                	Long time = limite.getTime()-new Date().getTime();
                	//Logger.info("Reloj : " + time.toString());
                    //es.sendData(dateFormat.format(cronometro(limite)));
                    es.sendData(cronometro(limite)+"");
                	
                }
                
            }

        }
        
        private int cronometro(Date limite){
        	
        	Calendar max = Calendar.getInstance();
        	max.setTime(limite);
        	Calendar min = Calendar.getInstance();
        	min.setTime(new Date());
        	
        	long milisMax = max.getTimeInMillis();
        	long milisMin = min.getTimeInMillis();
        	
        	long diff = milisMax - milisMin;
        	
        	int segundos = (int)(diff / (1000));
        	
        	
        	
//        	Calendar cal = Calendar.getInstance();
//    		cal.set(Calendar.HOUR_OF_DAY, horas);
//    		cal.set(Calendar.MINUTE, minutos);
//    		cal.set(Calendar.SECOND, segundos);
        	
    		return segundos;
    		
        }
        
    }
  
}
