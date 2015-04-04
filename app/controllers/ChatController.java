package controllers;

import models.chat.ChatRoom;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

public class ChatController extends Controller{
	/**
     * Display the home page.
     */
//	public static Result index(){
//		//return ok(views.html.chat.index.render());
//		return noContent();
//	}
	/**
     * Display the chat room.
     */
//    public static Result chatRoom(String username) {
//        if(username == null || username.trim().equals("")) {
//            flash("error", "Please choose a valid username.");
//            return redirect(routes.ChatController.index());
//        }
//        //return ok(views.html.chat.chatRoom.render(username));
//        return noContent();
//    }

    public static Result chatRoomJs(String username, String chatid) {
        return ok(views.js.chat.chatRoom.render(username, chatid));
    }
    /**
     * Handle the chat websocket.
     */
    public static WebSocket<JsonNode> chat(final String username, final String chatid) {
        return new WebSocket<JsonNode>() {
            
            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){                
                // Join the chat room.
                try { 
                	
                	play.Logger.info("chatid "+chatid);
                    ChatRoom.join(username, chatid,  in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    play.Logger.info(ex.getMessage());
                }
            }
        };
    }
    
}
