package models.chat;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.HashMap;
import java.util.Map;

import play.libs.Akka;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A chat room is an Actor.
 */
public class ChatRoom extends UntypedActor {

	// Default room.
	// static ActorRef chatRoom = Akka.system().actorOf(
	// Props.create(ChatRoom.class));

	// Hashmap to keep references to actors(rooms)
	public static HashMap<String, ActorRef> openedChats = new HashMap<>();

	// added unique identifier to know which room join
	final String chatId;
	public ChatRoom(String chatId) {
		this.chatId = chatId;
	}

	// static ActorRef chatRoom;

	/**
	 * Join the default room.
	 */
	public static void join(final String username, String chatid,
			WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out)
			throws Exception {

		final ActorRef chatRoom;

		// Find the good room to bind to in the hashmap

		if (openedChats.containsKey(chatid)) {
			chatRoom = openedChats.get(chatid);
		} else {
			chatRoom = Akka.system().actorOf(
					Props.create(ChatRoom.class, chatid));
			// chatRoom = Akka.system().actorOf(
			// Props.create(new Creator<ChatRoom>() {
			//
			// private static final long serialVersionUID = 1L;
			//
			// public ChatRoom create() {
			// return new ChatRoom(chatId);
			// }
			// }));
			openedChats.put(chatid, chatRoom);
		}

		// Send the Join message to the room
		String result = (String) Await.result(
				ask(chatRoom, new Join(username, out), 1000),
				Duration.create(1, SECONDS));

		if ("OK".equals(result)) {
			// For each event received on the socket,
			in.onMessage(new Callback<JsonNode>() {
				public void invoke(JsonNode event) {
					// Send a Talk message to the room.
					chatRoom.tell(
							new Talk(username, event.get("text").asText()),
							null);
				}
			});
			// When the socket is closed.
			in.onClose(new Callback0() {
				public void invoke() {
					// Send a Quit message to the room.
					chatRoom.tell(new Quit(username), null);
				}
			});
		} else {
			// Cannot connect, create a Json error.
			ObjectNode error = Json.newObject();
			error.put("error", result);
			// Send the error to the socket.
			out.write(error);
		}
	}

	// Members of this room.
	Map<String, WebSocket.Out<JsonNode>> members = new HashMap<String, WebSocket.Out<JsonNode>>();

	public void onReceive(Object message) throws Exception {
		if (message instanceof Join) {
			// Received a Join message
			Join join = (Join) message;
			// Check if this username is free.
			if (members.containsKey(join.username)) {
				getSender().tell("This username is already used", getSelf());
			} else {
				members.put(join.username, join.channel);
				notifyAll("join", join.username, "ha entrado al chat-room");
				getSender().tell("OK", getSelf());
			}
		} else if (message instanceof Talk) {
			// Received a Talk message
			Talk talk = (Talk) message;
			notifyAll("talk", talk.username, talk.text);

		} else if (message instanceof Quit) {
			// Received a Quit message
			Quit quit = (Quit) message;
			members.remove(quit.username);
			notifyAll("quit", quit.username, "ha dejado el chat-room");
		} else {
			unhandled(message);
		}
	}

	// Send a Json event to all members
	public void notifyAll(String kind, String user, String text) {
		for (WebSocket.Out<JsonNode> channel : members.values()) {
			ObjectNode event = Json.newObject();
			event.put("kind", kind);
			event.put("user", user);
			event.put("message", text);
			event.put("icon", user.substring(0, 2).toUpperCase());
			ArrayNode m = event.putArray("members");
			for (String u : members.keySet()) {
				m.add(u);
			}
			channel.write(event);
		}
	}

	// -- Messages

	public static class Join {

		final String username;

		final WebSocket.Out<JsonNode> channel;

		public Join(String username, WebSocket.Out<JsonNode> channel) {
			this.username = username;
			this.channel = channel;

		}

	}

	public static class Talk {

		final String username;

		final String text;

		public Talk(String username, String text) {
			this.username = username;

			this.text = text;
		}

	}

	public static class Quit {

		final String username;

		public Quit(String username) {
			this.username = username;

		}

	}

}
