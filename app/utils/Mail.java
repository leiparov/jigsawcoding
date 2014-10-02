package utils;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail{
	private String host;
	private String email_emisor;  
    private String contrasenia; 

    public Mail(){
    	host = "smtp.gmail.com";
    	email_emisor = "examenesonlineperu";
    	contrasenia = "sylplagji";
    }
    
    public void enviarContrasenia(String email, String nuevaContrasenia){
    	String subject = "Nueva Contraseña";
        String body = "Tu nueva contraseña es: " + nuevaContrasenia;
        String[] receptores = {email};
        enviarMensaje(receptores, subject, body);
    }

    private void enviarMensaje(String[] to, String subject, String body) {
        
    	Properties props = System.getProperties();
        
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.user", email_emisor);
        props.put("mail.smtp.contrasenia", contrasenia);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage mensaje = new MimeMessage(session);

        try {
        	mensaje.setFrom(new InternetAddress(email_emisor));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
            	mensaje.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            mensaje.setSubject(subject);
            mensaje.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, email_emisor, contrasenia);
            transport.sendMessage(mensaje, mensaje.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}