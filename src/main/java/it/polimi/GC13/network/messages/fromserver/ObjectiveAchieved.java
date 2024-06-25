package it.polimi.GC13.network.messages.fromserver;

import java.io.Serializable;

/**
 * used to track for each player which are the objective cards that gave him points and the amount
 * @param serialCard serial number of the objective card
 * @param pointsAchieved points achieved thanks to the objective card
 */
public record ObjectiveAchieved(int serialCard, int pointsAchieved) implements Serializable {
}
