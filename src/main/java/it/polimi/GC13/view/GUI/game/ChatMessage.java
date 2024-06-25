package it.polimi.GC13.view.GUI.game;

/**
 * Represents a chat message containing information about the sender and the content of the message.
 *
 * @param sender the sender of the message
 * @param content the content of the message
 */
public record ChatMessage(String sender, String content) {
}
