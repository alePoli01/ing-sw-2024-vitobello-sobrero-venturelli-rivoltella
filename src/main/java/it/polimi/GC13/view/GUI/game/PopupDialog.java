package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.exception.CardNotFoundException;
import it.polimi.GC13.exception.WrongCardException;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.view.GUI.CardManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PopupDialog extends JDialog implements CardManager, ActionListener{
    private PlayableCard card; //rappresenta la carta scoperta di uno dei due mazzi da poter pescare
    public PopupDialog(JFrame owner /*, PlayableCard drawableCard*/) {
        super(owner, "Card drawable", true);
        //this.card = drawableCard;
        setSize(400, 300);
        setResizable(false);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Vuoi pescare questa carta?",  SwingConstants.CENTER);
        JButton confirmButton = new JButton("Pesca");
        confirmButton.setPreferredSize(new Dimension(150, 25));
        JButton closeButton = new JButton("Chiudi");
        closeButton.setPreferredSize(new Dimension(150, 25));

        //bozza della chiamata del metodo
        /*JLabel labelIcon = new JLabel(showCard(card), SwingConstants.CENTER);
        add(labelIcon, BorderLayout.CENTER);*/

        //prova ridimensionamento immagine
        Image image = new ImageIcon("src/main/utils/dead-fish.png").getImage();
        Image newImg = image.getScaledInstance(image.getWidth(null)/3, image.getHeight(null)/3, Image.SCALE_SMOOTH);
        JLabel labelIcon = new JLabel(new ImageIcon(newImg), SwingConstants.CENTER); //immagine di prova
        add(labelIcon, BorderLayout.CENTER);

        //di prova, chiudono il popup e basta
        /*confirmButton.addActionListener(e -> dispose());
        closeButton.addActionListener(e -> dispose());*/


        confirmButton.addActionListener(this);
        closeButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        add(label, BorderLayout.NORTH);
        buttonPanel.add(confirmButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(owner);
    }

    //permette di visualizzare la carta scoperta davanti al mazzo
    @Override
    public ImageIcon showCard(PlayableCard card) {
        ImageIcon img = new ImageIcon();
        File directory;
        int error;
        String serialNumber = Integer.toString(card.serialNumber); //trasforma il serialNumber in una stringa (  in alternativa String s = String.valueOf(serialNumber)  )

        //Da verificare se scritta bene
        try {
            if(card.cardType.equals(CardType.GOLD)) directory = new File("src/main/utils/gold_card/gold_card_front");
            else if(card.cardType.equals(CardType.RESOURCE)) directory = new File("src/main/utils/resource_card/resource_card_front");
            else {
                error = 0;
                throw new WrongCardException(card, error);
            }
            if (directory.exists() && directory.isDirectory()) { // Verifica che la directory esista e che sia una directory
                File[] files = directory.listFiles(); // Ottieni un elenco dei file nella directory
                if(files != null) {
                    for(File file : files){
                        if(file.getName().contains(serialNumber)){
                            Image image = new ImageIcon(file.getName()).getImage();
                            Image newImg = image.getScaledInstance(image.getWidth(null)/5, image.getHeight(null)/5, Image.SCALE_SMOOTH);
                            img.setImage(new ImageIcon(newImg).getImage());

                            //img.setImage(new ImageIcon(file.getName()).getImage());
                        } else {
                            error = 1;
                            throw new WrongCardException(card, error);
                        }
                    }
                } else {
                    error = 2;
                    throw new WrongCardException(card, error);
                }
            } else {
                error = 3;
                throw new WrongCardException(card, error);
            }
        } catch(WrongCardException e){
            System.out.println(e.getMessage());
            Image image = new ImageIcon("src/main/utils/dead-fish.png").getImage();
            Image newImg = image.getScaledInstance(image.getWidth(null)/3, image.getHeight(null)/3, Image.SCALE_SMOOTH);
            img.setImage(new ImageIcon(newImg).getImage());
            return img;
        }
        return img;
    }


    //TODO: da implementare l'azione del bottone "Pesca"
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Pesca")){
            //aggiunge la carta alla mano
            dispose();
        }else if(e.getActionCommand().equals("Chiudi")){
            dispose(); //dovrebbe chiudere il popup senza fare niente, da verificare
        }
    }
}
