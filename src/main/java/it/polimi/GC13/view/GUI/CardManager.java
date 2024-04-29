package it.polimi.GC13.view.GUI;

import it.polimi.GC13.exception.CardNotFoundException;
import it.polimi.GC13.exception.WrongCardException;
import it.polimi.GC13.model.PlayableCard;

import javax.swing.*;

public interface CardManager {

    ImageIcon showCard(PlayableCard card); //permette di associare alla carta con tale serialNumber i corrispettivi front e back

}
