package it.polimi.GC13.model;

import java.io.Serializable;

/**
 * Represents a chat message containing information about the sender and the content of the message.
 *
 * @param sender the sender of the message
 * @param content the content of the message
 */
public record ChatMessage(String sender, String content) implements Serializable {
}
